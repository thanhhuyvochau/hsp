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
package fu.hbs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fu.hbs.dto.BookRoomByCategory;
import fu.hbs.entities.CategoryRoomFurniture;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomCategories;
import fu.hbs.entities.RoomFurniture;
import fu.hbs.entities.RoomImage;
import fu.hbs.entities.RoomService;
import fu.hbs.repositoties.CategoryRoomFurnitureRepository;
import fu.hbs.repositoties.RoomCategoriesRepository;
import fu.hbs.repositoties.RoomFurnitureRepository;
import fu.hbs.repositoties.RoomImageRepository;
import fu.hbs.repositoties.RoomRepository;
import fu.hbs.repositoties.RoomStatusRepository;
import fu.hbs.repositoties.ServiceRepository;
import fu.hbs.service.dao.RoomByCategoryService;

@Service
public class RoomImpl implements RoomByCategoryService {
	@Autowired
	RoomCategoriesRepository categoriesRepository;
	@Autowired
	RoomStatusRepository statusRepository;

	@Autowired
	ServiceRepository serviceRepository;

	@Autowired
	RoomImageRepository roomImageRepository;

	@Autowired
	RoomStatusRepository roomStatusRepository;
	@Autowired
	RoomRepository roomRepository;

	@Autowired
	RoomFurnitureRepository roomFurnitureRepository;
	@Autowired
	CategoryRoomFurnitureRepository categoryRoomFurnitureRepository;

	@Override
	public BookRoomByCategory getRoom(Long categoryId) {
		RoomCategories roomCategories = new RoomCategories();
		List<Room> room = roomRepository.findByRoomCategoryId(categoryId);
		List<CategoryRoomFurniture> categoryRoomFurnitures = categoryRoomFurnitureRepository
				.findByRoomCategoryId(categoryId);
		List<RoomService> allService = serviceRepository.findAll();
		RoomImage roomImage = new RoomImage();
		List<RoomImage> images = new ArrayList<>();

		for (int i = 0; i < room.size(); i++) {
			roomImage = roomImageRepository.findByRoomImageId(room.get(i).getRoomImageId());
			images.add(roomImage);
		}

		List<RoomFurniture> roomFurnitures = new ArrayList<>();
		RoomFurniture roomFurniture = new RoomFurniture();
		for (int i = 0; i < categoryRoomFurnitures.size(); i++) {
			roomFurniture = roomFurnitureRepository.findByFurnitureId(categoryRoomFurnitures.get(i).getFurnitureId());
			roomFurnitures.add((roomFurniture));
		}
		System.out.println(roomFurnitures);
		BookRoomByCategory bookRoomByCategory = new BookRoomByCategory();
		bookRoomByCategory.setCategoryRoomFurnitures(categoryRoomFurnitures);
		bookRoomByCategory.setImages(images);
		bookRoomByCategory.setServices(allService);
		bookRoomByCategory.setRooms(room);
		bookRoomByCategory.setRoomFurnitures(roomFurnitures);

		return bookRoomByCategory;
	}

}
