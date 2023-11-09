/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 2/11/2023     1.0        HieuLBM          First Deploy
 */
package fu.hbs.dto.HotelBookingDTO;


import fu.hbs.entities.RoomCategories;
import fu.hbs.entities.RoomStatus;
import fu.hbs.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViewHotelBookingDTO {
    private Long hotelBookingId;
    private int totalRoom;
    private RoomStatus statusId;
    private User user;
    private List<RoomCategories> roomCategoriesList;
    private BigDecimal totalPrice;
    private Date checkIn;
    private Date checkOut;

}
