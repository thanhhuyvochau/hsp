package fu.hbs.service.dao;

import fu.hbs.dto.CancellationFormDTO;
import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.dto.HotelBookingDTO.CreateBookingDTO;
import fu.hbs.dto.HotelBookingDTO.ViewHotelBookingDTO;
import fu.hbs.entities.HotelBooking;
import fu.hbs.exceptionHandler.RoomCategoryNamesNullException;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface HotelBookingService {
    List<ViewHotelBookingDTO> findAllByUserId(Long id);

    public HotelBookingAvailable findBookingsByDates(Date checkIn, Date checkOut, int numberPerson) throws RoomCategoryNamesNullException;


    public CreateBookingDTO createBooking(Long categoryId, LocalDate checkIn, LocalDate checkOut);

    List<ViewHotelBookingDTO> findAllByUserIdAndSameTime(Long id);


    HotelBooking save(HotelBooking hotelBooking);

    public CreateBookingDTO createBooking(
            List<Long> roomCategoryNames,
            List<Integer> selectedRoomCategories,
            LocalDate checkIn,
            LocalDate checkOut,
            HttpSession session);

    //    void cancelBooking(Long hotelBookingId, String reason, String otherReason, String bank, String account, String userName);
    void cancelBooking(CancellationFormDTO cancellationFormDTO, Authentication authentication);
}
