/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 27/10/2023    1.0        HieuLBM          First Deploy
 */
package fu.hbs.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fu.hbs.entities.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByRoomCategoryId(Long roomCategoryId);

    @Query(value = "SELECT r.* FROM room r\n" +
            "LEFT JOIN room_categories rc ON r.room_category_id = rc.room_category_id\n" +
            "WHERE (r.status_id = 1 OR r.status_id = 2) AND rc.number_person >= ?3 AND r.room_id NOT IN (\n" +
            "SELECT brd.room_id FROM booking_room_details brd\n" +
            "INNER JOIN hotel_booking hb ON brd.hotel_booking_id = hb.hotel_booking_id\n" +
            "WHERE ( (hb.check_in BETWEEN ?1 AND ?2)\n" +
            " OR (hb.check_out BETWEEN ?1 AND ?2)));", nativeQuery = true)
    List<Room> getAllRoom(Date checkIn, Date checkOut, int numberPerson);

    @Query(value = "SELECT r.*\n" +
            "FROM room r\n" +
            "LEFT JOIN room_categories rc ON r.room_category_id = rc.room_category_id\n" +
            "WHERE r.room_category_id = ?1 AND  r.status_id = 1\n" +
            "AND r.room_id NOT IN (\n" +
            "SELECT brd.room_id\n" +
            "FROM booking_room_details brd\n" +
            "INNER JOIN hotel_booking hb ON brd.hotel_booking_id = hb.hotel_booking_id\n" +
            "WHERE ((hb.check_in BETWEEN ?2 AND ?3)\n" +
            "OR (hb.check_out BETWEEN ?2 AND ?3)));", nativeQuery = true)
    List<Room> findAvailableRoomsByCategoryId(Long id, LocalDate checkIn, LocalDate checkOut);

}
