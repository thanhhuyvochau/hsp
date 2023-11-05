package fu.hbs.service.dao;

import fu.hbs.entities.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    List<Room> countRoomAvaliableByCategory(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut);
}
