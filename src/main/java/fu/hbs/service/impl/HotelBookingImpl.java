package fu.hbs.service.impl;

import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.entities.HotelBooking;
import fu.hbs.entities.Room;
import fu.hbs.repository.HotelBookingRepository;
import fu.hbs.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HotelBookingImpl {
    @Autowired
    private HotelBookingRepository hotelBookingRepository;
    @Autowired
    private RoomRepository roomRepository;

    public HotelBookingAvailable findBookingsByDates(Date checkIn, Date checkOut, int numberPerson) {
        HotelBookingAvailable hotelBookingAvailable = new HotelBookingAvailable();
        List<Room> rooms = roomRepository.getAllRoom(checkIn, checkOut, numberPerson);
        System.out.println(rooms);

        hotelBookingAvailable.setRooms(rooms);
        return hotelBookingAvailable;
    }
}
