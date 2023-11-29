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

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateHotelBookingDTO {
    private Long statusId;
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String address;
    @NotEmpty
    @Pattern(regexp = "/(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b/", message = "Nhập đúng định dạng số điện thoại Việt Nam")
    private String phone;
    private LocalDate checkIn = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toLocalDate();
    private LocalDate checkOut =  LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toLocalDate().plusDays(1);
    private String notes;
    private Long paymentTypeId = 1L;
    private List<CreateHotelBookingDetailDTO> bookingDetails = new ArrayList<>();
    private boolean payFull = true;
}

