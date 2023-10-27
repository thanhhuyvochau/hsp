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
	/**
     * Get information about rooms within a specific category.
     *
     * @param categoryId the identifier of the room category
     * @return a BookRoomByCategory object containing details about rooms and related information
     */
	BookRoomByCategory getRoom(Long categoryId);
}
