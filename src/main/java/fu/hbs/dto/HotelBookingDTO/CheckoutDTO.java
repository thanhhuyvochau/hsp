package fu.hbs.dto.HotelBookingDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CheckoutDTO {
    private Long hotelBookingId;
    private Long totalPrice;
    private List<Long> usageServicesIdList = new ArrayList<>();
    private BigDecimal surcharge = BigDecimal.ZERO;

    public Long getHotelBookingId() {
        return hotelBookingId;
    }

    public void setHotelBookingId(Long hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Long> getUsageServicesIdList() {
        return usageServicesIdList;
    }

    public void setUsageServicesIdList(List<Long> usageServicesIdList) {
        this.usageServicesIdList = usageServicesIdList;
    }

    public BigDecimal getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(BigDecimal surcharge) {
        this.surcharge = surcharge;
    }
}
