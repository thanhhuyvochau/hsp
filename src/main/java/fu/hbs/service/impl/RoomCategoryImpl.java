package fu.hbs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fu.hbs.dto.RoomCategoryDTO;
import fu.hbs.entities.RoomCategories;
import fu.hbs.repositoties.CategoryRoomPriceRepository;
import fu.hbs.repositoties.RoomCategoriesRepository;
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
