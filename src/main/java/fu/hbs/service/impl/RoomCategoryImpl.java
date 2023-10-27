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
 * 
 */
package fu.hbs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fu.hbs.dto.RoomCategoryDTO;
import fu.hbs.entities.RoomCategories;
import fu.hbs.repository.CategoryRoomPriceRepository;
import fu.hbs.repository.RoomCategoriesRepository;
import fu.hbs.service.dao.RoomCategoryService;

@Service
public class RoomCategoryImpl implements RoomCategoryService {

	private RoomCategoriesRepository roomCategoriesRepository;
	private CategoryRoomPriceRepository categoryRoomPriceRepository;

	public RoomCategoryImpl(RoomCategoriesRepository roomCategoriesRepository,
			CategoryRoomPriceRepository categoryRoomPriceRepository) {
		this.categoryRoomPriceRepository = categoryRoomPriceRepository;
		this.roomCategoriesRepository = roomCategoriesRepository;
	}

	/**
	 * Get a list of all room categories along with their details.
	 *
	 * @return List of RoomCategoryDTO containing room category information
	 */

	@Override
	public List<RoomCategoryDTO> getAllRoom() {
		List<RoomCategories> roomCategories = roomCategoriesRepository.findAll();
		List<RoomCategoryDTO> roomCategoryDTOS = new ArrayList<>();

		for (RoomCategories roomCategory : roomCategories) {
			RoomCategoryDTO roomCategoryDTO = new RoomCategoryDTO();
			roomCategoryDTO.setRoomCategoryId(roomCategory.getRoomCategoryId());
			roomCategoryDTO.setRoomCategoryName(roomCategory.getRoomCategoryName());
			roomCategoryDTO.setDescription(roomCategory.getDescription());
			roomCategoryDTO.setCategoryRoomPrice(
					categoryRoomPriceRepository.findByRoomPriceId(roomCategory.getRoomCategoryId()));
			roomCategoryDTO.setImage(roomCategory.getImage());
			roomCategoryDTOS.add(roomCategoryDTO);
		}

		return roomCategoryDTOS;
	}

}
