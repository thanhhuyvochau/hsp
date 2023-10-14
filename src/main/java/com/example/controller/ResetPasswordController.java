package com.example.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entities.Token;
import com.example.service.dao.TokenService;
import com.example.service.impl.RestPasswordService;

@Controller
public class ResetPasswordController {
	private RestPasswordService restPasswordService;
	private TokenService tokenService;

	public ResetPasswordController(RestPasswordService restPasswordService, TokenService tokenService) {
		this.restPasswordService = restPasswordService;
		this.tokenService = tokenService;

	}

	@GetMapping("/hbs/resetPassword")
	public String showResetPasswordRequestForm() {
		return "forgotPassword"; // Thay đổi thành tên giao diện của bạn
	}

	@PostMapping("/hbs/resetPassword")
	public String handleResetPasswordRequest(@RequestParam String email, Model model) {
		// Gửi email chứa token đến địa chỉ email của người dùng
		restPasswordService.resetPasswordRequest(email);
		model.addAttribute("successMessage", "send email success!");
		return "forgotPassword"; // Thay đổi thành trang mà bạn muốn chuyển hướng sau khi gửi yêu cầu đặt lại
									// mật khẩu
	}

	@GetMapping("/hbs/reset-password")
	public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
		Token tokenEntity = tokenService.findTokenByValue(token);
		if (tokenEntity != null) {
			// Kiểm tra xem token có hợp lệ (chưa hết hạn) hay không
			if (tokenEntity.getExpirationDate().after(new Date())) {
				System.out.println("ok");
				model.addAttribute("token", tokenEntity.getToken());
				return "resetPassword"; // Thay đổi thành tên giao diện của bạn
			}
		}
		return "redirect:/error"; // Thay đổi thành trang lỗi hoặc thông báo
	}

	@PostMapping("/hbs/reset-password")
	public String handleResetPassword(@RequestParam("token") String token,
			@RequestParam("repassword") String rePassword, Model model) {
		Token tokenEntity = tokenService.findTokenByValue(token);

		if (tokenEntity == null) {
			System.err.println("Change failed");
			model.addAttribute("errorMessage", "invalid token!");
			return "resetPassword"; // Thay đổi thành trang lỗi hoặc thông báo
		}

		// Kiểm tra xem token có hợp lệ (chưa hết hạn) hay không
		if (tokenEntity.getExpirationDate().after(new Date())) {
			if (restPasswordService.resetPassword(tokenEntity.getUser(), rePassword)) {
				// Đặt lại mật khẩu thành công, bạn có thể đăng nhập lại
				return "redirect:/login"; // Thay đổi thành trang mà bạn muốn chuyển hướng sau khi đặt lại mật khẩu
											// thành công
			}

		}
		return "redirect:/login";

	}
}
