package fu.hbs.service.dao;

import java.math.BigDecimal;
import java.util.List;

import fu.hbs.dto.HotelBookingDTO.SaveCheckinDTO;
import fu.hbs.dto.HotelBookingDTO.SaveCheckoutDTO;
import fu.hbs.dto.HotelBookingDTO.ViewCheckoutDTO;
import fu.hbs.dto.HotelBookingDTO.CreateHotelBookingDTO;

import fu.hbs.entities.HotelBooking;
import fu.hbs.entities.Transactions;

public interface ReceptionistBookingService {
    public List<HotelBooking> findAll();

    public HotelBooking findById(Long id);

    public void save(HotelBooking booking);

    public void deleteById(Long id);

    List<HotelBooking> findAllWithStatusTwo();

    List<HotelBooking> findAllWithStatusOne();

    Long createHotelBookingByReceptionist(CreateHotelBookingDTO bookingRequest);

    boolean checkout(SaveCheckoutDTO saveCheckoutDTO);

    BigDecimal getTotalPriceOfHotelBooking(Long hotelBooking);

    Boolean checkIn(Long hotelBookingId);

    List<HotelBooking> findAllWithStatusOneAndValidBooking(Boolean validBooking);

    Boolean checkIn(SaveCheckinDTO checkinDTO);


}
