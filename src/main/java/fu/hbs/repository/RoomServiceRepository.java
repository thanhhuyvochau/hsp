/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 20/10/2023    1.0        HieuDT          First Deploy
 * 23/10/2023	 2.0		HieuDT			Update
 */
package fu.hbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fu.hbs.entities.RoomService;

@Repository
public interface RoomServiceRepository extends JpaRepository<RoomService, Long> {
	List<RoomService> findByServiceNameContaining(String serviceName);

	RoomService findByServiceId(Long serviceId);
}
