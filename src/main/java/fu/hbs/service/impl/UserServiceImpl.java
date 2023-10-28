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
import fu.hbs.repository.UserRepository;
import fu.hbs.repository.UserRoleRepository;
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

	/**
	 * Create and save a new user based on the provided UserDto object.
	 *
	 * @param userDto the user information to be saved
	 * @return the saved user object
	 */
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
		userRole.setRoleId(6L); // 2 là roleId bạn muốn gán
		userroleRepository.save(userRole);

		return savedUser;
	}

	/**
	 * Check if the provided password matches the user's password for the given
	 * email.
	 *
	 * @param email    the email address of the user
	 * @param password the password to be checked
	 * @return true if the password matches, false otherwise
	 */
	@Override
	public Boolean checkPasswordUser(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user.getPassword().equals(password))
			return true;
		return false;
	}

	/**
	 * Check if a user with the provided email exists.
	 *
	 * @param email the email address to be checked
	 * @return true if a user with the email exists, false otherwise
	 */
	@Override
	public Boolean checkUserbyEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null)
			return false;
		return true;
	}

	/**
	 * Get a user by their email address.
	 *
	 * @param email the email address of the user to retrieve
	 * @return the user with the specified email
	 */
	@Override
	public User getUserbyEmail(String email) {
		return userRepository.getUserByEmail(email);
	}

	/**
	 * Update the user information.
	 *
	 * @param user the updated user information
	 * @return the updated user
	 * @throws UserIvalidException if there is an issue with the update
	 */
	@Override
	public User update(User user) throws UserIvalidException {

		Optional<User> existingUser = userRepository.findByPhone(user.getPhone());
		// Phone đã tồn tại và không thuộc về người dùng hiện tại
		if (existingUser.isPresent() && !existingUser.get().getUserId().equals(user.getUserId())) {
			throw new UserIvalidException("Phone đã tồn tại");

		}
		return userRepository.save(user);
	}

	/**
	 * Mark: Phone number already exists.
	 *
	 * @param id the ID of the user to be updated
	 * @return the updated user
	 * @throws UserIvalidException if there is an issue with the update
	 */
	@Override
	public User findById(Long id) throws UserNotFoundException {
		Optional<User> optional = userRepository.findById(id);

		return optional.orElseThrow(() -> new UserNotFoundException("User " + id + "Không tìm thấy!"));
	}

	/**
	 * Check if a user with the provided phone number exists.
	 *
	 * @param phone the phone number to be checked
	 * @return true if a user with the phone number exists, false otherwise
	 */
	@Override
	public boolean findByPhone(String phone) throws UserNotFoundException {
		if (userRepository.findByPhone(phone) != null) {
			return true;
		}
		return false;

	}

	/**
	 * Save the user information.
	 *
	 * @param user the user information to be saved
	 * @return the saved user
	 */
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

}
