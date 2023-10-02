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

	@GetMapping("/register")
	public String registerForm() {
		return "authentication/register";
	}

}
