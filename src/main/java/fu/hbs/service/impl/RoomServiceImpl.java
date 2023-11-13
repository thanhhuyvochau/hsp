package fu.hbs.service.impl;

import fu.hbs.dto.HotelBookingDTO.SearchingResultRoomDTO;
import fu.hbs.entities.CategoryRoomPrice;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomCategories;
import fu.hbs.repository.CategoryRoomPriceRepository;
import fu.hbs.repository.RoomCategoriesRepository;
import fu.hbs.repository.RoomRepository;
import fu.hbs.service.dao.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    public SearchingResultRoomDTO getSearchingRoomForBooking(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut) {
        List<Room> rooms = roomRepository.findAvailableRoomsByCategoryId(roomCategoryId, checkIn, checkOut);
        List<Room> allRoomsByIdCategory = roomRepository.findByRoomCategoryId(roomCategoryId);
        RoomCategories category = roomCategoriesRepository.findByRoomCategoryId(roomCategoryId);
        CategoryRoomPrice categoryRoomPrice = categoryRoomPriceRepository.findByRoomCategoryId(roomCategoryId);
        SearchingResultRoomDTO searchingRoomDTO = new SearchingResultRoomDTO(rooms.size(), categoryRoomPrice.getPrice(), category.getRoomCategoryId(), category.getRoomCategoryName(), category.getDescription(), category.getSquare(), category.getNumberPerson(), category.getImage(), "", allRoomsByIdCategory.size() - rooms.size());
        rooms.stream().findFirst().ifPresent(room -> searchingRoomDTO.setBed(room.getBedSize()));
        return searchingRoomDTO;
    }
}
