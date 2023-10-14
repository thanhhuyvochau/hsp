
package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitController {

	@GetMapping("/homepage")
	public String home() {
		return "homepage";
	}
//
//	@GetMapping("/accessDenied")
//	public String accessDenied() {
//		return "authentication/accessDenied";
//	}

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