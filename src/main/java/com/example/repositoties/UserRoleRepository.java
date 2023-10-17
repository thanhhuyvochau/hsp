package com.example.repositoties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.UserRole;

@Repository("userroleReposity")
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	UserRole save(UserRole userRole);
}
