package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.dto.UserDto;
import com.example.services.dao.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {
	private UserService userService;

	@ModelAttribute("userdto")
	public UserDto userResgistrationDto() {
		return new UserDto();
	}

	@GetMapping("/login")
	public String loginForm() {
		return "authentication/login";
	}

	@GetMapping("/accessDenied")
	public String accessDenied() {
		return "authentication/accessDenied";
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
		userService.save(userDto);
		return "redirect:/registration?success";
	}

}
