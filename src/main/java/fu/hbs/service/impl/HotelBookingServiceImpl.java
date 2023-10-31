package fu.hbs.service.impl;

import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.entities.CategoryRoomFurniture;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomCategories;
import fu.hbs.entities.RoomFurniture;
import fu.hbs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelBookingServiceImpl {
    @Autowired
    private HotelBookingRepository hotelBookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    RoomCategoriesRepository roomCategoriesRepository;
    @Autowired
    CategoryRoomFurnitureRepository categoryRoomFurnitureRepository;
    @Autowired
    RoomFurnitureRepository roomFurnitureRepository;


    public HotelBookingAvailable findBookingsByDates(Date checkIn, Date checkOut, int numberPerson) {
        HotelBookingAvailable hotelBookingAvailable = new HotelBookingAvailable();
        List<Room> rooms = roomRepository.getAllRoom(checkIn, checkOut, numberPerson);


        List<RoomCategories> addedCategories = new ArrayList<>();
        RoomCategories categories = new RoomCategories();

        Map<Long, List<Room>> groupedRooms = rooms.stream()
                .collect(Collectors.groupingBy(Room::getRoomCategoryId));
        for (Map.Entry<Long, List<Room>> entry : groupedRooms.entrySet()) {
            Long categoryId = entry.getKey();
            List<Room> roomsWithSameCategory = entry.getValue();

            addedCategories.add(roomCategoriesRepository.findDistinctByRoomCategoryId(categoryId));


        }
        System.out.println(addedCategories);
        List<CategoryRoomFurniture> categoryRoomFurnitures = new ArrayList<>();
//        CategoryRoomFurniture categoryRoomFurniture = new CategoryRoomFurniture();
        for (int i = 0; i < addedCategories.size(); i++) {
            categoryRoomFurnitures = categoryRoomFurnitureRepository.findByRoomCategoryId(addedCategories.get(i).getRoomCategoryId());
        }

        List<RoomFurniture> roomFurnitures = new ArrayList<>();
        RoomFurniture roomFurniture = new RoomFurniture();
        for (int i = 0; i < categoryRoomFurnitures.size(); i++) {
            roomFurniture = roomFurnitureRepository.findByFurnitureId(categoryRoomFurnitures.get(i).getFurnitureId());
            roomFurnitures.add(roomFurniture);
        }

        hotelBookingAvailable.setRooms(rooms);
        hotelBookingAvailable.setRoomCategories(addedCategories);
        hotelBookingAvailable.setCategoryRoomFurnitures(categoryRoomFurnitures);
        hotelBookingAvailable.setRoomFurnitures(roomFurnitures);
        hotelBookingAvailable.setTotalRoom(groupedRooms);
        return hotelBookingAvailable;
    }
}
