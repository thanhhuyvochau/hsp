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
package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entities.Token;
import com.example.entities.User;
import com.example.repositoties.UserRepository;
import com.example.service.dao.TokenService;

@Service
public class RestPasswordService {

	private UserRepository userRepository;

	private TokenService tokenService;

	private JavaMailSender javaMailSender;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public RestPasswordService(UserRepository userRepository, TokenService tokenServices,
			JavaMailSender javaMailSender) {
		this.userRepository = userRepository;
		this.tokenService = tokenServices;
		this.javaMailSender = javaMailSender;
	}

	public void resetPasswordRequest(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			Token token = tokenService.createToken(user);

			// Tạo nội dung email với mã token và thời gian hết hạn
			String emailContent = "Nhấn vào đây để đổi mật khẩu của bạn:"
					+ "http://localhost:8080/hbs/reset-password?token=" + token.getToken();
			emailContent += "\nThis link will expire at: " + token.getExpirationDate(); // Thời gian hết hạn

			// Gửi email
			sendResetPasswordEmail(user.getEmail(), emailContent);
		}
	}

	public boolean resetPassword(User user, String newPassword) {
		String encodedPassword = passwordEncoder.encode(newPassword);
		// Đặt lại mật khẩu cho người dùng
		user.setPassword(encodedPassword);
		userRepository.save(user);

		return true;
	}

	public void sendResetPasswordEmail(String to, String content) {
		// Gửi email chứa nội dung với mã token và thời gian hết hạn
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Password Reset Request");
		message.setText(content);
		javaMailSender.send(message);
	}
}
