/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 14/10/2023    1.0        HieuLBM          First Deploy
 * 16/10/2023	 1.1		HieuLBM			 Add accessDenied 			
 */

package fu.hbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitController {

	/**
	 * Displays the home page when the root URL is accessed.
	 *
	 * @return The home page view.
	 */
	@GetMapping("/")
	public String home() {
		return "homepage";
	}

	/**
	 * Handles the access denied scenario and displays an access denied page.
	 *
	 * @return The access denied page view.
	 */
	@GetMapping("/accessDenied")
	public String accessDenied() {
		return "authentication/accessDenied";
	}
	/**
	 * Handles error scenarios and displays a general error page.
	 *
	 * @return The error page view.
	 */
//	@GetMapping("/error")
//	public String error() {
//		return "404";
//	}
	/**
	 * Handles access denied scenarios and displays a custom error page.
	 *
	 * @param model The model to add attributes.
	 * @return The custom error page view.
	 */
//	@GetMapping("/accessDenied")
//	public String accessDenied(Model model) {
//		model.addAttribute("message", "Access Denied");
//		return "404";
//	}
}