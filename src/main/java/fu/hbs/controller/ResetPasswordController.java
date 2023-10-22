/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 14/10/2023    1.0        HieuLBM          First Deploy
 *  * 
 */
package fu.hbs.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fu.hbs.entities.Token;
import fu.hbs.exceptionHandler.ResetExceptionHandler;
import fu.hbs.service.dao.ResetService;
import fu.hbs.service.dao.TokenService;
import fu.hbs.service.dao.UserService;
import fu.hbs.utils.StringDealer;
import jakarta.servlet.http.HttpSession;

@Controller
public class ResetPasswordController {
	private ResetService restPasswordService;
	private TokenService tokenService;
	private StringDealer stringDealer;
	private UserService userService;

	public ResetPasswordController(ResetService restPasswordService, TokenService tokenService,
			UserService userService) {
		this.restPasswordService = restPasswordService;
		this.tokenService = tokenService;
		this.stringDealer = new StringDealer();
		this.userService = userService;
	}

	@GetMapping("/hbs/resetPassword")
	public String showResetPasswordRequestForm() {
		return "forgotPassword"; // Thay đổi thành tên giao diện của bạn
	}

	@PostMapping("/hbs/resetPassword")
	public String handleResetPasswordRequest(@RequestParam String email, Model model) throws ResetExceptionHandler {
		String checkemail = stringDealer.trimMax(email);
		if ((!checkemail.equals("")) && !stringDealer.checkEmailRegex(checkemail)) { /* Email is not valid */
			model.addAttribute("email", "Email không đúng định dạng");
			model.addAttribute("saveEmail", checkemail);
			return "forgotPassword";
		} else {
			if (userService.checkUserbyEmail(checkemail)) {
				restPasswordService.resetPasswordRequest(checkemail);
				model.addAttribute("successMessage", "Gửi email thành công!");
				return "forgotPassword";
			}
			model.addAttribute("saveEmail", checkemail);
			model.addAttribute("errMess", "Email không tồn tại!");
			return "forgotPassword";

		}
	}

	@GetMapping("/hbs/reset-password")
	public String showResetPasswordForm(@RequestParam("token") String token, Model model, HttpSession session) {
		Token tokenEntity = tokenService.findTokenByValue(token);
		if (tokenEntity != null) {
			// Kiểm tra xem token có hợp lệ (chưa hết hạn) hay không
			if (tokenEntity.getExpirationDate().after(new Date())) {
				System.out.println("ok");
				session.setAttribute("token", tokenEntity.getToken());
				return "resetPassword";
			}
		}
		return "error";
	}

	@PostMapping("/hbs/reset-password")
	public String handleResetPassword(@RequestParam("token") String token, @RequestParam("password") String Password,
			@RequestParam("repassword") String rePassword, Model model, HttpSession session)
			throws ResetExceptionHandler {

		String resetToken = (String) session.getAttribute("token");

		Token tokenEntity = tokenService.findTokenByValue(resetToken);
		if (tokenEntity == null || tokenEntity.isExpired()) {
			System.out.println("full time");
			model.addAttribute("errorMessage", "Hết hạn thời gian!!!");
			return "resetPassword";
		}
		// Password
		String password = stringDealer.trimMax(Password);
		if (password.equals("")) { /* Password is empty */
			model.addAttribute("pass", "Mật khẩu không được để trống");
			return "resetPassword";
		}
		if (!stringDealer.checkPasswordRegex(password)) { /* Password is not valid */
			model.addAttribute("pass", "Mật khẩu không hợp lệ");
			return "resetPassword";
		}
		// Confirm password
		String rePassword1 = stringDealer.trimMax(rePassword);

		if (!password.equals(rePassword)) { /* Password not match */
			model.addAttribute("pass", "Mật khẩu không khớp");
			return "resetPassword";
		}
		// Password match
		if (password.equals(rePassword)) {
			if (restPasswordService.resetPassword(tokenEntity, rePassword)) {
				model.addAttribute("valid", "Đổi mật khẩu thành công");
				session.removeAttribute("token");
				return "resetPassword";
			}
		}

		return "resetPassword";

	}
}
