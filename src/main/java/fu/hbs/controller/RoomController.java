/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 26/10/2023    1.0        HieuLBM          First Deploy
 * 27/10/2023	 2.0	    HieuLBM			 update category
 */

package fu.hbs.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.entities.HotelBooking;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomCategories;
import fu.hbs.entities.RoomService;
import fu.hbs.service.impl.HotelBookingImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fu.hbs.dto.BookRoomByCategory;
import fu.hbs.dto.RoomCategoryDTO;
import fu.hbs.service.dao.RoomByCategoryService;
import fu.hbs.service.dao.RoomCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoomController {
    public static final Integer DEFAULT_NUMBERPERSON = 1;
    private RoomCategoryService roomCategoryService;

    private RoomByCategoryService roomByCategoryService;


    private HotelBookingImpl hotelBookingService;

    public RoomController(RoomCategoryService roomCategoryService, RoomByCategoryService roomByCategoryService, HotelBookingImpl hotelBookingService) {
        this.roomCategoryService = roomCategoryService;
        this.roomByCategoryService = roomByCategoryService;
        this.hotelBookingService = hotelBookingService;
    }

    /**
     * Handles the request to display all room categories available.
     *
     * @param model The model used to pass data to the view.
     * @return The name of the view to render.
     */
    @GetMapping("/room/all")
    public String getRoomCate(Model model, HttpSession session) {
        List<RoomCategoryDTO> categories = roomCategoryService.getAllRoom();
        if (categories != null) {
            model.addAttribute("categories", categories);
            session.setAttribute("categories", categories);
            return "room/viewRoomCustomer";
        }
        model.addAttribute("NotFound", "Không có loại phòng mà bạn cần tìm");
        return "room/viewRoomCustomer";

    }

    /**
     * Handles the request to display rooms of a specific category.
     *
     * @param model      The model used to pass data to the view.
     * @param categoryId The ID of the room category.
     * @return The name of the view to render.
     */
    @GetMapping("/room/category/{categoryId}")
    public String getRoomByCategory(Model model, @PathVariable Long categoryId) {

        BookRoomByCategory bookRoomByCategories = roomByCategoryService.getRoom(categoryId);

        List<RoomCategoryDTO> distinctCategories = roomCategoryService.getAllRoom().stream()
                .filter(roomCategory -> roomCategory.getRoomCategoryId() != categoryId)
                .collect(Collectors.toList());
        model.addAttribute("distinctCategories", distinctCategories);
        model.addAttribute("bookRoomByCategories", bookRoomByCategories);
        return "room/detailRoomCustomer";

    }


    @GetMapping("/room/search")

    public String searchRooms(@RequestParam(value = "checkIn", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkIn,
                              @RequestParam(value = "checkOut", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOut,
                              @RequestParam(value = "numberOfPeople", required = false) Integer numberOfPeople,
                              Model model) {
        HotelBookingAvailable conflictingBookings;
//        List<RoomCategories> availableRoomCategories = roomCategoryService.findAvailableRoomCategories(numberOfPeople);
        if (numberOfPeople == null || "".equals(numberOfPeople)) {
            numberOfPeople = DEFAULT_NUMBERPERSON;
            conflictingBookings = hotelBookingService.findBookingsByDates(checkIn, checkOut, numberOfPeople);
        } else {
            conflictingBookings = hotelBookingService.findBookingsByDates(checkIn, checkOut, numberOfPeople);
        }
        // Lấy danh sách đặt phòng trùng với ngày check-in và check-out


//        model.addAttribute("availableRoomCategories", availableRoomCategories);

        model.addAttribute("conflictingBookings", conflictingBookings);
        return "room/searchRoomCustomer";
    }
}
