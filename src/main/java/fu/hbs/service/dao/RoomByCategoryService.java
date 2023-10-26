/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 27/10/2023	 2.0	    HieuLBM			 Firt Deploy		
 */
package fu.hbs.service.dao;

import fu.hbs.dto.BookRoomByCategory;

public interface RoomByCategoryService {
	BookRoomByCategory getRoom(Long categoryId);
}
