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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


import fu.hbs.dto.RoomCategoryDTO.ViewRoomCategoryDTO;
import fu.hbs.service.dao.RoomCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class InitController {
    @Autowired
    private RoomCategoryService roomCategoryService;

    /**
     * Displays the home page when the root URL is accessed.
     *
     * @return The home page view.
     */
    @GetMapping("/")
    public String home(HttpSession session) {
        List<ViewRoomCategoryDTO> categories = roomCategoryService.getAllRoom();
        session.setAttribute("categories", categories);
        // Lấy ngày hôm nay và định dạng thành chuỗi ngày-tháng-năm
        LocalDate today = LocalDate.now();
        LocalDate nextDay = today.plusDays(1);
        String todayString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nextDayString = nextDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        Đặt giá trị mặc định cho trường ngày
        session.setAttribute("defaultDate", todayString);
        session.setAttribute("defaultDate1", nextDayString);
        return "homepage";
    }

    @RequestMapping("/room/reversion")
    public String redirectToLongURL(HttpSession session) {
        LocalDate checkIn = (LocalDate) session.getAttribute("defaultDate");
        LocalDate checkOut = (LocalDate) session.getAttribute("defaultDate1");
        return "redirect:/room/search?checkIn=" + checkIn + "&checkOut=" + checkOut + "&numberOfPeople=";

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