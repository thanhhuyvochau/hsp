package fu.hbs.service.dao;

import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.dto.HotelBookingDTO.ViewHotelBookingDTO;

import java.util.Date;
import java.util.List;

public interface HotelBookingService {
    List<ViewHotelBookingDTO> findAllByUserId(Long id);

    public HotelBookingAvailable findBookingsByDates(Date checkIn, Date checkOut, int numberPerson);
}
