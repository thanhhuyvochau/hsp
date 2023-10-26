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
package fu.hbs.service.dao;

import java.util.List;

import fu.hbs.dto.RoomCategoryDTO;

public interface RoomCategoryService {
	List<RoomCategoryDTO> getAllRoom();
}
