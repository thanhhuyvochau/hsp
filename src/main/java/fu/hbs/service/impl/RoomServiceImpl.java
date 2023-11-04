package fu.hbs.service.impl;

import fu.hbs.entities.Room;
import fu.hbs.repository.RoomRepository;
import fu.hbs.service.dao.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> countRoomAvaliableByCategory(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRoomsByCategoryId(roomCategoryId, checkIn, checkOut);
    }
}
