/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 04/10/2023    1.0        HieuLBM          First Deploy
 * 10/10/2023    2.0        HieuLBM          Fix notation, id filed
 * 22/10/2023	 2.1		HieuLBM			 change name
 * 2/11/2023     2.2        HieuLBM          Add attribute
 */

package fu.hbs.dto.HotelBookingDTO;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateHotelBookingDTO {
    private Long statusId;
    private String name;
    private String email;
    private String address;
    private String phone;
    private Date checkIn;
    private Date checkOut;
    private String notes;
    private Long paymentTypeId = 1L;
    private List<CreateHotelBookingDetailDTO> bookingDetails = new ArrayList<>();
}

