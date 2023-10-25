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
package fu.hbs.service.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fu.hbs.entities.Service;

public interface ServiceService {
//    List<com.example.entities.Service> getAllServices();

	Page<Service> searchByName(String serviceName, Pageable pageable);

	Page<Service> getAllServices(Pageable pageable);


//	List<com.example.entities.Service> searchByName(String serviceName);

}
