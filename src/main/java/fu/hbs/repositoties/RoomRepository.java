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
package fu.hbs.repositoties;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fu.hbs.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

	List<Room> findByRoomCategoryId(Long roomCategoryId);
}
