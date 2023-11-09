package fu.hbs.service.dao;

import java.util.List;

import org.springframework.data.domain.Page;

import fu.hbs.entities.HotelBooking;

public interface ReceptionistBookingService {
	public List<HotelBooking> findAll();

	public HotelBooking findById(Long id);

	public void save(HotelBooking booking);

	public void deleteById(Long id);

	List<HotelBooking> findAllWithStatusTwo();

	List<HotelBooking> findAllWithStatusOne();





}
