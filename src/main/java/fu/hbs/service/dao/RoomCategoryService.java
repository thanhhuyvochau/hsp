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

import fu.hbs.dto.RoomCategoryDTO.ViewRoomCategoryDTO;
import fu.hbs.entities.RoomCategories;

public interface RoomCategoryService {
    /**
     * Get a list of room categories with associated information.
     *
     * @return a list of RoomCategoryDTO objects containing details about room categories
     */
    List<ViewRoomCategoryDTO> getAllRoom();

    List<RoomCategories> findAvailableRoomCategories(int numberOfPeople);

    RoomCategories getRoomCategoryId(Long id);


    RoomCategories deleteByRoomCategoryId(Long id);

}
