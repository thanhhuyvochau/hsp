/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 04/10/2023    1.0        HieuLBM          First Deploy
 * 
 * 
 */
package com.example.repositoties;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entities.Role;

@Repository("roleReposity")
public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query(value = "SELECT r.*  FROM backup_hotel.role r JOIN backup_hotel.user_role ur"
			+ " on r.role_id = ur.role_id where ur.user_id=?1", nativeQuery = true)
	List<Role> findRole(Long id);
}