/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 17/11/2023    1.0        HieuLBM          First Deploy
 */
package fu.hbs.repository;

import fu.hbs.entities.RoomStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomStatusHistoryRepository extends JpaRepository<RoomStatusHistory, Long> {

}
