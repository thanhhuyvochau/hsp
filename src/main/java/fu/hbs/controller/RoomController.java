/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 26/10/2023    1.0        HieuLBM          First Deploy	
 * 27/10/2023	 2.0	    HieuLBM			 update category		
 */

package fu.hbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fu.hbs.dto.BookRoomByCategory;
import fu.hbs.dto.RoomCategoryDTO;
import fu.hbs.service.dao.RoomByCategoryService;
import fu.hbs.service.dao.RoomCategoryService;

@Controller
public class RoomController {
	@Autowired
	private RoomCategoryService roomCategoryService;
	@Autowired
	private RoomByCategoryService roomByCategoryService;

	@GetMapping("/room/all")
	public String getRoomCate(Model model) {
		List<RoomCategoryDTO> categories = roomCategoryService.getAllRoom();
		if (categories != null) {
			model.addAttribute("categories", categories);
			return "viewRoom_customer";
		}
		model.addAttribute("NotFound", "Không có loại phòng mà bạn cần tìm");
		return "viewRoom_customer";

	}

	@GetMapping("/room/category/{categoryId}")
	public String getRoomByCategory(Model model, @PathVariable Long categoryId) {
		BookRoomByCategory bookRoomByCategories = roomByCategoryService.getRoom(categoryId);
		model.addAttribute("bookRoomByCategories", bookRoomByCategories);
		return "viewRoom";

	}

}
