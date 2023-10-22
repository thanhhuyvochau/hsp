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
 * 
 */
package fu.hbs.controller;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fu.hbs.dto.UserDto;
import fu.hbs.entities.User;
import fu.hbs.exceptionHandler.UserIvalidException;
import fu.hbs.exceptionHandler.UserNotFoundException;
import fu.hbs.service.dao.UserService;
import jakarta.validation.Valid;

@Controller

public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
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

	@GetMapping("/customer/profile")
	public String viewUserProfile(Model model, Authentication authentication) throws UserNotFoundException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userService.getUserbyEmail(userDetails.getUsername());
		model.addAttribute("user", user);
		System.out.println(user);
		return "userProfile";
	}

	@PostMapping("/customer/profile")
	public String updateProfile(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model,
			@RequestParam("file") MultipartFile file) throws UserNotFoundException, UserIvalidException, IOException {
		System.out.println(user);
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "userProfile";
		}
		User user1 = userService.findById(user.getUserId());
		if (user1 != null) {
			user1.setName(user.getName());
			user1.setPhone(user.getPhone());
			user1.setAddress(user.getAddress());
			user1.setDob(user.getDob());
			user1.setGender(user.getGender());
			userService.update(user1);

		}

		return "redirect:/customer/profile?changeSuccess";

	}
}
