package fu.hbs.dto.HotelBookingDTO;

import fu.hbs.dto.RoomServiceDTO.RoomBookingServiceDTO;
import fu.hbs.entities.*;
import fu.hbs.utils.BookingUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private BigDecimal totalPrice;
    private BigDecimal depositPrice;
    private Date checkIn;
    private Date checkOut;
    private Long paymentTypeId = 1L;
    private String paymentTypeName;


    private List<CheckoutBookingDetailsDTO> bookingDetails = new ArrayList<>();
    private List<RoomBookingServiceDTO> roomBookingServiceDTOS = new ArrayList<>();
    private BigDecimal surcharge = BigDecimal.ZERO;


    public Long getHotelBookingId() {
        return hotelBookingId;
    }

    public void setHotelBookingId(Long hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<RoomBookingServiceDTO> getRoomBookingServiceDTOS() {
        return roomBookingServiceDTOS;
    }

    public void setRoomBookingServiceDTOS(List<RoomBookingServiceDTO> roomBookingServiceDTOS) {
        this.roomBookingServiceDTOS = roomBookingServiceDTOS;
    }

    public BigDecimal getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(BigDecimal surcharge) {
        this.surcharge = surcharge;
    }

    public Long getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

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
        viewCheckoutDto.setCheckIn(hotelBooking.getCheckIn());
        viewCheckoutDto.setCheckOut(hotelBooking.getCheckOut());
        viewCheckoutDto.setPaymentTypeId(paymentType.getPaymentId());
        viewCheckoutDto.setPaymentTypeName(paymentType.getPaymentName());

        viewCheckoutDto.setTotalPrice(hotelBooking.getTotalPrice());


        List<RoomCategories> allBookingCategories = bookingRoomDetails.stream().map(BookingRoomDetails::getRoomCategoryId).distinct().map(categoryId -> {
            RoomCategories roomCategory = roomCategoriesMap.get(categoryId);
            return roomCategory;
        }).toList();

        for (RoomCategories category : allBookingCategories) {
            List<BookingRoomDetails> bookingRoomDetailsByCategory = bookingRoomDetails.stream()
                    .filter(bookingRoomDetail -> bookingRoomDetail.getRoomCategoryId().equals(category.getRoomCategoryId()))
                    .toList();
            CheckoutBookingDetailsDTO detailsDTO = CheckoutBookingDetailsDTO.valueOf(category, bookingRoomDetailsByCategory, hotelBooking.getCheckIn(), hotelBooking.getCheckOut());
            viewCheckoutDto.getBookingDetails().add(detailsDTO);
        }


        List<HotelBookingService> usedBookingServices = BookingUtil.getAllHotelBookingService(hotelBooking.getHotelBookingId());
        Map<Long, RoomService> roomServiceAsMap = BookingUtil.getAllRoomServiceAsMap();
        for (HotelBookingService usedBookingService : usedBookingServices) {
            RoomService roomService = roomServiceAsMap.get(usedBookingService.getServiceId());
            RoomBookingServiceDTO roomBookingServiceDTO = RoomBookingServiceDTO.valueOf(roomService, usedBookingService.getQuantity());
            viewCheckoutDto.getRoomBookingServiceDTOS().add(roomBookingServiceDTO);
        }

        return viewCheckoutDto;
    }
}
