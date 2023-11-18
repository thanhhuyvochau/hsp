package fu.hbs.service.impl;

import fu.hbs.dto.HotelBookingDTO.SearchingResultRoomDTO;
import fu.hbs.entities.CategoryRoomPrice;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomCategories;
import fu.hbs.repository.CategoryRoomPriceRepository;
import fu.hbs.repository.RoomCategoriesRepository;
import fu.hbs.repository.RoomRepository;
import fu.hbs.service.dao.RoomService;
import fu.hbs.utils.BookingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomCategoriesRepository roomCategoriesRepository;
    @Autowired
    private CategoryRoomPriceRepository categoryRoomPriceRepository;

    @Override
    public List<Room> countRoomAvaliableByCategory(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRoomsByCategoryId(roomCategoryId, checkIn, checkOut);
    }

    @Override
    public List<SearchingResultRoomDTO> getSearchingRoomForBooking(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut) {
        List<Room> rooms;
        if (roomCategoryId == null) {
            rooms = roomRepository.findAvailableRoomsByDate(checkIn, checkOut);
        } else {
            rooms = roomRepository.findAvailableRoomsByCategoryId(roomCategoryId, checkIn, checkOut);
        }

        CategoryRoomPrice categoryRoomPrice = categoryRoomPriceRepository.findByRoomCategoryId(roomCategoryId);

        Map<Long, RoomCategories> roomCategoryAsMap = BookingUtil.getAllRoomCategoryAsMap();
        List<SearchingResultRoomDTO> searchingResultRoomDTOList = new ArrayList<>();

        for (Room room : rooms) {
            List<Room> allRoomsByIdCategory = roomRepository.findByRoomCategoryId(room.getRoomCategoryId());
            RoomCategories category = roomCategoryAsMap.get(room.getRoomCategoryId());

            SearchingResultRoomDTO searchingRoomDTO = new SearchingResultRoomDTO
                    (rooms.size(), categoryRoomPrice.getPrice(), category.getRoomCategoryId(), category.getRoomCategoryName(), category.getDescription(), category.getSquare(), category.getNumberPerson(), category.getImage(), room.getBedSize(), allRoomsByIdCategory.size() - rooms.size());
            searchingResultRoomDTOList.add(searchingRoomDTO);

        }
        return searchingResultRoomDTOList;
    }
}
