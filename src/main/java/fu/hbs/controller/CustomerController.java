/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 *  2/11/2023    1.0        HieuLBM          First Deploy
 *
 */


package fu.hbs.controller;


import fu.hbs.dto.HotelBookingDTO.ViewHotelBookingDTO;
import fu.hbs.entities.HotelBooking;
import fu.hbs.entities.User;
import fu.hbs.service.dao.HotelBookingService;
import fu.hbs.service.dao.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private UserService userService;
    @Autowired
    private HotelBookingService hotelBookingService;

    @GetMapping("/customer/viewBooking")
    public String booking(Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserbyEmail(userDetails.getUsername());
        List<ViewHotelBookingDTO> hotelBookings = hotelBookingService.findAllByUserId(user.getUserId());
        model.addAttribute("hotelBookings", hotelBookings);
        return "customer/bookingHistory";
    }

    @GetMapping("/customer/addbooking")
    public String addbooking(Model model) {
        return "customer/create-booking";
    }
}
