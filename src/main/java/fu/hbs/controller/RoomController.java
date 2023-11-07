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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fu.hbs.dto.CategoryRoomPriceDTO.DateInfoCategoryRoomPriceDTO;
import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.dto.RoomCategoryDTO.ViewRoomCategoryDTO;
import fu.hbs.entities.CategoryRoomPrice;
import fu.hbs.service.impl.HotelBookingServiceImpl;
import fu.hbs.utils.StringDealer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fu.hbs.dto.BookRoomByCategory;
import fu.hbs.service.dao.RoomByCategoryService;
import fu.hbs.service.dao.RoomCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoomController {
    public static final Integer DEFAULT_NUMBERPERSON = 1;
    private RoomCategoryService roomCategoryService;

    private RoomByCategoryService roomByCategoryService;


    private HotelBookingServiceImpl hotelBookingService;
    StringDealer stringDealer;


    public RoomController(RoomCategoryService roomCategoryService, RoomByCategoryService roomByCategoryService, HotelBookingServiceImpl hotelBookingService) {
        this.roomCategoryService = roomCategoryService;
        this.roomByCategoryService = roomByCategoryService;
        this.hotelBookingService = hotelBookingService;
        this.stringDealer = new StringDealer();
    }

    /**
     * Handles the request to display all room categories available.
     *
     * @param model The model used to pass data to the view.
     * @return The name of the view to render.
     */
    @GetMapping("/room/all")
    public String getRoomCate(Model model, HttpSession session) {
        List<ViewRoomCategoryDTO> categories = roomCategoryService.getAllRoom();
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

        List<ViewRoomCategoryDTO> distinctCategories = roomCategoryService.getAllRoom().stream()
                .filter(roomCategory -> roomCategory.getRoomCategoryId() != categoryId)
                .collect(Collectors.toList());
        model.addAttribute("distinctCategories", distinctCategories);
        model.addAttribute("bookRoomByCategories", bookRoomByCategories);
        return "room/detailRoomCustomer";

    }


    @GetMapping("/room/search")
    public String searchRooms(
            @RequestParam(value = "checkIn", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkIn,
            @RequestParam(value = "checkOut", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOut,
            @RequestParam(value = "numberOfPeople", required = false) Integer numberOfPeople,
            Model model, HttpSession session
    ) {
        HotelBookingAvailable conflictingBookings;
        LocalDate checkin = stringDealer.convertDateToLocalDate(checkIn);
        LocalDate checkout = stringDealer.convertDateToLocalDate(checkOut);


        // Set default value for numberOfPeople if it's null or invalid
        if (numberOfPeople == null) {
            numberOfPeople = DEFAULT_NUMBERPERSON;
            conflictingBookings = hotelBookingService.findBookingsByDates(checkIn, checkOut, numberOfPeople);
        } else {
            conflictingBookings = hotelBookingService.findBookingsByDates(checkIn, checkOut, numberOfPeople);
        }

        List<DateInfoCategoryRoomPriceDTO> dataList = conflictingBookings.getDateInfoCategoryRoomPriceDTOS();
        int pageSize = 7; // Number of records per page

        List<List<DateInfoCategoryRoomPriceDTO>> pages = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i += pageSize) {
            int end = Math.min(i + pageSize, dataList.size());
            pages.add(dataList.subList(i, end));
        }
        session.setAttribute("defaultDate", checkin);
        session.setAttribute("defaultDate1", checkout);
        model.addAttribute("carouselPages", pages);
        session.setAttribute("numberOfPeople", numberOfPeople);
        model.addAttribute("conflictingBookings", conflictingBookings);

        return "room/searchRoomCustomer";
    }


}
