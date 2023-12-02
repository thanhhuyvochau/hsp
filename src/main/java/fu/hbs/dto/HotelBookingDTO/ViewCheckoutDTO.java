package fu.hbs.dto.HotelBookingDTO;

import fu.hbs.dto.RoomServiceDTO.RoomBookingServiceDTO;
import fu.hbs.entities.*;
import fu.hbs.utils.BookingUtil;
import fu.hbs.utils.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ViewCheckoutDTO {
    private Long hotelBookingId;
    private Long userId;
    private Long statusId;
    private Long serviceId;
    private int totalRoom;
    private String name;
    private String email;
    private String address;
    private String phone;
    private BigDecimal totalRoomPrice = BigDecimal.ZERO;
    private BigDecimal depositPrice = BigDecimal.ZERO;
    private String checkIn;
    private String checkOut;
    private Long paymentTypeId = 1L;
    private String paymentTypeName;
    private BigDecimal totalServicePrice = BigDecimal.ZERO;
    private List<CheckoutBookingDetailsDTO> bookingDetails = new ArrayList<>();
    private List<RoomBookingServiceDTO> roomBookingServiceDTOS = new ArrayList<>();
    private BigDecimal prepay = BigDecimal.ZERO;
    private BigDecimal taxPrice = BigDecimal.ZERO;

    public static ViewCheckoutDTO valueOf(HotelBooking hotelBooking,
                                          List<BookingRoomDetails> bookingRoomDetails,
                                          PaymentType paymentType,
                                          Map<Long, RoomCategories> roomCategoriesMap) {

        ViewCheckoutDTO viewCheckoutDto = new ViewCheckoutDTO();
        viewCheckoutDto.setHotelBookingId(hotelBooking.getHotelBookingId());
        viewCheckoutDto.setUserId(hotelBooking.getUserId());
        viewCheckoutDto.setStatusId(hotelBooking.getStatusId());
        viewCheckoutDto.setServiceId(hotelBooking.getServiceId());
        viewCheckoutDto.setTotalRoom(hotelBooking.getTotalRoom());
        viewCheckoutDto.setName(hotelBooking.getName());
        viewCheckoutDto.setEmail(hotelBooking.getEmail());
        viewCheckoutDto.setAddress(hotelBooking.getAddress());
        viewCheckoutDto.setPhone(hotelBooking.getPhone());
        viewCheckoutDto.setDepositPrice(hotelBooking.getDepositPrice());
        viewCheckoutDto.setCheckIn(DateUtil.formatInstantToPattern(hotelBooking.getCheckIn()));
        if (hotelBooking.getStatusId() == 2L){
            viewCheckoutDto.setCheckOut(DateUtil.formatInstantToPattern(Instant.now()));
        }else{
            viewCheckoutDto.setCheckOut(DateUtil.formatInstantToPattern(hotelBooking.getCheckOut()));
        }
        viewCheckoutDto.setPaymentTypeId(paymentType.getPaymentId());
        viewCheckoutDto.setPaymentTypeName(paymentType.getPaymentName());

        List<RoomCategories> allBookingCategories = bookingRoomDetails.stream().map(BookingRoomDetails::getRoomCategoryId).distinct().map(categoryId -> {
            RoomCategories roomCategory = roomCategoriesMap.get(categoryId);
            return roomCategory;
        }).toList();

        for (RoomCategories category : allBookingCategories) {
            List<BookingRoomDetails> bookingRoomDetailsByCategory = bookingRoomDetails.stream()
                    .filter(bookingRoomDetail -> bookingRoomDetail.getRoomCategoryId().equals(category.getRoomCategoryId()))
                    .toList();
            CheckoutBookingDetailsDTO detailsDTO = CheckoutBookingDetailsDTO.valueOf(category, bookingRoomDetailsByCategory, hotelBooking.getCheckIn(), Instant.now());
            viewCheckoutDto.getBookingDetails().add(detailsDTO);
        }


        List<HotelBookingService> usedBookingServices = BookingUtil.getAllHotelBookingService(hotelBooking.getHotelBookingId());
        Map<Long, RoomService> roomServiceAsMap = BookingUtil.getAllRoomServiceAsMap();
        for (HotelBookingService usedBookingService : usedBookingServices) {
            RoomService roomService = roomServiceAsMap.get(usedBookingService.getServiceId());
//            RoomBookingServiceDTO roomBookingServiceDTO = RoomBookingServiceDTO.valueOf(roomService, usedBookingService.getQuantity());
            RoomBookingServiceDTO roomBookingServiceDTO = RoomBookingServiceDTO.valueOf(roomService, 1);

            viewCheckoutDto.getRoomBookingServiceDTOS().add(roomBookingServiceDTO);
        }
        BigDecimal totalRoomPrice = viewCheckoutDto.getBookingDetails().stream()
                .reduce(BigDecimal.ZERO, (subTotal, bookingDetail) -> subTotal.add(bookingDetail.getTotalPrice()), BigDecimal::add);
        BigDecimal totalHotelServicePrice = viewCheckoutDto.getRoomBookingServiceDTOS().stream()
                .reduce(BigDecimal.ZERO, (subTotal, useHotelService) -> subTotal.add(useHotelService.getTotalPrice()), BigDecimal::add);


        viewCheckoutDto.setTotalRoomPrice(totalRoomPrice);
        viewCheckoutDto.setTotalServicePrice(totalHotelServicePrice);
        viewCheckoutDto.setPrepay(BigDecimal.ZERO);
        return viewCheckoutDto;
    }
}
