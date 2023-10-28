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

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fu.hbs.entities.Room;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByRoomCategoryId(Long roomCategoryId);


    @Query(value = "SELECT r.*\n" +
            "FROM room r \n" +
            "LEFT JOIN room_categories rc ON rc.room_category_id = r.room_category_id\n" +
            "LEFT JOIN hotel_booking br ON r.room_id = br.room_id\n" +
            "   AND (br.check_in BETWEEN ?1 AND ?2\n" +
            "	AND br.check_out BETWEEN ?1 AND ?2 ) \n" +
            "WHERE rc.number_person >= ?3\n" +
            "   AND br.room_id IS NULL", nativeQuery = true)
    List<Room> getAllRoom(Date checkIn, Date checkOut, int numberPerson);
}
