package fu.hbs.dto.HotelBookingDTO;

import fu.hbs.entities.Room;
import fu.hbs.entities.RoomCategories;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateBookingDTO {
    private LocalDate checkIn;
    private LocalDate checkOut;
    private List<RoomCategories> roomCategoriesList;
    private List<Room> rooms;
    private BigDecimal totalPrice;
    private int totalDay;
}
