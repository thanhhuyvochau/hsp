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
package fu.hbs.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import fu.hbs.entities.Token;
import fu.hbs.entities.User;
import fu.hbs.repositoties.TokenRepository;
import fu.hbs.repositoties.UserRepository;
import fu.hbs.service.dao.TokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class RestPasswordService {

	private UserRepository userRepository;

	private TokenRepository tokenRepository;
	private TokenService tokenService;

	private JavaMailSender javaMailSender;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	TemplateEngine templateEngine;

	public RestPasswordService(UserRepository userRepository, TokenService tokenServices, JavaMailSender javaMailSender,
			TokenRepository tokenRepository) {
		this.userRepository = userRepository;
		this.tokenService = tokenServices;
		this.javaMailSender = javaMailSender;
		this.tokenRepository = tokenRepository;
	}

	public void resetPasswordRequest(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			Token token = tokenService.createToken(user);

			// Tạo nội dung email với mã token và thời gian hết hạn
			Context context = new Context();
			context.setVariable("resetLink", "http://localhost:8080/hbs/reset-password?token=" + token.getToken());
			context.setVariable("expirationDate", token.getExpirationDate());

			String emailContent = templateEngine.process("reset-password-template", context);

			// Gửi email
			sendResetPasswordEmail(user.getEmail(), "Quên Mật Khẩu", emailContent);
		}
	}

	public boolean resetPassword(Token token, String newPassword) {
		String encodedPassword = passwordEncoder.encode(newPassword);
		Optional<User> userOptional = userRepository.findById(token.getUserId());
		// Đặt lại mật khẩu cho người dùng
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setPassword(encodedPassword);
			userRepository.save(user);
			System.out.println(token.getToken());
			System.out.println(token.getId());
			tokenRepository.deleteById(token.getId());
			return true;
		}
		return false;
	}

	public void sendResetPasswordEmail(String recipientEmail, String subject, String emailContent) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom("3HKT@gmail.com");
			helper.setTo(recipientEmail);
			helper.setSubject(subject);
			helper.setText(emailContent, true); // Sử dụng HTML

			javaMailSender.send(message);
		} catch (MessagingException e) {
			// Xử lý lỗi gửi email
			e.printStackTrace();

		}
	}
}
