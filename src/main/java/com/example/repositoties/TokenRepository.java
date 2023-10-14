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
package com.example.repositoties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Token;

@Repository("tokenRepository")
public interface TokenRepository extends JpaRepository<Token, Long> {
	Token findByToken(String token);

	Token findByUserEmail(String email);
}
