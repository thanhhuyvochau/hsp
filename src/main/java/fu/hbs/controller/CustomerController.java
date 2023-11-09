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
import fu.hbs.entities.*;
import fu.hbs.service.dao.*;
import fu.hbs.service.dao.RoomService;
import fu.hbs.service.impl.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;


@Controller
public class CustomerController {
    @Autowired
    private UserService userService;
    @Autowired
    private HotelBookingService hotelBookingService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private VnpayTransactionsService vnpayTransactionsService;
    @Autowired
    BookingRoomDetailsService bookingRoomDetailsService;

    @GetMapping("/customer/viewBooking")
    public String booking(Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserbyEmail(userDetails.getUsername());
        List<ViewHotelBookingDTO> hotelBookings = hotelBookingService.findAllByUserId(user.getUserId());
        model.addAttribute("hotelBookings", hotelBookings);
        return "customer/bookingHistory";
    }


}
