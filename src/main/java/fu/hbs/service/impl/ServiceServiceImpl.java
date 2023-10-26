/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 20/10/2023    1.0        HieuDT          First Deploy
 * 23/10/2023	 2.0		HieuDT			Update
 */
package fu.hbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import fu.hbs.entities.RoomService;
import fu.hbs.repositoties.ServiceRepository;
import fu.hbs.service.dao.ServiceService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServiceServiceImpl implements ServiceService {


	@Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Page<RoomService> getAllServices(Pageable pageable) {
        List<RoomService> allServices = serviceRepository.findAll(); // Lấy toàn bộ dịch vụ
        return paginateList(allServices, pageable); // Phân trang danh sách toàn bộ dịch vụ
    }

    @Override
    public Page<RoomService> searchByName(String serviceName, Pageable pageable) {
        List<RoomService> filteredServices = serviceRepository.findByServiceNameContaining(serviceName); // Tìm kiếm trong danh sách dịch vụ
        return paginateList(filteredServices, pageable); // Phân trang kết quả tìm kiếm
    }

    // Phân trang danh sách
    private Page<RoomService> paginateList(List<RoomService> list, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<RoomService> pageList;

        if (list.size() < startItem) {
            pageList = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            pageList = list.subList(startItem, toIndex);
        }

        return new PageImpl<>(pageList, pageable, list.size());
    }



}
