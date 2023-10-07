package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.services.dao.UserService;

@Controller
public class UserController {
	private UserService userService;

	@GetMapping("/login")
	public String loginForm() {
		return "authentication/login";
	}

	@GetMapping("/user")
	public String index() {
		return "user-index";
	}

	@GetMapping("/accessDenied")
	public String accessDenied() {
		return "accessDenied";
	}

	@GetMapping("/registration")
	public String registerForm() {
		return "authentication/registration";
	}

}
