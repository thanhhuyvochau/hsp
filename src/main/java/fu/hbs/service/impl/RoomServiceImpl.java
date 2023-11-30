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
import fu.hbs.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if (roomCategoryId == -1) {
            rooms = roomRepository.findAvailableRoomsByDate(checkIn, checkOut);
        } else {
            rooms = roomRepository.findAvailableRoomsByCategoryId(roomCategoryId, checkIn, checkOut);
        }
        Instant now = Instant.now();
        LocalDate localDate = LocalDateTime.ofInstant(now, ZoneId.systemDefault()).toLocalDate();
        if (localDate.equals(checkIn)){
            rooms = rooms.stream().filter(room -> room.getRoomStatusId() == 2).toList();
        }


        Map<Long, List<Room>> roomMapWithCategoryIdAsKey = rooms.stream()
                .collect(Collectors.groupingBy(Room::getRoomCategoryId));

        List<Room> roomGroupByCategory = roomMapWithCategoryIdAsKey.values().stream()
                .map(roomsList -> roomsList.stream().findFirst()
                        .orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toList());

        Map<Long, RoomCategories> roomCategoryAsMap = BookingUtil.getAllRoomCategoryAsMap();
        List<SearchingResultRoomDTO> searchingResultRoomDTOList = new ArrayList<>();
        for (Room room : roomGroupByCategory) {
            List<Room> allRoomsByIdCategory = roomRepository.findByRoomCategoryId(room.getRoomCategoryId()); // All room by category
            List<Room> availableRoomsByCategoryId = roomRepository.findAvailableRoomsByCategoryId(room.getRoomCategoryId(), checkIn, checkOut);

            RoomCategories category = roomCategoryAsMap.get(room.getRoomCategoryId());
            CategoryRoomPrice categoryRoomPrice = categoryRoomPriceRepository.findByRoomCategoryId(category.getRoomCategoryId());
            SearchingResultRoomDTO searchingRoomDTO = new SearchingResultRoomDTO
                    (availableRoomsByCategoryId.size(), categoryRoomPrice.getPrice(), category.getRoomCategoryId(), category.getRoomCategoryName(), category.getDescription(), category.getSquare(), category.getNumberPerson(), category.getImage(), room.getBedSize(), allRoomsByIdCategory.size() - availableRoomsByCategoryId.size());
            searchingResultRoomDTOList.add(searchingRoomDTO);

        }
        return searchingResultRoomDTOList;
    }
}
