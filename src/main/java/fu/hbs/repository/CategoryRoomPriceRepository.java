package fu.hbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fu.hbs.entities.CategoryRoomPrice;

@Repository
public interface CategoryRoomPriceRepository extends JpaRepository<CategoryRoomPrice, Long> {
	CategoryRoomPrice findByRoomPriceId(Long id);
}
