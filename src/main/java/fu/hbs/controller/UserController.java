/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 04/10/2023    1.0        HieuLBM          First Deploy
 * 21/10/2023	 2.0		HieuLBM			 view, update profile
 * 25/10/2023	 3.0		HieuLBM			 change password
 */
package fu.hbs.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fu.hbs.dto.UserDto;
import fu.hbs.entities.User;
import fu.hbs.exceptionHandler.UserIvalidException;
import fu.hbs.exceptionHandler.UserNotFoundException;
import fu.hbs.service.dao.UserService;
import fu.hbs.utils.StringDealer;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller

public class UserController {
	private UserService userService;
	private StringDealer stringDealer;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserController(UserService userService) {
		this.userService = userService;
		this.stringDealer = new StringDealer();
	}

	@ModelAttribute("userdto")
	public UserDto userResgistrationDto() {
		return new UserDto();
	}

	@GetMapping("/registration")
	public String registerForm() {
		return "authentication/registration";
	}

	@PostMapping("/registration")
	public String registerUserAccount(@ModelAttribute("userdto") UserDto userDto) {
		if (userService.checkUserbyEmail(userDto.getUserEmail())) {
			return "redirect:/registration?emailexist";
		}
		if (userDto.getUserPassword().equals(userDto.getCheckPass()) == false) {
			return "redirect:/registration?checkpass";
		}
		userDto.setStatus(true);
		// userService.save(userDto);

		// Lưu thông tin người dùng
		userService.save(userDto);

		return "redirect:/registration?success";
	}

	@GetMapping("/customer/viewProfile")
	public String viewUserProfile(Model model, Authentication authentication) throws UserNotFoundException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.getUserbyEmail(userDetails.getUsername());
		model.addAttribute("user", user);
		return "profile/viewProfile";
	}

	@GetMapping("/customer/updateprofile")
	public String viewEditUserProfile(Model model, Authentication authentication) throws UserNotFoundException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.getUserbyEmail(userDetails.getUsername());
		model.addAttribute("user", user);
		return "profile/updateProfile";
	}

	@PostMapping("/customer/updateprofile")
	public String updateProfile(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model,
			@RequestParam("file") MultipartFile file) throws UserNotFoundException, UserIvalidException, IOException {
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "profile/updateProfile";
		}
		User user1 = userService.findById(user.getUserId());
		if (user1 != null) {
			user1.setName(user.getName());
			user1.setPhone(user.getPhone());
			user1.setAddress(user.getAddress());
			user1.setDob(user.getDob());
			user1.setGender(user.getGender());
			user1.setImage(file.getOriginalFilename());
			userService.update(user1);
			try {
				uploadFile(file);
			} catch (Exception e) {
				return "redirect:/customer/updateprofile?FailsFile";
			}

		}
		return "redirect:/customer/updateprofile?changeSuccess";

	}

	@GetMapping("/customer/changepass")
	public String viewChangePassword(Model model, Authentication authentication) throws UserNotFoundException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.getUserbyEmail(userDetails.getUsername());
		model.addAttribute("user", user);
		return "profile/changepassword";
	}

	@PostMapping("/customer/changepass")
	public String UserChangePassword(@RequestParam("oldpassword") String oldpassword,
			@RequestParam("newpassword") String newpassword, @RequestParam("confirmpassword") String confirmpassword,
			Model model, final RedirectAttributes redirectAttributes, HttpSession session) {
		UserDetails user = (UserDetails) session.getAttribute("accountDetail");
		User user1 = userService.getUserbyEmail(user.getUsername());
		System.out.println(user1);

		if (!passwordEncoder.matches(oldpassword, user1.getPassword())) {
			model.addAttribute("pass", "Mật khẩu cũ không đúng");
			model.addAttribute("oldpassword", oldpassword);
			return "profile/changepassword";
		}
		model.addAttribute("oldpassword", oldpassword);

		if (!stringDealer.checkPasswordRegex(newpassword)) { /* Password is not valid */
			model.addAttribute("pass1", "Mật khẩu mới không hợp lệ");
			model.addAttribute("newpassword", newpassword);
			return "profile/changepassword";
		}
		model.addAttribute("newpassword", newpassword);

		if (passwordEncoder.matches(newpassword, user1.getPassword())) {// oldpassword != newPassword
			model.addAttribute("pass4", "Mật khẩu nhập mới không được trùng với mật khẩu cũ");
			model.addAttribute("oldpassword", oldpassword);
			return "profile/changepassword";

		}
		model.addAttribute("oldpassword", oldpassword);

		if (!stringDealer.checkPasswordRegex(confirmpassword)) {
			model.addAttribute("pass3", "Mật khẩu nhập lại không hợp lệ");

			model.addAttribute("confirmpassword", confirmpassword);
			return "profile/changepassword";
		}

		model.addAttribute("confirmpassword", confirmpassword);

		if (!newpassword.trim().equals(confirmpassword.trim())) { /* Password not match */
			model.addAttribute("pass2", "Mật khẩu không khớp");
			model.addAttribute("newpassword", newpassword);
			model.addAttribute("confirmpassword", confirmpassword);
			return "profile/changepassword";
		}

		// Password match

		String encodedPassword = passwordEncoder.encode(confirmpassword);
		user1.setPassword(encodedPassword);
		userService.save(user1);
		return "redirect:/customer/changepass?changeSuccess";

	}

	public void uploadFile(MultipartFile file) throws IOException {
		// Đường dẫn đến thư mục trong dự án của bạn
		String uploadDirectory = "src/main/resources/static/assets/img"; // Thay đổi thành đường dẫn thư mục của bạn

		// Tạo đường dẫn đến tệp đích
		String destinationPath = uploadDirectory + File.separator + file.getOriginalFilename();

		// Tạo một tệp đích và ghi nội dung của tệp tải lên vào tệp đó
		File destinationFile = new File(destinationPath);

		try (FileOutputStream fileOutputStream = new FileOutputStream(destinationFile)) {
			fileOutputStream.write(file.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
