package fu.hbs.dto.HotelBookingDTO;

import fu.hbs.dto.RoomServiceDTO.RoomBookingServiceDTO;
import fu.hbs.entities.PaymentType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CheckoutDTO {
    private Long hotelBookingId;
    private BigDecimal totalPrice;
    private List<RoomBookingServiceDTO> roomBookingServiceDTOS = new ArrayList<>();
    private BigDecimal surcharge = BigDecimal.ZERO;
    private Long paymentTypeId = 1L;

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
}
