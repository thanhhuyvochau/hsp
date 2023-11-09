/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 10/11/2023    1.0        HieuLBM          First Deploy
 *
 */

package fu.hbs.controller;

import fu.hbs.dto.HotelBookingDTO.BookingDetailsDTO;
import fu.hbs.dto.HotelBookingDTO.CreateBookingDTO;
import fu.hbs.entities.*;
import fu.hbs.service.dao.*;
import fu.hbs.service.dao.RoomService;
import fu.hbs.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class BookingController {
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
    @Autowired
    CustomerCancellationReasonService customerCancellationReasonService;

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


    @GetMapping("/customer/bookingDetails/{hotelBookingId}")
    public String bookingDetails(Model model, Authentication authentication,
                                 @PathVariable Long hotelBookingId) {
        // lấy tất cả booking đã có
        BookingDetailsDTO bookingDetailsDTO = bookingRoomDetailsService.getBookingDetails(authentication, hotelBookingId);
        model.addAttribute("bookingDetailsDTO", bookingDetailsDTO);
        return "customer/bookingDetail";
    }

    @PostMapping("/customer/cancelBooking")
    public String cancelBooking(@RequestParam("hotelBookingId") Long hotelBookingId,
                                @RequestParam("reason") String reason,
                                @RequestParam("otherReason") String otherReason,
                                @RequestParam("bank") String bank,
                                @RequestParam("account") String account,
                                @RequestParam("userName") String userName) {
        hotelBookingService.cancelBooking(hotelBookingId, reason, otherReason, bank, account, userName);
        return "customer/cancel-booking";
    }

    @GetMapping("/customer/cancelBooking/{hotelBookingId}")
    public String cancelBooking(Model model, Authentication authentication,
                                @PathVariable Long hotelBookingId) {
        BookingDetailsDTO bookingDetailsDTO = bookingRoomDetailsService.getBookingDetails(authentication, hotelBookingId);
        List<customerCancellationReasons> customerCancellationReasons = customerCancellationReasonService.findAll();

        model.addAttribute("bookingDetailsDTO", bookingDetailsDTO);
        model.addAttribute("customerCancellationReasons", customerCancellationReasons);

        return "customer/cancel-booking";
    }
}
