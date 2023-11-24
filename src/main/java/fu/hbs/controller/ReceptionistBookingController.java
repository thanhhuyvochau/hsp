package fu.hbs.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import fu.hbs.dto.HotelBookingDTO.*;
import fu.hbs.dto.RoomCategoryDTO.ViewRoomCategoryDTO;
import fu.hbs.dto.RoomServiceDTO.RoomBookingServiceDTO;
import fu.hbs.entities.*;
import fu.hbs.entities.RoomService;
import fu.hbs.exceptionHandler.ResetExceptionHandler;
import fu.hbs.service.dao.*;
import fu.hbs.service.dao.HotelBookingService;
import fu.hbs.service.impl.VNPayService;
import fu.hbs.utils.EmailUtil;
import fu.hbs.utils.StringDealer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fu.hbs.repository.HotelBookingRepository;
import fu.hbs.repository.RoomStatusRepository;

@Controller
public class ReceptionistBookingController {
    @Autowired
    private HotelBookingRepository hotelBookingRepository;

    @Autowired
    private ReceptionistBookingService bookingService;

    @Autowired
    private RoomStatusRepository roomStatusRepository;
    @Autowired
    private BookingRoomDetailsService bookingRoomDetailsService;
    @Autowired
    private HotelBookingService hotelBookingService;
    @Autowired
    private ServiceService roomServiceService;

    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private RoomCategoryService roomCategoryService;
    @Autowired
    private HotelBookingServiceService hotelBookingServiceService;
    @Autowired
    private CategoryRoomPriceService categoryRoomPriceService;
    @Autowired
    private TransactionsService transactionsService;

    @GetMapping("/create")
    public String createBooking(Model model) {
        model.addAttribute("booking", new CreateHotelBookingDTO());
        return "booking/create";
    }

    @PostMapping("/receptionist-save-booking")
    public String saveBooking(@ModelAttribute("booking") CreateHotelBookingDTO bookingRequest, HttpSession session) {
        Long hotelBookingId = bookingService.createHotelBookingByReceptionist(bookingRequest);
        if (bookingRequest.getPaymentTypeId() == 1L) {
            HotelBooking hotelBooking = bookingService.findById(hotelBookingId);
            if (hotelBooking == null) {
                return "errorPage";
            }
            BigDecimal totalPriceOfHotelBooking = bookingService.getTotalPriceOfHotelBooking(hotelBookingId);
            session.setAttribute("userEmail", hotelBooking.getEmail().trim());
            session.setAttribute("orderTotal", totalPriceOfHotelBooking.multiply(BigDecimal.valueOf(0.5)).setScale(0, RoundingMode.HALF_DOWN));
            session.setAttribute("orderInfo", "Thanh toán tiền cọc phòng cho đơn đặt:" + hotelBooking.getHotelBookingId());
            return "redirect:/payment";
        }
        return "redirect:/listBookingReceptionist";
    }

    @GetMapping("/test-save")
    public String testSaveBooking() {
        CreateHotelBookingDTO hotelBookingDTO = new CreateHotelBookingDTO();
        hotelBookingDTO.setStatusId(1L);
        hotelBookingDTO.setName("John Doe");
        hotelBookingDTO.setEmail("john.doe@example.com");
        hotelBookingDTO.setAddress("123 Main Street");
        hotelBookingDTO.setPhone("123-456-7890");
//        hotelBookingDTO.setCheckIn(Instant.ofEpochSecond(LocalDate.of(2023, 11, 25).toEpochSecond(LocalTime.of(12, 0), ZoneOffset.UTC)));
//        hotelBookingDTO.setCheckOut(Instant.ofEpochSecond(LocalDate.of(2023, 11, 28).toEpochSecond(LocalTime.of(12, 0), ZoneOffset.UTC)));
        // Create sample data for CreateHotelBookingDetailDTO
        CreateHotelBookingDetailDTO bookingDetailDTO1 = new CreateHotelBookingDetailDTO();
        bookingDetailDTO1.setRoomCategoryId(1L);
        bookingDetailDTO1.setRoomNumber(1);

        CreateHotelBookingDetailDTO bookingDetailDTO2 = new CreateHotelBookingDetailDTO();
        bookingDetailDTO2.setRoomCategoryId(1L);
        bookingDetailDTO2.setRoomNumber(2);

        // Add booking details to the list in CreateHotelBookingDTO
        List<CreateHotelBookingDetailDTO> bookingDetails = new ArrayList<>();
        bookingDetails.add(bookingDetailDTO1);
        bookingDetails.add(bookingDetailDTO2);

//        hotelBookingDTO.setBookingDetails(bookingDetails);
        bookingService.createHotelBookingByReceptionist(hotelBookingDTO);
        return "redirect:/listBookingReceptionist";
    }

    @PostMapping("/handle-payment")
    public String saveCheckoutPaymentReceptionist(@ModelAttribute("checkoutDTO") ViewCheckoutDTO checkoutDTO, Model model) {
        // Retrieve booking details based on the bookingId
        // Replace the following line with your actual logic to fetch booking details
        HotelBooking hotelBooking = bookingService.findById(checkoutDTO.getHotelBookingId());
        List<BookingRoomDetails> bookingDetailsList = bookingRoomDetailsService.getBookingDetailsByHotelBookingId(hotelBooking.getHotelBookingId());

        // Add booking details to the model
        model.addAttribute("hotelBooking", hotelBooking);
        model.addAttribute("bookingDetailsList", bookingDetailsList);
        // Return the view name for checkout page
        return "customer/orderCustomer";
    }

    @GetMapping("/receptionist/listBookingReceptionist")
    public String listBooking(Model model) {
        // Sử dụng service để lấy danh sách đặt trước
        List<HotelBooking> allBookings = bookingService.findAllWithStatusOne();
        model.addAttribute("bookings", allBookings);
        return "receptionist/viewBookingReceptionist";
    }

    @GetMapping("/edit/{id}")
    public String editBooking(@PathVariable("id") Long id, Model model) {
        HotelBooking booking = bookingService.findById(id);
        model.addAttribute("booking", booking);
        return "booking/edit";
    }

    @PostMapping("/update")
    public String updateBooking(@ModelAttribute("booking") HotelBooking booking) {
        bookingService.save(booking);
        return "redirect:/listBookingReceptionist";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") Long id) {
        bookingService.deleteById(id);
        return "redirect:/listBookingReceptionist";
    }

    @GetMapping("/getBookingDetails")
    public ResponseEntity<HotelBooking> getBookingDetails(@RequestParam("bookingId") Long bookingId) {
        HotelBooking booking = bookingService.findById(bookingId);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

//    @PostMapping("/checkIn")
//    public ResponseEntity<String> checkIn(@RequestParam("hotelBookingId") Long hotelBookingId) {
//        // Thay đổi trạng thái đặt phòng thành "Đã Check In"
//        // ...
//        //long newStatusId = 2L; // Đã Check In
//        // Cập nhật trạng thái đặt phòng trong dữ liệu
//        // Ví dụ: Sử dụng một service để cập nhật trạng thái đặt phòng
//        hotelBookingRepository.updateStatus(2L, hotelBookingId);
//
//        return new ResponseEntity<>("Check In thành công", HttpStatus.OK);
//    }

    @GetMapping("/receptionist/listRoomInUse")
    public String listRoomInUse(Model model) {
        // Sử dụng service để lấy danh sách đặt trước
        List<HotelBooking> allBookings = bookingService.findAllWithStatusTwo();
        model.addAttribute("bookings", allBookings);
        return "receptionist/listRoomInUseReceptionist";
    }

    @GetMapping("/receptionist/listHistoryBooking")
    public String listHistoryBooking(Model model) {
        // Sử dụng service để lấy danh sách đặt trước
        List<HotelBooking> allBookings = bookingService.findAll();
        model.addAttribute("bookings", allBookings);
        return "receptionist/bookingHistoryReceptionist";
    }

    @GetMapping("receptionist/checkOutReceptionist")
    public String checkOutReceptionist(@RequestParam("hotelBookingId") Long hotelBookingId, Model model) {
        HotelBooking hotelBooking = hotelBookingService.findById(hotelBookingId);
        if (hotelBooking != null) {
            List<BookingRoomDetails> bookingRoomDetails = bookingRoomDetailsService.getBookingDetailsByHotelBookingId(hotelBookingId);
            List<RoomService> roomServices = roomServiceService.getAllServices();
            PaymentType paymentType = paymentTypeService.getPaymentTypeById(2L);
            List<fu.hbs.entities.HotelBookingService> usedServices = hotelBookingServiceService.findAllByHotelBookingId(hotelBookingId);

            Map<Long, RoomCategories> roomCategoriesMap = this.roomCategoryService.getAllRoomCategories().stream().collect(Collectors.toMap(RoomCategories::getRoomCategoryId, Function.identity()));
            ViewCheckoutDTO viewCheckoutDTO = ViewCheckoutDTO.valueOf(hotelBooking, bookingRoomDetails, paymentType, roomCategoriesMap);
            model.addAttribute("viewCheckoutDTO", viewCheckoutDTO);
            model.addAttribute("roomServices", roomServices);
            model.addAttribute("currentTime", Date.valueOf(LocalDate.now()));
            SaveCheckoutDTO checkoutModel = new SaveCheckoutDTO();
            List<SaveCheckoutHotelServiceDTO> hotelServices = checkoutModel.getHotelServices();
            for (RoomBookingServiceDTO roomBookingServiceDTO : viewCheckoutDTO.getRoomBookingServiceDTOS()) {
                SaveCheckoutHotelServiceDTO saveCheckoutHotelServiceDTO = new SaveCheckoutHotelServiceDTO();
                saveCheckoutHotelServiceDTO.setServiceId(roomBookingServiceDTO.getServiceId());
                saveCheckoutHotelServiceDTO.setQuantity(1);
                hotelServices.add(saveCheckoutHotelServiceDTO);

            }
            checkoutModel.setHotelBookingId(hotelBookingId);
            checkoutModel.setServicePrice(viewCheckoutDTO.getTotalServicePrice());
            model.addAttribute("saveCheckoutDTO", checkoutModel);
        } else {
            return "errorPage";
        }
        return "receptionist/checkOutReceptionist";
    }


    @PostMapping("receptionist/checkOutReceptionist")
    public String saveCheckOutReceptionist(@ModelAttribute("saveCheckoutDTO") SaveCheckoutDTO checkoutDTO, HttpSession session) {
        bookingService.checkout(checkoutDTO);
        if (checkoutDTO.getPaymentTypeId() == 1) {
            HotelBooking hotelBooking = bookingService.findById(checkoutDTO.getHotelBookingId());
            if (hotelBooking == null) {
                return "errorPage";
            }
            BigDecimal totalPrice = hotelBooking.getTotalPrice();
            session.setAttribute("userEmail", hotelBooking.getEmail().trim());
            session.setAttribute("orderTotal", totalPrice);
            session.setAttribute("orderInfo", "Thanh toán tiền phòng cho đơn đặt phòng:" + hotelBooking.getHotelBookingId());
            return "redirect:/payment";
        }
        return "redirect:/receptionist/checkOutReceptionist";
    }

    @GetMapping("/payment")
    public String payment(HttpSession session, HttpServletRequest request) {
        BigDecimal orderTotal = (BigDecimal) session.getAttribute("orderTotal");
        String orderInfo = (String) session.getAttribute("orderInfo");
        String userEmail = (String) session.getAttribute("userEmail");
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl, "/receptionist-payment");
        EmailUtil.sendBookingEmail(userEmail, orderInfo, vnpayUrl);
        return "redirect:/receptionist/checkOutReceptionist?hotelBookingId=10";
    }

    @GetMapping("/receptionist-payment")
    public String GetMapping(HttpServletRequest request, Model model) throws ResetExceptionHandler {
        int paymentStatus = vnPayService.orderReturn(request);
        Transactions vnpayTransactions = new Transactions();


        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String hotelBookingIdAsString = StringDealer.extractNumberFromString(orderInfo);
        Long hotelBookingId;
        if (!hotelBookingIdAsString.isEmpty()) {
            hotelBookingId = Long.valueOf(hotelBookingIdAsString);
        } else {
            return "errorPage";
        }
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

    @GetMapping("receptionist/createRoomReceptionist")
    public String createRoomReceptionist(Model model) {
        List<ViewRoomCategoryDTO> categories = roomCategoryService.getAllRoom();
        CreateHotelBookingDTO attributeValue = new CreateHotelBookingDTO();
        attributeValue.setBookingDetails(new ArrayList<>());
        model.addAttribute("booking", attributeValue);
        model.addAttribute("categories", categories);
        model.addAttribute("searchingModel", new SearchingRoomDTO());
        return "receptionist/createRoomReceptionist";
    }

    @PostMapping("/checkIn")
    public ResponseEntity<String> saveCheckIn(@RequestParam("hotelBookingId") Long hotelBookingId) {
        Boolean result = this.bookingService.checkIn(hotelBookingId);
        // Return to Page you want
        if (result) {
            return new ResponseEntity<>("Check In thành công", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Check In thất bại", HttpStatus.BAD_REQUEST);
        }
    }
}
