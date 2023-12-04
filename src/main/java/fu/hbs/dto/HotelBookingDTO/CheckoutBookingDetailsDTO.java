package fu.hbs.dto.HotelBookingDTO;

import fu.hbs.dto.CategoryRoomPriceDTO.DateInfoCategoryRoomPriceDTO;
import fu.hbs.entities.*;
import fu.hbs.utils.BookingUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckoutBookingDetailsDTO {
    private Long categoryId;
    private String categoryName;
    private long roomNumber;
    private String note;
    private int customerNumber;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public static CheckoutBookingDetailsDTO valueOf(RoomCategories roomCategory, List<BookingRoomDetails> bookingRoomDetails, Instant checkIn, Instant checkOut) {
        CheckoutBookingDetailsDTO detailsDTO = new CheckoutBookingDetailsDTO();
        detailsDTO.setCategoryId(detailsDTO.getCategoryId());
        detailsDTO.setCategoryName(roomCategory.getRoomCategoryName());
        detailsDTO.setRoomNumber(BookingUtil.calculateRoomNumber(roomCategory, bookingRoomDetails));
        detailsDTO.setPrice(BookingUtil.getPriceOfRoom(roomCategory.getRoomCategoryId()));
        detailsDTO.setTotalPrice(BookingUtil.calculatePriceBetweenDate(checkIn, checkOut, roomCategory.getRoomCategoryId(), true));
        return detailsDTO;
    }
}
