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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
    List<HotelBooking> findByUserId(Long id);


    List<HotelBooking> findAllByUserId(Long id);

    List<HotelBooking> findByStatusId(Long status);
    List<HotelBooking> findByStatusIdAndValidBooking(Long status, Boolean validBooking);


    HotelBooking findByHotelBookingId(Long hotelBookingId);

    @Modifying
    @Transactional
    @Query("UPDATE HotelBooking hb SET hb.statusId = :statusId WHERE hb.hotelBookingId = :hotelBookingId")
    void updateStatus(@Param("statusId") Long statusId, @Param("hotelBookingId") Long hotelBookingId);

}
