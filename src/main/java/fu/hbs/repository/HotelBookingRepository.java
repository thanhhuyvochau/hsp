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

import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.entities.HotelBooking;
import fu.hbs.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
    List<HotelBooking> findByUserId(Long id);

    @Query(value = "SELECT invoice_id, user_id, check_in, check_out, GROUP_CONCAT(DISTINCT room_category_id) AS room_categories\n" +
            "FROM hotel_booking\n" +
            "WHERE user_id = ?1\n" +
            "GROUP BY invoice_id, user_id, check_in, check_out", nativeQuery = true)
    List<HotelBooking> findByUserIdSameTime(Long id);

}
