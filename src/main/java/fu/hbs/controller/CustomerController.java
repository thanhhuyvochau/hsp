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


import fu.hbs.dto.CategoryRoomPriceDTO.DateInfoCategoryRoomPriceDTO;
import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.dto.HotelBookingDTO.CreateBookingDTO;
import fu.hbs.dto.HotelBookingDTO.ViewHotelBookingDTO;
import fu.hbs.entities.*;
import fu.hbs.repository.HotelBookingRepository;
import fu.hbs.service.dao.*;
import fu.hbs.service.dao.RoomService;
import fu.hbs.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        List<ViewHotelBookingDTO> hotelBookings = hotelBookingService.findAllByUserIdAndSameTime(user.getUserId());
        model.addAttribute("hotelBookings", hotelBookings);
        return "customer/bookingHistory";
    }

    @GetMapping("/room/addBooking")
    public String addBooking(
            @RequestParam(name = "RoomCategoryId", required = false) List<Long> roomCategoryNames,
            @RequestParam(name = "selectedRoomCategory", required = false) List<Integer> selectedRoomCategories,

            Model model, HttpSession session) {

        LocalDate checkIn = (LocalDate) session.getAttribute("defaultDate");
        LocalDate checkOut = (LocalDate) session.getAttribute("defaultDate1");

//        LocalDate inputDate = LocalDate.parse(checkIn);
//        LocalDate inputDate1 = LocalDate.parse(checkOut);


        CreateBookingDTO createBookingDTO = hotelBookingService.createBooking(
                roomCategoryNames, selectedRoomCategories, checkIn, checkOut, session);

        model.addAttribute("checkIn", checkIn.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM , yyyy")));
        model.addAttribute("checkOut", checkOut.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM , yyyy")));
        model.addAttribute("createBookingDTO", createBookingDTO);
        session.setAttribute("createBookingDTO", createBookingDTO);

        return "customer/createBooking";
    }

    @PostMapping("/room/addBooking")
    public String submidOrder(@RequestParam("amount") String orderTotal,
                              @RequestParam("orderInfo") String orderInfo, Model model, HttpSession session) {
        model.addAttribute("orderTotal", formatString(orderTotal));
        model.addAttribute("orderInfo", orderInfo);

        return "customer/orderCustomer";
    }


    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model, Authentication authentication, HttpSession session) {
        int paymentStatus = vnPayService.orderReturn(request);
        VnpayTransactions vnpayTransactions = new VnpayTransactions();

        CreateBookingDTO createBookingDTO = (CreateBookingDTO) session.getAttribute("createBookingDTO");
        List<RoomCategories> roomCategories = createBookingDTO.getRoomCategoriesList();
        Map<Long, Integer> roomCategoryMap = createBookingDTO.getRoomCategoryMap();

        List<BookingRoomDetails> bookingRoomDetailsList = new ArrayList<>();


        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserbyEmail(userDetails.getUsername());
        HotelBooking newHotelBooking = null;
        int totalRoom = 0;


        HotelBooking hotelBooking = new HotelBooking();
        hotelBooking.setUserId(user.getUserId());
        hotelBooking.setCheckIn(Date.valueOf(createBookingDTO.getCheckIn()));
        hotelBooking.setCheckOut(Date.valueOf(createBookingDTO.getCheckOut()));
        hotelBooking.setTotalPrice(createBookingDTO.getTotalPrice());
        for (Map.Entry<Long, Integer> entry : roomCategoryMap.entrySet()) {
            Integer value = entry.getValue();
            totalRoom += value;
        }
        hotelBooking.setTotalRoom(totalRoom);
        hotelBooking.setStatusId((Long) 1L);
        newHotelBooking = hotelBookingService.save(hotelBooking);


        for (Map.Entry<Long, Integer> entry : roomCategoryMap.entrySet()) {
            Long categoryId = entry.getKey();
            Integer roomCount = entry.getValue();
            List<Room> rooms = roomService.countRoomAvaliableByCategory(categoryId, newHotelBooking.getCheckIn().toLocalDate(), newHotelBooking.getCheckOut().toLocalDate());

            // Khai báo biến đếm số lần đã thêm phòng
            int roomsAdded = 0;

            for (Room room : rooms) {
                if (roomsAdded < roomCount) {
                    BookingRoomDetails bookingRoomDetails = new BookingRoomDetails();
                    bookingRoomDetails.setRoomId(room.getRoomId());
                    bookingRoomDetails.setHotelBookingId(newHotelBooking.getHotelBookingId());
                    bookingRoomDetails.setRoomCategoryId(categoryId);
                    bookingRoomDetailsService.save(bookingRoomDetails);

                    // Tăng biến đếm số phòng đã thêm
                    roomsAdded++;
                } else {
                    break; // Đã thêm đủ số lượng phòng cần thiết, thoát khỏi vòng lặp
                }

            }
        }


        if (paymentStatus == 1) {


            vnpayTransactions.setTransactionId(transactionId);
            vnpayTransactions.setHotelBookingId(newHotelBooking.getHotelBookingId());
//            vnpayTransactions.setCreatedDate(Date.valueOf(paymentTime));
            vnpayTransactionsService.save(vnpayTransactions);
            return "customer/ordersuccess";
        }
        return "customer/orderfail";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }


    public String formatString(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Remove trailing zeros and decimal point
        String formattedString = input.replaceAll("0*$", "");

        // Remove decimal point if it's the last character
        if (formattedString.endsWith(".")) {
            formattedString = formattedString.substring(0, formattedString.length() - 1);
        }

        return formattedString;
    }


    @GetMapping("/customer/bookingDetails")
    public String bookingDetails(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserbyEmail(userDetails.getUsername());

        model.addAttribute("user", user);
        return "customer/bookingDetail";
    }


    @GetMapping("/room/invoice")
    public String getInvoice() {

        return "customer/invoice";
    }

}
