
/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 11/11/2023    1.0        HieuLBM          First Deploy
 *
 *
 */
package fu.hbs.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "hotel_booking_service")
public class HotelBookingService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_booking_service_id")
    private Long hotelBookingServiceId;
    private Long hotelBookingId;
    private Long roomId;
    private Long serviceId;
    private Instant createDate;

}
