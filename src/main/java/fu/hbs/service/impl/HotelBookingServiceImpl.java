package fu.hbs.service.impl;

import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomCategories;
import fu.hbs.repository.HotelBookingRepository;
import fu.hbs.repository.RoomCategoriesRepository;
import fu.hbs.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HotelBookingServiceImpl {
    @Autowired
    private HotelBookingRepository hotelBookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    RoomCategoriesRepository roomCategoriesRepository;

    public HotelBookingAvailable findBookingsByDates(Date checkIn, Date checkOut, int numberPerson) {
        HotelBookingAvailable hotelBookingAvailable = new HotelBookingAvailable();
        List<Room> rooms = roomRepository.getAllRoom(checkIn, checkOut, numberPerson);

        List<RoomCategories> addedCategories = new ArrayList<>();
        RoomCategories categories = new RoomCategories();

        for (int i = 0; i < rooms.size(); i++) {
            Long roomCategoryId = rooms.get(i).getRoomCategoryId();
            categories = roomCategoriesRepository.findByRoomCategoryId(roomCategoryId);

            boolean categoryExists = false;

            for (RoomCategories addedCategory : addedCategories) {
                if (addedCategory.getRoomCategoryName().equals(categories.getRoomCategoryName())) {
                    categoryExists = true;
                    break;
                }
            }

            if (!categoryExists) {
                addedCategories.add(categories);
            }
        }


        hotelBookingAvailable.setRooms(rooms);
        hotelBookingAvailable.setRoomCategories(addedCategories);
        return hotelBookingAvailable;
    }
}
