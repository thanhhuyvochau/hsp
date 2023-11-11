package fu.hbs.controller;

import fu.hbs.dto.CancellationDTO.ViewCancellationDTO;
import fu.hbs.dto.HotelBookingDTO.BookingDetailsDTO;
import fu.hbs.service.dao.BookingRoomDetailsService;
import fu.hbs.service.dao.CustomerCancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class SaleManagerController {
    @Autowired
    CustomerCancellationService customerCancellationService;
    @Autowired
    BookingRoomDetailsService bookingRoomDetailsService;

    @GetMapping("/management/listRefund")
    public String getAllRefund(Model model) {
        List<ViewCancellationDTO> viewCancellationDTOList = customerCancellationService.getAll();
        System.out.println(viewCancellationDTOList);
        model.addAttribute("viewCancellationDTOList", viewCancellationDTOList);
        return "salemanager/listRefund";
    }

    @GetMapping("/management/ConfirmRefund/hotelBookingId={hotelBookingId}&userId={userId}")
    public String confirmRefund(@PathVariable Long hotelBookingId,
                                @PathVariable Long userId,
                                Model model) {

        BookingDetailsDTO bookingDetailsDTO = bookingRoomDetailsService.getBookingDetailsByUser(userId, hotelBookingId);
        System.out.println(bookingDetailsDTO);
        model.addAttribute("bookingDetailsDTO", bookingDetailsDTO);
        model.addAttribute("userId", userId);

        return "salemanager/confirmRefund";
    }

    @PostMapping("/management/listRefund")
    public ResponseEntity<String> handleCancellationRequest(ViewCancellationDTO viewCancellationDTO) {
        try {
            // You can access the data from the JSON object using cancellationRequest
////            Long hotelBookingId = viewCancellationDTO.getHotelBooking().getHotelBookingId();
//            System.out.println("Gư thành công:" + hotelBookingId);
//            System.out.println("Gư thành công:" + userId);
            // Perform the necessary operations with the received data
            // For example, update the refund in the database

            return ResponseEntity.ok("Yêu cầu huỷ phòng đã được xử lý thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xử lý yêu cầu huỷ phòng.");
        }
    }
}
