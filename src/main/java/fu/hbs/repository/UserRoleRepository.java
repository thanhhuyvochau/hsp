package fu.hbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fu.hbs.entities.UserRole;

@Repository("userroleReposity")
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	UserRole save(UserRole userRole);
}
