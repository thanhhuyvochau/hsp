package fu.hbs.service.dao;

import fu.hbs.entities.HotelBookingService;

import java.util.List;

public interface HotelBookingServiceService {
    List<HotelBookingService> findAllByHotelBookingId(Long id);
}
