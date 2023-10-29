/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 27/10/2023    1.0        HieuLBM          First Deploy
 * 29/10/2023	 2.0		HieuLBM			 edit getRoom
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
import fu.hbs.repository.CategoryRoomFurnitureRepository;
import fu.hbs.repository.RoomCategoriesRepository;
import fu.hbs.repository.RoomFurnitureRepository;
import fu.hbs.repository.RoomImageRepository;
import fu.hbs.repository.RoomRepository;
import fu.hbs.repository.RoomStatusRepository;
import fu.hbs.repository.ServiceRepository;
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
    @Autowired
    RoomCategoriesRepository roomCategoriesRepository;

    /**
     * Get information about rooms within a specific category.
     *
     * @param categoryId the category ID for which room information is requested
     * @return BookRoomByCategory object containing room details
     */
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
        BookRoomByCategory bookRoomByCategory = new BookRoomByCategory();
        bookRoomByCategory.setRoomCategories(roomCategoriesRepository.findByRoomCategoryId(categoryId));
        bookRoomByCategory.setCategoryRoomFurnitures(categoryRoomFurnitures);
        bookRoomByCategory.setImages(images);
        bookRoomByCategory.setServices(allService);
        bookRoomByCategory.setRooms(room);
        bookRoomByCategory.setRoomFurnitures(roomFurnitures);

        return bookRoomByCategory;
    }

}
