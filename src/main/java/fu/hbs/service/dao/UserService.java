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
package fu.hbs.service.dao;

import fu.hbs.dto.UserDto;
import fu.hbs.entities.User;
import fu.hbs.exceptionHandler.UserIvalidException;
import fu.hbs.exceptionHandler.UserNotFoundException;

public interface UserService {

	User save(UserDto userDto);

	Boolean checkPasswordUser(String email, String password);

	Boolean checkUserbyEmail(String email);

	User getUserbyEmail(String email);

	User findById(Long id) throws UserNotFoundException;

	User update(User user) throws UserIvalidException;

	boolean findByPhone(String phone) throws UserNotFoundException;

	User save(User user);

}
