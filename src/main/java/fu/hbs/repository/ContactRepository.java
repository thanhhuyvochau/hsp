package fu.hbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fu.hbs.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	Contact findByEmail(String email);

	Long findUserIdByEmail(String email);
}
