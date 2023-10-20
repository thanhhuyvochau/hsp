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
package fu.hbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fu.hbs.dto.UserDto;
import fu.hbs.service.dao.UserService;

@Controller

public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@ModelAttribute("userdto")
	public UserDto userResgistrationDto() {
		return new UserDto();
	}

	@GetMapping("/registration")
	public String registerForm() {
		return "authentication/registration";
	}

	@PostMapping("/registration")
	public String registerUserAccount(@ModelAttribute("userdto") UserDto userDto) {
		if (userService.checkUserbyEmail(userDto.getUserEmail())) {
			return "redirect:/registration?emailexist";
		}
		if (userDto.getUserPassword().equals(userDto.getCheckPass()) == false) {
			return "redirect:/registration?checkpass";
		}
		userDto.setStatus(true);
		//userService.save(userDto);

        // Lưu thông tin người dùng
        userService.save(userDto);


		return "redirect:/registration?success";
	}

}
