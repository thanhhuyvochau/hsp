package com.example.services.dao.impl;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.UserDto;
import com.example.entities.User;
import com.example.repositoties.UserRepository;
import com.example.services.dao.UserService;

//
import org.springframework.stereotype.Service;
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
@Service("userService")
public class UserServiceImpl implements UserService {

	    @Autowired
	    private UserRepository userRepository;
	    @Autowired
	    //@Qualifier("passwordEncoder")
	    private PasswordEncoder passwordEncoder;

//	    @Override
//	    public User save(UserDto userDto) {
//	    	User newUser = new User();
//	        // Tạo một đối tượng User từ dữ liệu được nhập từ userDto
//	        newUser.setEmail(userDto.getUserEmail());
//	        newUser.setName(userDto.getUserName());
//	        newUser.setPassword(userDto.getUserPassword());
//
//	        return userRepository.save(newUser);
//	    }
	
	    @Override
	    public User save(UserDto userDto) {
	       
	        User user = new User( );
	        user.setEmail(userDto.getUserEmail());
	        user.setName(userDto.getUserName());
	     // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
	        String encodedPassword = passwordEncoder.encode(userDto.getUserPassword());
	        user.setPassword(encodedPassword);
	        //user.setPassword(userDto.getUserPassword());
//	        System.out.println(userDto.getUserPassword());
	        
	        return userRepository.save(user);
	        }
    @Override
    public Boolean checkPasswordUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user.getPassword().equals(password)) return true;
        return false;
    }

    @Override
    public Boolean checkUserbyEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user==null) return false;
        return true;
    }

    @Override
    public User getUserbyEmail(String email) {
        return userRepository.getUserByEmail(email);
    }





}
