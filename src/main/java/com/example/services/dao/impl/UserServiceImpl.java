package com.example.services.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.UserDto;
import com.example.entities.User;
import com.example.repositoties.UserRepository;
import com.example.services.dao.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	// @Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Override
	public User save(UserDto userDto) {

		User user = new User();
		user.setEmail(userDto.getUserEmail());
		user.setName(userDto.getUserName());
		// Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
		String encodedPassword = passwordEncoder.encode(userDto.getUserPassword());
		user.setPassword(encodedPassword);

		return userRepository.save(user);
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
