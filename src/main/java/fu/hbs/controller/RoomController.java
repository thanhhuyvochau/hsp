/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 26/10/2023    1.0        HieuLBM          First Deploy		
 */

package fu.hbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fu.hbs.dto.RoomCategoryDTO;
import fu.hbs.service.dao.RoomCategoryService;

@Controller
public class RoomController {
	@Autowired
	private RoomCategoryService roomCategoryService;

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
}
