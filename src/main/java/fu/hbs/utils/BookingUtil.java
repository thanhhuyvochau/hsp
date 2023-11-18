package fu.hbs.utils;

import fu.hbs.entities.*;
import fu.hbs.service.dao.CategoryRoomPriceService;
import fu.hbs.service.dao.HotelBookingServiceService;
import fu.hbs.service.dao.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class BookingUtil {

    private static CategoryRoomPriceService staticCategoryRoomPriceService;
    private static HotelBookingServiceService statichHotelBookingServiceService;

    private static ServiceService staticRoomServiceService;

    public BookingUtil(CategoryRoomPriceService categoryRoomPriceService, HotelBookingServiceService hotelBookingServiceService, ServiceService roomServiceService) {
        BookingUtil.staticCategoryRoomPriceService = categoryRoomPriceService;
        BookingUtil.statichHotelBookingServiceService = hotelBookingServiceService;
        BookingUtil.staticRoomServiceService = roomServiceService;
    }

    public static long calculateRoomNumber(RoomCategories roomCategories, List<BookingRoomDetails> bookingRoomDetails) {
        return bookingRoomDetails.stream()
                .filter(bookingRoomDetail -> bookingRoomDetail.getRoomCategoryId().equals(roomCategories.getRoomCategoryId()))
                .count();
    }

    public static BigDecimal calculatePriceBetweenDate(Instant checkIn, Instant checkout, Long categoryId) {
        long diffInDays = checkIn.until(checkout, ChronoUnit.DAYS);
        BigDecimal pricePer = getPriceOfRoom(categoryId);
        return pricePer.multiply(BigDecimal.valueOf(diffInDays));
    }

    public static BigDecimal getPriceOfRoom(Long categoryId) {
        CategoryRoomPrice categoryRoomPrice = staticCategoryRoomPriceService.findByCateRoomPriceId(categoryId);
        return categoryRoomPrice.getPrice();
    }

    public static List<HotelBookingService> getAllHotelBookingService(Long hotelId) {
        return statichHotelBookingServiceService.findAllByHotelBookingId(hotelId);
    }

    public static BigDecimal calculateTotalPriceOfUseService(RoomService roomService, int quantity) {
        BigDecimal pricePer = roomService.getServicePrice();
        return pricePer.multiply(BigDecimal.valueOf(quantity));
    }

    public static Map<Long, RoomService> getAllRoomServiceAsMap() {
        return staticRoomServiceService.getAllServices().stream().collect(Collectors.toMap(RoomService::getServiceId, Function.identity()));
    }
}
