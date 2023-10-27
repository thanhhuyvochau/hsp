/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 27/10/2023    1.0        HieuLBM          First Deploy
 *
 */
package fu.hbs.repository;

import fu.hbs.entities.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {


    @Query(value = "SELECT * FROM hotel_booking WHERE check_in BETWEEN ?1 AND ?2 AND check_out BETWEEN ?1 AND ?2", nativeQuery = true)
    List<HotelBooking> getAllRoom(Date checkIn, Date checkOut);


}
