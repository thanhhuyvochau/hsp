package com.example.repositoties;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entities.Role;

@Repository("roleReposity")
public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query("SELECT r FROM Role r JOIN r.userRoles ur WHERE ur.user.userId = ?1")
	List<Role> findRole(Long id);

}
