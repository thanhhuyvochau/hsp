package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String loginForm(Authentication action) {
		if (action != null) {
			return "redirect:/";
		}
		return "authentication/login";
	}

	@GetMapping("/")
	public String index(Authentication action, Model model) {
		if (action != null) {
			User user = (User) action.getPrincipal();
			for (GrantedAuthority authority : user.getAuthorities()) {
				if (authority.getAuthority().equals("Admin")) {
					System.out.println("ok");
					model.addAttribute("accountDetail", user.getUsername());
					return "redirect:homepage";
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