package fu.hbs.utils;

import fu.hbs.dto.HotelBookingDTO.SearchingResultRoomDTO;
import fu.hbs.entities.*;
import fu.hbs.service.dao.CategoryRoomPriceService;
import fu.hbs.service.dao.HotelBookingServiceService;
import fu.hbs.service.dao.RoomCategoryService;
import fu.hbs.service.dao.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.*;
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

    private static RoomCategoryService staticRoomCategoryService;

    private static fu.hbs.service.dao.HotelBookingService staticHotelBookingService;
    private static fu.hbs.service.dao.RoomService staticRoomService;
    public BookingUtil(CategoryRoomPriceService categoryRoomPriceService,
                       HotelBookingServiceService hotelBookingServiceService,
                       ServiceService roomServiceService,
                       RoomCategoryService roomCategoryService,
                       fu.hbs.service.dao.HotelBookingService hotelBookingService,
                       fu.hbs.service.dao.RoomService roomService) {
        BookingUtil.staticCategoryRoomPriceService = categoryRoomPriceService;
        BookingUtil.statichHotelBookingServiceService = hotelBookingServiceService;
        BookingUtil.staticRoomServiceService = roomServiceService;
        BookingUtil.staticRoomCategoryService = roomCategoryService;
        BookingUtil.staticHotelBookingService = hotelBookingService;
        BookingUtil.staticRoomService = roomService;
    }

    public static long calculateRoomNumber(RoomCategories roomCategories, List<BookingRoomDetails> bookingRoomDetails) {
        return bookingRoomDetails.stream()
                .filter(bookingRoomDetail -> bookingRoomDetail.getRoomCategoryId().equals(roomCategories.getRoomCategoryId()))
                .count();
    }

    public static BigDecimal calculatePriceBetweenDate(Instant checkIn, Instant checkout, Long categoryId, Boolean isCalculateForCheckout) {
//        long diffInDays = checkIn.until(checkout, ChronoUnit.HOURS);
        Duration durationOfStay = Duration.between(checkIn, checkout);

        BigDecimal pricePer = getPriceOfRoom(categoryId);
        // If the user checked out earlier than 24 hours, deduct 1 day's price as a refund
        long durationOfStayDays = durationOfStay.toDays();
        float multipler = 0L;
        checkIn = checkIn.truncatedTo(ChronoUnit.DAYS);
        checkout = checkout.truncatedTo(ChronoUnit.DAYS);
        // If checkin same day multipler equal 1 => mean 1 day
        if (checkIn.equals(checkout)){
            multipler = 1;
        }else{
            while (checkIn.isBefore(checkout)){
                int dayType = staticHotelBookingService.getDayType(LocalDateTime.ofInstant(checkIn, ZoneId.systemDefault()).toLocalDate());
                if (dayType == 3){
                    multipler+=3;
                } else if (dayType == 2) {
                    multipler+=1.5F;
                }else{
                    multipler+=1;
                }
                checkIn = checkIn.plus(1,ChronoUnit.DAYS);
            }
        }


        BigDecimal totalPrice  = BigDecimal.ZERO;
        if (isCalculateForCheckout){
            Duration durationOfCheckoutSooner = Duration.between(Instant.now(), checkout);
            long soonerDays = durationOfCheckoutSooner.toDays();
             totalPrice = pricePer.multiply(BigDecimal.valueOf(multipler));
            if (soonerDays > 1) {
                BigDecimal refund = pricePer.multiply(BigDecimal.valueOf(soonerDays));
                return totalPrice.subtract(refund);
            }
        }else{
             totalPrice = pricePer.multiply(BigDecimal.valueOf(multipler));
        }

        return totalPrice;

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

    public static Map<Long, RoomCategories> getAllRoomCategoryAsMap() {
        return staticRoomCategoryService.getAllRoomCategories().stream().collect(Collectors.toMap(RoomCategories::getRoomCategoryId, Function.identity()));
    }

    public static BigDecimal calculateTotalPriceOfBooking(BigDecimal servicePrice, BigDecimal roomPrice, BigDecimal prePay) {
        BigDecimal taxPrice = servicePrice.add(roomPrice).multiply(BigDecimal.valueOf(0.1));
//        return servicePrice.add(roomPrice).add(taxPrice).subtract(prePay);
        return servicePrice.add(roomPrice).add(taxPrice); // Total is not related to prepay

    }
    public static List<Room> findAvailableRoom(Long roomCategoryId, LocalDate checkIn, LocalDate checkOut){
        return staticRoomService.findAvailableRoom(roomCategoryId,checkIn,checkOut);
    }
    public static Room findRoomById(Long roomId){
        return staticRoomService.findRoomById(roomId);
    }
}
