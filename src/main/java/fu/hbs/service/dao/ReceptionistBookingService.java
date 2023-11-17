package fu.hbs.service.dao;

import java.util.List;

import fu.hbs.dto.HotelBookingDTO.SaveCheckoutDTO;
import fu.hbs.dto.HotelBookingDTO.ViewCheckoutDTO;
import fu.hbs.dto.HotelBookingDTO.CreateHotelBookingDTO;

import fu.hbs.entities.HotelBooking;

public interface ReceptionistBookingService {
    public List<HotelBooking> findAll();

    public HotelBooking findById(Long id);

    public void save(HotelBooking booking);

    public void deleteById(Long id);

    List<HotelBooking> findAllWithStatusTwo();

    List<HotelBooking> findAllWithStatusOne();

    public void createHotelBookingByReceptionist(CreateHotelBookingDTO bookingRequest);

    boolean checkout(SaveCheckoutDTO saveCheckoutDTO);


}
