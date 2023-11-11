package fu.hbs.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import fu.hbs.dto.HotelBookingDTO.BookingDetailsDTO;
import fu.hbs.dto.HotelBookingDTO.CreateHotelBookingDTO;
import fu.hbs.dto.HotelBookingDTO.CreateHotelBookingDetailDTO;
import fu.hbs.entities.BookingRoomDetails;
import fu.hbs.service.dao.BookingRoomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fu.hbs.entities.HotelBooking;
import fu.hbs.repository.HotelBookingRepository;
import fu.hbs.repository.RoomStatusRepository;
import fu.hbs.service.dao.ReceptionistBookingService;

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

    @GetMapping("/create")
    public String createBooking(Model model) {
        model.addAttribute("booking", new CreateHotelBookingDTO());
        return "booking/create";
    }

    @PostMapping("/save")
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
        hotelBookingDTO.setCheckIn(Date.valueOf("2023-11-15"));
        hotelBookingDTO.setCheckOut(Date.valueOf("2023-11-20"));
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

        hotelBookingDTO.setBookingDetails(bookingDetails);
        bookingService.createHotelBookingByReceptionist(hotelBookingDTO);
        return "redirect:/listBookingReceptionist";
    }

    @GetMapping("/checkout-receptionist")
    public String checkoutReceptionist(@RequestParam("bookingId") Long bookingId, Model model) {
        // Retrieve booking details based on the bookingId
        // Replace the following line with your actual logic to fetch booking details
        HotelBooking hotelBooking = bookingService.findById(bookingId);
        List<BookingRoomDetails> bookingDetailsList = bookingRoomDetailsService.getBookingDetailsByHotelBookingId(hotelBooking.getHotelBookingId());

        // Add booking details to the model
        model.addAttribute("hotelBooking", hotelBooking);
        model.addAttribute("bookingDetailsList", bookingDetailsList);
        // Return the view name for checkout page
        return "receptionist/checkoutReceptionist";
    }

    @PostMapping("/checkout-receptionist")
    public String saveCheckoutReceptionist(@RequestParam("bookingId") Long bookingId, Model model) {
        // Retrieve booking details based on the bookingId
        // Replace the following line with your actual logic to fetch booking details
        HotelBooking hotelBooking = bookingService.findById(bookingId);
        model.getAttribute("hotelBooking");
        List<BookingRoomDetails> bookingDetailsList = bookingRoomDetailsService.getBookingDetailsByHotelBookingId(hotelBooking.getHotelBookingId());

        // Add booking details to the model
        model.addAttribute("hotelBooking", hotelBooking);
        model.addAttribute("bookingDetailsList", bookingDetailsList);
        // Return the view name for checkout page
        return "receptionist/checkoutReceptionist";
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
    public String checkOutReceptionist(Model model) {

        return "receptionist/checkOutReceptionist";
    }

    @GetMapping("receptionist/createRoomReceptionist")
    public String createRoomReceptionist(Model model) {

        return "receptionist/createRoomReceptionist";
    }
}
