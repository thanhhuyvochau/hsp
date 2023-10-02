//package com.example.services.dao.impl;
//
//import org.springframework.stereotype.Service;
//
//import com.example.dto.UserDto;
//import com.example.entities.User;
//import com.example.repositoties.UserRepository;
//import com.example.services.dao.UserService;
//
//@Service("userService")
//public class UserServiceImpl implements UserService {
//
//	private UserRepository userRepository;
//
//	public UserServiceImpl(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
//
//	@Override
//	public User findByEmail(String email) {
//		return userRepository.findByEmail(email);
//	}
//
//	@Override
//	public User save(UserDto userDto) {
//		User user = new User(userDto.getUserName(), userDto.getUserDob(), userDto.getUserEmail(),
//				userDto.getUserPassword(), userDto.getUserAddress(), userDto.getUserPhone());
//
//		return userRepository.save(user);
//	}
//
//}
