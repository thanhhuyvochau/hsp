package fu.hbs.dto.HotelBookingDTO;

import fu.hbs.dto.RoomDTO;
import fu.hbs.entities.BookingRoomDetails;
import fu.hbs.entities.HotelBooking;
import fu.hbs.entities.RoomCategories;
import fu.hbs.utils.BookingUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckInBookingDetailsDTO {
    private Long categoryId;
    private String categoryName;
    private long roomNumber;
    private String note;
    private int customerNumber;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private List<RoomDTO> optionRooms = new ArrayList<>();
    public static CheckInBookingDetailsDTO valueOf(HotelBooking hotelBooking, RoomCategories roomCategory, List<BookingRoomDetails> bookingRoomDetails, Instant checkIn, Instant checkOut) {
        CheckInBookingDetailsDTO detailsDTO = new CheckInBookingDetailsDTO();
        detailsDTO.setCategoryId(detailsDTO.getCategoryId());
        detailsDTO.setCategoryName(roomCategory.getRoomCategoryName());
        detailsDTO.setRoomNumber(BookingUtil.calculateRoomNumber(roomCategory, bookingRoomDetails));
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (hotelBooking.getStatusId() == 3L){
             totalPrice = BookingUtil.calculatePriceBetweenDate(checkIn, hotelBooking.getCheckOut(), roomCategory.getRoomCategoryId(), true);
        }else{
            totalPrice = BookingUtil.calculatePriceBetweenDate(checkIn, checkOut, roomCategory.getRoomCategoryId(), true);
        }
        detailsDTO.setPrice(BookingUtil.getPriceOfRoom(roomCategory.getRoomCategoryId()));
        detailsDTO.setTotalPrice(totalPrice.multiply(BigDecimal.valueOf(detailsDTO.getRoomNumber())));
        return detailsDTO;
    }
}
