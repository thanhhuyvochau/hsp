package fu.hbs.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fu.hbs.entities.HotelBooking;
import fu.hbs.entities.RoomStatus;
import fu.hbs.repository.HotelBookingRepository;
import fu.hbs.repository.RoomStatusRepository;
import fu.hbs.service.dao.ReceptionistBookingService;

public class ReceptionistBookingServiceImpl implements ReceptionistBookingService {
	@Autowired
	private HotelBookingRepository bookingRepository;
	@Autowired
    private RoomStatusRepository roomStatusRepository; // Autowire RoomStatusRepository
	@Override
	public List<HotelBooking> findAll() {
		return bookingRepository.findAll();
	}

	@Override
	public HotelBooking findById(Long id) {
		Optional<HotelBooking> result = bookingRepository.findById(id);
		HotelBooking booking = null;
		if (result.isPresent()) {
			booking = result.get();
		} else {
			throw new RuntimeException("Không tìm thấy đặt phòng có id - " + id);
		}
		return booking;
	}

	@Override
	public void save(HotelBooking booking) {
		bookingRepository.save(booking);
	}

	@Override
	public void deleteById(Long id) {
		bookingRepository.deleteById(id);
	}

	@Override
	public List<HotelBooking> findAllWithStatusOne() {
	    return bookingRepository.findByStatusId(1L);
	}
	
	@Override
	public List<HotelBooking> findAllWithStatusTwo() {
	    return bookingRepository.findByStatusId(2L);
	}

}
