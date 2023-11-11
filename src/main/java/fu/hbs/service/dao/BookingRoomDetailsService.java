package fu.hbs.service.dao;

import fu.hbs.dto.HotelBookingDTO.BookingDetailsDTO;
import fu.hbs.entities.BookingRoomDetails;
import fu.hbs.entities.User;
import org.springframework.security.core.Authentication;

public interface BookingRoomDetailsService {
    BookingRoomDetails save(BookingRoomDetails bookingRoomDetails);

    BookingDetailsDTO getBookingDetails(Authentication authentication, Long hotelBookingId);

    BookingDetailsDTO getBookingDetailsByUser(Long userId, Long hotelBookingId);
}
