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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.UserDto;
import com.example.entities.User;
import com.example.entities.UserRole;
import com.example.repositoties.UserRepository;
import com.example.repositoties.UserRoleRepository;
import com.example.service.dao.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userroleRepository;
	@Autowired
	// @Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User save(UserDto userDto) {
	    // Lưu thông tin người dùng
	    User user = new User();
	    user.setEmail(userDto.getUserEmail());
	    user.setName(userDto.getUserName());
	 // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
	    String encodedPassword = passwordEncoder.encode(userDto.getUserPassword());
	    user.setPassword(encodedPassword);
	    user.setStatus(true); 
	    User savedUser = userRepository.save(user);

	    // Tạo UserRole và gán roleId là 2
	    UserRole userRole = new UserRole();
	    userRole.setUserId(savedUser.getUserId());
	    userRole.setRoleId(5L); // 2 là roleId bạn muốn gán
	    userroleRepository.save(userRole);

	    return savedUser;
	}


	@Override
	public Boolean checkPasswordUser(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user.getPassword().equals(password))
			return true;
		return false;
	}

	@Override
	public Boolean checkUserbyEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null)
			return false;
		return true;
	}

	@Override
	public User getUserbyEmail(String email) {
		return userRepository.getUserByEmail(email);
	}
	

}
