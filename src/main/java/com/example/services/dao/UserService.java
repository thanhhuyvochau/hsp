package com.example.services.dao;

import com.example.dto.UserDto;
import com.example.entities.User;

public interface UserService {

//	User findByEmail(String email);

	User save(UserDto userDto);
	Boolean checkPasswordUser(String email, String password);
	Boolean checkUserbyEmail(String email);
	User getUserbyEmail(String email);

//	User save(UserDto userDto, User newUser);

}
