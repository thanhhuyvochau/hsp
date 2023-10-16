/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 14/10/2023    1.0        HieuLBM          First Deploy
 * 16/10/2023    1.1        HieuLBM          Edit Link
 *  * 
 */
package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String loginForm(Authentication action) {
		if (action != null) {
			return "redirect:/homepage";
		}
		return "authentication/login";
	}

	@GetMapping("/homepage")
	public String index(Authentication action, Model model) {
		if (action != null) {
			UserDetails user = (UserDetails) action.getPrincipal();
			for (GrantedAuthority authority : user.getAuthorities()) {
				if (authority.getAuthority().equals("ADMIN")) {
					System.out.println("ADMIN");
					model.addAttribute("accountDetail", user.getUsername());
					return "homepage";
				} else if (authority.getAuthority().equals("Management")) {
					System.out.println("Management");
					model.addAttribute("accountDetail", user);
					return "redirect:homepage";
				} else if (authority.getAuthority().equals("Receptionists")) {
					System.out.println("Receptionists");
					model.addAttribute("accountDetail", user);
					return "redirect:homepage";
				} else if (authority.getAuthority().equals("Housekeeping")) {
					System.out.println("Housekeeping");
					model.addAttribute("accountDetail", user);
					return "redirect:homepage";
				} else if (authority.getAuthority().equals("Accounting")) {
					System.out.println("Accounting");
					model.addAttribute("accountDetail", user);
					return "redirect:homepage";
				} else {
					System.out.println("Customer");
					model.addAttribute("accountDetail", user);
					return "redirect:homepage";
				}
			}

		}
		return "redirect:homepage";
	}
}