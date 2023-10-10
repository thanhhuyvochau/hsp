/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 04/10/2023    1.0        HieuLBM          First Deploy
 * 
 * 
 */
package com.example.services.dao;

import com.example.dto.UserDto;
import com.example.entities.User;

public interface UserService {

	User save(UserDto userDto);

	Boolean checkPasswordUser(String email, String password);

	Boolean checkUserbyEmail(String email);

	User getUserbyEmail(String email);

}
