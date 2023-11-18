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

import com.google.gson.Gson;
import fu.hbs.dto.CancellationFormDTO;
import fu.hbs.dto.HotelBookingDTO.*;
import fu.hbs.entities.*;
import fu.hbs.exceptionHandler.CancellationExistException;
import fu.hbs.exceptionHandler.ResetExceptionHandler;
import fu.hbs.service.dao.*;
import fu.hbs.service.dao.HotelBookingService;
import fu.hbs.service.dao.RoomService;
import fu.hbs.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private TransactionsService transactionsService;
    @Autowired
    BookingRoomDetailsService bookingRoomDetailsService;
    @Autowired
    CustomerCancellationReasonService customerCancellationReasonService;
    @Autowired
    BankListService bankListService;

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


    @PostMapping(value = "/room/addBooking", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitOrder(@RequestBody GuestBookingDTO guestBookingDTO, Model model, HttpSession session) {
        System.out.println(guestBookingDTO);

        BigDecimal doubleValue = guestBookingDTO.getPaymentAmount();
        System.out.println(doubleValue);
        session.setAttribute("orderTotal", doubleValue);
        session.setAttribute("orderInfo", guestBookingDTO.getOrderInfo());
        session.setAttribute("guestBookingDTO", guestBookingDTO);

        return ResponseEntity.ok("Gửi thành công.");
    }

    @GetMapping("/room/invoice")
    public String showInvoice(Model model) {
        return "redirect:/submitOrder";
    }

    @GetMapping("/submitOrder")
    public String submitdOrder(HttpSession session, HttpServletRequest request) {

        BigDecimal orderTotal = (BigDecimal) session.getAttribute("orderTotal");
        String orderInfo = (String) session.getAttribute("orderInfo");
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model, Authentication authentication, HttpSession session) throws ResetExceptionHandler {
        int paymentStatus = vnPayService.orderReturn(request);
        Transactions vnpayTransactions = new Transactions();


        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        BigDecimal bigDecimalValue = new BigDecimal(totalPrice).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);


        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


        // Parse the string into a LocalDateTime object
        LocalDateTime parsedDate = LocalDateTime.parse(paymentTime, formatter);
        if (paymentStatus == 1) {

            Long hotelBookingId = hotelBookingService.saveRoomAfterBooking(authentication, session, bigDecimalValue);

            vnpayTransactions.setVnpayTransactionId(transactionId);
            vnpayTransactions.setHotelBookingId(hotelBookingId);
            vnpayTransactions.setAmount(bigDecimalValue);
            vnpayTransactions.setCreatedDate(Instant.from(parsedDate));
            vnpayTransactions.setStatus("Thành công");
            vnpayTransactions.setPaymentId(1L);
            transactionsService.save(vnpayTransactions);


            return "customer/ordersuccess";
        }
        return "customer/orderfail";
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
    public ResponseEntity<?> cancelBooking(@RequestBody CancellationFormDTO cancellationForm, Model model, Authentication authentication) throws CancellationExistException {
        try {
            hotelBookingService.cancelBooking(cancellationForm, authentication);
            return ResponseEntity.ok("Gửi yêu cầu thành công");
        } catch (CancellationExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bạn đã gửi rồi.");

        }

    }


    @GetMapping("/customer/cancelBooking/{hotelBookingId}")
    public String cancelBooking(Model model, Authentication authentication,
                                @PathVariable Long hotelBookingId, HttpSession session, RedirectAttributes redirectAttributes) {
        BookingDetailsDTO bookingDetailsDTO = bookingRoomDetailsService.getBookingDetails(authentication, hotelBookingId);
        List<CustomerCancellationReasons> customerCancellationReasons = customerCancellationReasonService.findAll();
        List<BankList> bankLists = bankListService.getAll();
        model.addAttribute("bookingDetailsDTO", bookingDetailsDTO);
        model.addAttribute("customerCancellationReasons", customerCancellationReasons);
        model.addAttribute("bankLists", bankLists);
        model.addAttribute("hotelBookingId", hotelBookingId);


        session.setAttribute("bookingDetailsDTO", bookingDetailsDTO);
        return "customer/cancelBooking";
    }

    @PostMapping("/search-room")
    public ResponseEntity<String> searchRoomForBooking(@RequestBody SearchingRoomDTO searchingRoomDTO) {
        List<SearchingResultRoomDTO> searchingRoomForBooking = this.roomService.getSearchingRoomForBooking(searchingRoomDTO.getCategoryId(), searchingRoomDTO.getCheckIn(), searchingRoomDTO.getCheckOut());
        Gson gson = new Gson();
        String json = gson.toJson(searchingRoomForBooking);
        return ResponseEntity.ok(json);
    }
}
