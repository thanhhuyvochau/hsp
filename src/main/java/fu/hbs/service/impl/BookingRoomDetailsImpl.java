package fu.hbs.service.impl;

import fu.hbs.entities.BookingRoomDetails;
import fu.hbs.repository.BookingRoomRepository;
import fu.hbs.service.dao.BookingRoomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingRoomDetailsImpl implements BookingRoomDetailsService {

    @Autowired
    BookingRoomRepository bookingRoomRepository;

    
    @Override
    public BookingRoomDetails save(BookingRoomDetails bookingRoomDetails) {

        return bookingRoomRepository.save(bookingRoomDetails);
    }
}
