package fu.hbs.controller;

import java.util.List;

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
    @GetMapping("/create")
    public String createBooking(Model model) {
        model.addAttribute("booking", new HotelBooking());
        return "booking/create";
    }

    @PostMapping("/save")
    public String saveBooking(@ModelAttribute("booking") HotelBooking booking) {
        bookingService.save(booking);
        return "redirect:/listBookingReceptionist";
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

    
    @GetMapping("receptionist/createRoomReceptionist")
    public String createRoomReceptionist(Model model) {
        
        return "receptionist/createRoomReceptionist";
    }
}
