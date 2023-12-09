package fu.hbs.service.dao;

import fu.hbs.dto.HotelBookingDTO.SearchingResultRoomDTO;
import fu.hbs.entities.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    List<Room> countRoomAvaliableByCategory(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut);

    List<SearchingResultRoomDTO> getSearchingRoomForBooking(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut);

    List<Room> findAvailableRoom(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut);
    Room findRoomById(Long id);
}
