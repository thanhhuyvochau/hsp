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

package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitController {

	@GetMapping("/")
	public String home() {
		return "homepage";
	}

//	@GetMapping("/accessDenied")
//	public String accessDenied() {
//		return "authentication/accessDenied";
//	}
//
//	@GetMapping("/error")
//	public String error() {
//		return "404";
//	}
//
//	@GetMapping("/accessDenied")
//	public String accessDenied(Model model) {
//		model.addAttribute("message", "Access Denied");
//		return "404";
//	}
}