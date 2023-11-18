package fu.hbs.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import fu.hbs.dto.HotelBookingDTO.*;
import fu.hbs.dto.RoomCategoryDTO.ViewRoomCategoryDTO;
import fu.hbs.entities.*;
import fu.hbs.entities.RoomService;
import fu.hbs.service.dao.*;
import fu.hbs.service.dao.HotelBookingService;
import fu.hbs.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/create")
    public String createBooking(Model model) {
        model.addAttribute("booking", new CreateHotelBookingDTO());
        return "booking/create";
    }

    @PostMapping("/receptionist-save-booking")
    public String saveBooking(@ModelAttribute("booking") CreateHotelBookingDTO bookingRequest) {
        bookingService.createHotelBookingByReceptionist(bookingRequest);
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
//        hotelBookingDTO.setCheckIn(Date.valueOf("2023-11-15"));
//        hotelBookingDTO.setCheckOut(Date.valueOf("2023-11-20"));
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

//    @GetMapping("/checkout-receptionist")
//    public String checkoutReceptionist(@RequestParam("bookingId") Long bookingId, Model model) {
//        // Retrieve booking details based on the bookingId
//        // Replace the following line with your actual logic to fetch booking details
//        HotelBooking hotelBooking = bookingService.findById(bookingId);
//        List<BookingRoomDetails> bookingDetailsList = bookingRoomDetailsService.getBookingDetailsByHotelBookingId(hotelBooking.getHotelBookingId());
//        List<RoomService> roomServiceList = roomServiceService.getAllServices();
//        List<PaymentType> paymentTypes = paymentTypeService.getAllPaymentType();
////        List<RoomBookingServiceDTO> usageRoomServices = hotelBookingService.getAllUsedRoomServices(bookingId);
//        ViewCheckoutDTO checkoutDTO = new ViewCheckoutDTO();
//        // Add booking details to the model
//        model.addAttribute("hotelBooking", hotelBooking);
//        model.addAttribute("bookingDetailsList", bookingDetailsList);
//        model.addAttribute("roomServiceList", roomServiceList);
//        model.addAttribute("checkoutDTO", checkoutDTO);
//        model.addAttribute("paymentTypes", paymentTypes)
//        ;//        model.addAttribute("usageRoomServices", usageRoomServices);
//        // Return the view name for checkout page
//        return "receptionist/checkoutReceptionist";
//    }

//    @PostMapping("/checkout-payment")
//    public String checkoutPaymentReceptionist(@ModelAttribute("checkoutDTO") ViewCheckoutDTO checkoutDTO, Model model, HttpServletRequest request) {
//        // Retrieve booking details based on the bookingId
//        // Replace the following line with your actual logic to fetch booking details
//        HotelBooking hotelBooking = bookingService.findById(checkoutDTO.getHotelBookingId());
//        List<BookingRoomDetails> bookingDetailsList = bookingRoomDetailsService.getBookingDetailsByHotelBookingId(hotelBooking.getHotelBookingId());
//        // Sử lý thanh toán luôn
//        if (checkoutDTO.getPaymentTypeId() == 1) {
//
//
//        } else {
//
//        }
//        // Add booking details to the model
//        model.addAttribute("hotelBooking", hotelBooking);
//        model.addAttribute("bookingDetailsList", bookingDetailsList);
//
////        BigDecimal totalPrice = checkoutDTO.getTotalPrice();
//        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
////        String vnpayUrl = vnPayService.createOrder(totalPrice, hotelBooking.getHotelBookingId(), baseUrl);
//        return "redirect:";
//    }

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

    @PostMapping("/checkIn")
    public ResponseEntity<String> checkIn(@RequestParam("hotelBookingId") Long hotelBookingId) {
        // Thay đổi trạng thái đặt phòng thành "Đã Check In"
        // ...
        //long newStatusId = 2L; // Đã Check In
        // Cập nhật trạng thái đặt phòng trong dữ liệu
        // Ví dụ: Sử dụng một service để cập nhật trạng thái đặt phòng
        hotelBookingRepository.updateStatus(2L, hotelBookingId);

        return new ResponseEntity<>("Check In thành công", HttpStatus.OK);
    }

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
            checkoutModel.setHotelBookingId(hotelBookingId);
            checkoutModel.setServicePrice(viewCheckoutDTO.getTotalServicePrice());
            model.addAttribute("saveCheckoutDTO", checkoutModel);
        } else {
            return "errorPage";
        }
        return "receptionist/checkOutReceptionist";
    }


    @PostMapping("receptionist/checkOutReceptionist")
    public String saveCheckOutReceptionist(@ModelAttribute("saveCheckoutDTO") SaveCheckoutDTO checkoutDTO) {
        bookingService.checkout(checkoutDTO);
        return "redirect:/receptionist/checkOutReceptionist";
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
}
