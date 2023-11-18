package fu.hbs.dto.HotelBookingDTO;

import fu.hbs.dto.CategoryRoomPriceDTO.DateInfoCategoryRoomPriceDTO;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomCategories;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateBookingDTO {
    private Instant checkIn;
    private Instant checkOut;
    private List<RoomCategories> roomCategoriesList;
    private List<Room> rooms;
    private BigDecimal totalPrice;
    private int totalDay;
    private Map<Long, Integer> roomCategoryMap;
    private Map<Long, BigDecimal> totalPriceByCategoryId;
    private List<DateInfoCategoryRoomPriceDTO> dateInfoList;
}
