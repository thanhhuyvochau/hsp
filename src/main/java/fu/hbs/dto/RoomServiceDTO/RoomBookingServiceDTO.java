package fu.hbs.dto.RoomServiceDTO;

import fu.hbs.entities.RoomBookingServices;
import fu.hbs.entities.RoomService;

public class RoomBookingServiceDTO {
    private Long roomServiceId;
    private int quantity;

    public Long getRoomServiceId() {
        return roomServiceId;
    }

    public void setRoomServiceId(Long roomServiceId) {
        this.roomServiceId = roomServiceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
