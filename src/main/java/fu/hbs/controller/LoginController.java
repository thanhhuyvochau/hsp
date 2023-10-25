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
package fu.hbs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fu.hbs.entities.User;
import fu.hbs.service.dao.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String loginForm(Authentication action) {
		if (action != null) {
			return "redirect:/homepage";
		}
		return "authentication/login";
	}

	@GetMapping("/homepage")
	public String index(Authentication action, Model model, HttpSession session) {
		if (action != null) {
			UserDetails user = (UserDetails) action.getPrincipal();
			System.out.println(user);
			for (GrantedAuthority authority : user.getAuthorities()) {
				if (authority.getAuthority().equalsIgnoreCase("ADMIN")) {
					System.out.println("ADMIN");
					model.addAttribute("accountDetail", user.getUsername());
					return "homepage";
				}
				if (authority.getAuthority().equalsIgnoreCase("MANAGEMENT")) {
					System.out.println("Management");
					model.addAttribute("accountDetail", user);
					return "homepage";
				}
				if (authority.getAuthority().equalsIgnoreCase("RECEPTIONISTS")) {
					System.out.println("Receptionists");
					model.addAttribute("accountDetail", user);
					return "homepage";
				}
				if (authority.getAuthority().equalsIgnoreCase("HOUSEKEEPING")) {
					System.out.println("Housekeeping");
					model.addAttribute("accountDetail", user);
					return "homepage";
				}
				if (authority.getAuthority().equalsIgnoreCase("ACCOUNTING")) {
					System.out.println("Accounting");
					model.addAttribute("accountDetail", user);
					return "homepage";
				}
				if (authority.getAuthority().equalsIgnoreCase("CUSTOMER")) { 
					System.out.println("Customer");
					User user1 = userService.getUserbyEmail(user.getUsername());
					session.setAttribute("accountDetail", user);
//					model.addAttribute("accountDetail", user);
					model.addAttribute("name", user1.getName());
					return "homepage";
				}
			}
 
		}
		return "redirect:homepage";
	}
}