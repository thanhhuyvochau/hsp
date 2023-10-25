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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fu.hbs.dto.UserDto;
import fu.hbs.entities.User;
import fu.hbs.entities.UserRole;
import fu.hbs.exceptionHandler.UserIvalidException;
import fu.hbs.exceptionHandler.UserNotFoundException;
import fu.hbs.repositoties.UserRepository;
import fu.hbs.repositoties.UserRoleRepository;
import fu.hbs.service.dao.UserService;

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

	@Override
	public User update(User user) throws UserIvalidException {

		Optional<User> existingUser = userRepository.findByPhone(user.getPhone());
		// Phone đã tồn tại và không thuộc về người dùng hiện tại
		if (existingUser.isPresent() && !existingUser.get().getUserId().equals(user.getUserId())) {
			throw new UserIvalidException("Phone đã tồn tại");

		}
		return userRepository.save(user);
	}

	@Override
	public User findById(Long id) throws UserNotFoundException {
		Optional<User> optional = userRepository.findById(id);

		return optional.orElseThrow(() -> new UserNotFoundException("User " + id + "Không tìm thấy!"));
	}

	@Override
	public boolean findByPhone(String phone) throws UserNotFoundException {
		if (userRepository.findByPhone(phone) != null) {
			return true;
		}
		return false;

	}

}
