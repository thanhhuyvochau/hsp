package fu.hbs.dto.HotelBookingDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchingRoomDTO {
    private Long categoryId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberPeople;
}
