package fu.hbs.dto.HotelBookingDTO;

import fu.hbs.entities.HotelBooking;
import fu.hbs.entities.Room;
import fu.hbs.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingDetailsDTO {
    private User user;
    private HotelBooking hotelBooking;
    private List<RoomInDetailsDTO> bookingRoomDetails;
    private Map<Long, List<Room>> groupedRooms;

}
