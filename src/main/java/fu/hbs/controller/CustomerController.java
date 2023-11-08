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
import java.util.stream.Collectors;

@Controller
public class CustomerController {
    @Value("${app.holidays.tetDuongLich}")
    private String tetDuongLichConfig;
    @Value("${app.holidays.ngayThongNhatDatNuoc}")
    private String ngayThongNhatDatNuocConfig;
    @Value("${app.holidays.ngayQuocTeLaoDong}")
    private String ngayQuocTeLaoDongConfig;
    @Value("${app.holidays.ngayQuocKhanh}")
    private String ngayQuocKhanhConfig;
    @Autowired
    private UserService userService;
    @Autowired
    private HotelBookingService hotelBookingService;
    @Autowired
    private RoomCategoryService roomCategoryService;
    @Autowired
    private CategoryRoomPriceService categoryRoomPriceService;
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

        CreateBookingDTO createBookingDTO = new CreateBookingDTO();
        Map<Long, Integer> roomCategoryMap = new HashMap<>();
        Integer number = (Integer) session.getAttribute("numberOfPeople");
        LocalDate checkIn = (LocalDate) session.getAttribute("defaultDate");
        LocalDate checkOut = (LocalDate) session.getAttribute("defaultDate1");

//        LocalDate inputDate = LocalDate.parse(checkIn);
//        LocalDate inputDate1 = LocalDate.parse(checkOut);

        // Lấy ra các Loại phòng và số phòng còn trống
        if (roomCategoryNames.size() == selectedRoomCategories.size()) {
            for (int i = 0; i < roomCategoryNames.size(); i++) {
                Long category = roomCategoryNames.get(i);
                int roomCount = selectedRoomCategories.get(i);
                if (roomCount > 0) {
                    roomCategoryMap.put(category, roomCount);
                }
            }
        }
        if (roomCategoryMap.isEmpty()) {
            model.addAttribute("error", "Bạn chưa đặt phòng nào");
            return "customer/errorBooking";
        }


        List<Room> rooms = new ArrayList<>();
        List<RoomCategories> roomCategoriesList = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : roomCategoryMap.entrySet()) {
            Long category = entry.getKey();
            rooms = roomService.countRoomAvaliableByCategory(category, checkIn, checkOut);
            roomCategoriesList.add(roomCategoryService.getRoomCategoryId(category));
        }


        for (Map.Entry<Long, Integer> entry : roomCategoryMap.entrySet()) {
            Long category = entry.getKey();
            List<Room> roomsByCategory = roomService.countRoomAvaliableByCategory(category, checkOut, checkOut);
            rooms.addAll(roomsByCategory);
        }

        // Group rooms by room category
        Map<Long, List<Room>> roomMap = rooms.stream().collect(Collectors.groupingBy(Room::getRoomCategoryId));
        for (Map.Entry<Long, List<Room>> entry : roomMap.entrySet()) {
            Long categoryId = entry.getKey();
            List<Room> roomsWithSameCategory = entry.getValue();

        }


        List<CategoryRoomPrice> categoryRoomPrices = new ArrayList<>();

        for (RoomCategories roomCategory : roomCategoriesList) {
            CategoryRoomPrice categoryRoomPrice = categoryRoomPriceService.findByCateRoomPriceId(roomCategory.getRoomCategoryId());
            categoryRoomPrices.add(categoryRoomPrice);
        }

        List<DateInfoCategoryRoomPriceDTO> dateInfoList = processDateInfo(checkOut, checkOut);
        BigDecimal total_Price = BigDecimal.ZERO;
        Map<Long, BigDecimal> totalPriceByCategoryId = new HashMap<>();

        for (CategoryRoomPrice cpr : categoryRoomPrices) {
            BigDecimal totalForCategory = calculateTotalForCategory(cpr, dateInfoList);
            totalPriceByCategoryId.put(cpr.getRoomCategoryId(), totalForCategory);

        }

        // Lặp qua map 1 và kiểm tra xem khóa có tồn tại trong map 2 không
        for (Map.Entry<Long, Integer> entry1 : roomCategoryMap.entrySet()) {
            Long category = entry1.getKey();
            Integer roomCount = entry1.getValue();

            if (totalPriceByCategoryId.containsKey(category)) {
                // Nếu khóa tồn tại trong map 2, lấy giá trị từ cả hai map
                BigDecimal totalPrice1 = totalPriceByCategoryId.get(category);

                BigDecimal totalPrice2 = totalPrice1.multiply(BigDecimal.valueOf(roomCount));

                total_Price = total_Price.add(totalPrice2);

            }
        }


        createBookingDTO.setRoomCategoriesList(roomCategoriesList);
        createBookingDTO.setTotalPrice(total_Price);
        createBookingDTO.setCheckIn(checkIn);
        createBookingDTO.setCheckOut(checkOut);
        createBookingDTO.setRoomCategoryMap(roomCategoryMap);

        model.addAttribute("checkIn", checkIn.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM , yyyy")));
        model.addAttribute("checkOut", checkOut.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM , yyyy")));
        model.addAttribute("createBookingDTO", createBookingDTO);
        model.addAttribute("roomCategoryMap", roomCategoryMap);
        model.addAttribute("categoryRoomPrices", categoryRoomPrices);
        model.addAttribute("totalPriceByCategoryId", totalPriceByCategoryId);
        model.addAttribute("roomMap", roomMap);
        session.setAttribute("createBookingDTO", createBookingDTO);
        return "customer/createBooking";
    }

    @PostMapping("/room/addBooking")
    public String submidOrder(@RequestParam("amount") String orderTotal,
                              @RequestParam("orderInfo") String orderInfo, Model model) {
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
        hotelBooking.setStatus("Chưa check-in");
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
                System.out.println("Số lần đã thêm" + roomsAdded);
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

    // Hàm tính tổng giá cho một CategoryRoomPrice dựa trên dateInfoList
    public BigDecimal calculateTotalForCategory(CategoryRoomPrice
                                                        cpr, List<DateInfoCategoryRoomPriceDTO> dateInfoList) {
        BigDecimal totalForCategory = BigDecimal.ZERO;

        for (int i = 0; i < dateInfoList.size(); i++) {
            BigDecimal multiplier = BigDecimal.ONE; // Mặc định là 1

            switch (dateInfoList.get(i).getDayType()) {
                case 2:
                    multiplier = new BigDecimal("1.5");
                    break;
                case 3:
                    multiplier = new BigDecimal("3");
                    break;
                default:
                    // Mặc định không thay đổi giá
                    break;
            }
            BigDecimal price = cpr.getPrice().multiply(multiplier); // Tính giá tiền cho cpr cụ thể
            totalForCategory = totalForCategory.add(price);
        }
        return totalForCategory;
    }

    @GetMapping("/customer/bookingDetails")
    public String bookingDetails(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserbyEmail(userDetails.getUsername());

        model.addAttribute("user", user);
        return "customer/bookingDetail";
    }


    private List<DateInfoCategoryRoomPriceDTO> processDateInfo(LocalDate startDate, LocalDate endDate) {
        List<DateInfoCategoryRoomPriceDTO> dateInfoList = new ArrayList<>();


        LocalDate startDate1 = startDate;
        LocalDate endDate1 = endDate;

        while (!startDate1.isAfter(endDate1)) {
            int dayType = getDayType(startDate1);

            dateInfoList.add(new DateInfoCategoryRoomPriceDTO(startDate1, dayType));
            startDate1 = startDate1.plusDays(1);
        }


        return dateInfoList;
    }

    public List<LocalDate> getHolidays(int year) {
        List<LocalDate> holidays = new ArrayList<>();

        // Chuyển đổi các ngày lễ từ cấu hình thành LocalDate
        LocalDate tetDuongLich = LocalDate.parse(year + "-" + tetDuongLichConfig);
        LocalDate ngayThongNhatDatNuoc = LocalDate.parse(year + "-" + ngayThongNhatDatNuocConfig);
        LocalDate ngayQuocTeLaoDong = LocalDate.parse(year + "-" + ngayQuocTeLaoDongConfig);
        LocalDate ngayQuocKhanh = LocalDate.parse(year + "-" + ngayQuocKhanhConfig);

        holidays.add(tetDuongLich);
        holidays.add(ngayThongNhatDatNuoc);
        holidays.add(ngayQuocTeLaoDong);
        holidays.add(ngayQuocKhanh);

        return holidays;
    }

    /**
     * Determine the day type (weekday, weekend, or holiday) for a given date.
     *
     * @param startDate The date to determine the day type.
     * @return An integer representing the day type (1: weekday, 2: weekend, 3:
     * holiday).
     */
    private int getDayType(LocalDate startDate) {
        if (getHolidays(startDate.getYear()).contains(startDate)) {
            return 3; // holidays
        }
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();

        if (!(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
            return 1; // weekday
        } else {
            return 2; // weekend
        }
    }
}
