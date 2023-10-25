/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 23/10/2023    1.0        HieuDT          First Deploy
 *  * 
 */
//package fu.hbs.controller;
//
//import jakarta.servlet.http.HttpSession;
//
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import fu.hbs.entities.Service;
//import fu.hbs.service.dao.ServiceService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//public class ServiceController {
//
//	private final ServiceService serviceService;
//
//	public ServiceController(ServiceService serviceService) {
//		this.serviceService = serviceService;
//	}
//
//	///////////////
//	@GetMapping("/services")
//	public String getAllServices(Model model, @RequestParam(required = false) String serviceName,
//			@PageableDefault(size = 4) Pageable pageable) {
//		Page<Service> services;
//
//		if (serviceName != null && !serviceName.isEmpty()) {
//			// Thực hiện tìm kiếm dịch vụ theo tên và lưu kết quả vào danh sách "services"
//			services = serviceService.searchByName(serviceName, pageable);
//		} else {
//			// Nếu không có tên dịch vụ, lấy toàn bộ dịch vụ và phân trang
//			services = serviceService.getAllServices(pageable);
//		}
//
//		model.addAttribute("services", services.getContent());
//		model.addAttribute("serviceName", serviceName);
//		model.addAttribute("currentPage", services.getNumber());
//		model.addAttribute("totalPages", services.getTotalPages());
//		return "listService";
//	}
//
//}
package fu.hbs.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import fu.hbs.entities.Service;
import fu.hbs.service.dao.ServiceService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/services")
    public String getAllServices(Model model, @RequestParam(required = false) String serviceName, @PageableDefault(size = 4) Pageable pageable) {
        Page<Service> services;

        if (serviceName != null && !serviceName.isEmpty()) {
            services = serviceService.searchByName(serviceName, pageable);
        } else {
            services = serviceService.getAllServices(pageable);
        }

        model.addAttribute("services", services.getContent());
        model.addAttribute("serviceName", serviceName);
        model.addAttribute("currentPage", services.getNumber());
        model.addAttribute("totalPages", services.getTotalPages());

        return "listService";
    }
}
