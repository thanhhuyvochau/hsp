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
package com.example.service.dao;

import com.example.entities.Token;
import com.example.entities.User;

public interface TokenService {
	Token createToken(User user);

	Token findTokenByValue(String tokenValue);

	void deleteToken(Long id);
}
