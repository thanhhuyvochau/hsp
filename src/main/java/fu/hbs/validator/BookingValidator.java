package fu.hbs.validator;

import fu.hbs.entities.Room;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingValidator {
    public static boolean isValidDateToCheckIn(Instant checkIn) {
        Instant currentDay = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant checkInTruncated = checkIn.truncatedTo(ChronoUnit.DAYS);
        return currentDay.equals(checkInTruncated);
    }

    public static boolean isExistNotReadyRoom(List<Room> allBookedRooms) {
        return allBookedRooms.stream().anyMatch(room -> room.getRoomStatusId() != 2L);
    }

}
