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

import java.util.Calendar;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.entities.Token;
import com.example.entities.User;
import com.example.repositoties.TokenRepository;
import com.example.service.dao.TokenService;

@Service("tokenService")
public class tokenServiceImpl implements TokenService {

	private TokenRepository tokenRepository;

	public tokenServiceImpl(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	@Override
	public Token createToken(User user) {
		Token existingToken = tokenRepository.findByUserEmail(user.getEmail());

		// Nếu đã tồn tại mã token cho người dùng, hãy cập nhật mã token cũ
		if (existingToken != null) {
			existingToken.setToken(generateToken());
			// Cập nhật thời gian hết hạn, ví dụ: 24 giờ sau thời điểm cập nhật
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, 2);
			existingToken.setExpirationDate(calendar.getTime());
			return tokenRepository.save(existingToken);
		}

		Token newToken = new Token();
		newToken.setUser(user);
		newToken.setToken(generateToken());

		// Thiết lập thời gian hết hạn cho token (ví dụ: 24 giờ sau thời điểm tạo)
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 2);
		newToken.setExpirationDate(calendar.getTime());

		return tokenRepository.save(newToken);
	}

	@Override
	public Token findTokenByValue(String tokenValue) {
		Token token = tokenRepository.findByToken(tokenValue);
		// Kiểm tra xem token có tồn tại và chưa hết hạn không
		if (token != null && !token.isExpired()) {
			return token;
		}

		return null; // Token không hợp lệ hoặc đã hết hạn
	}

	@Override
	public void deleteToken(Token token) {
		tokenRepository.delete(token);
	}

	private String generateToken() {
		// Tạo mã token duy nhất
		// (Ví dụ: sử dụng UUID)
		return UUID.randomUUID().toString();
	}
}
