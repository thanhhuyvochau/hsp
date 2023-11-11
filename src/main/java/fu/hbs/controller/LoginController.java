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


import fu.hbs.dto.RoomCategoryDTO.ViewRoomCategoryDTO;
import fu.hbs.service.dao.RoomCategoryService;
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

import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoomCategoryService roomCategoryService;

    /**
     * Displays the login form if the user is not already authenticated.
     *
     * @param action The authentication object.
     * @return The login form view.
     */
    @GetMapping("/login")
    public String loginForm(Authentication action) {
        if (action != null) {

            return "redirect:/homepage";
        }
        return "authentication/login";
    }
//    public String

    /**
     * Handles the rendering of the homepage based on the user's role.
     *
     * @param authentication The authentication object.
     * @param model          The model to add attributes.
     * @param session        The HTTP session for storing user-related attributes.
     * @return The view of the homepage based on the user's role.
     */
    @GetMapping("/homepage")
    public String homepage(Authentication authentication, Model model, HttpSession session) {
        if (authentication != null) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            System.out.println(user);

            for (GrantedAuthority authority : user.getAuthorities()) {
                String authorityName = authority.getAuthority();

                if (authorityName.equalsIgnoreCase("ADMIN")) {
                    System.out.println("ADMIN");
                    model.addAttribute("accountDetail", user.getUsername());
                    return "homepage";
                } else if (authorityName.equalsIgnoreCase("MANAGEMENT")) {
                    System.out.println("Management");
                    User user1 = userService.getUserbyEmail(user.getUsername());
                    session.setAttribute("accountDetail", user.getUsername());
                    session.setAttribute("name", user1.getName());
                    return "redirect:/management/listRefund";
                } else if (authorityName.equalsIgnoreCase("RECEPTIONISTS")) {
                    System.out.println("Receptionists");
                    model.addAttribute("accountDetail", user.getUsername());
                    return "receptionistsHomePage";
                } else if (authorityName.equalsIgnoreCase("HOUSEKEEPING")) {
                    System.out.println("Housekeeping");
                    model.addAttribute("accountDetail", user.getUsername());
                    return "housekeepingHomePage";
                } else if (authorityName.equalsIgnoreCase("ACCOUNTING")) {
                    System.out.println("Accounting");
                    model.addAttribute("accountDetail", user.getUsername());
                    return "accountingHomePage";
                } else if (authorityName.equalsIgnoreCase("CUSTOMER")) {
                    User user1 = userService.getUserbyEmail(user.getUsername());
                    session.setAttribute("accountDetail", user.getUsername());
                    session.setAttribute("name", user1.getName());
                    return "homepage";
                }
            }
        }
        return "redirect:/login";
    }
}