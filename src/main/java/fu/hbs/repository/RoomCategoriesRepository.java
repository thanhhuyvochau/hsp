package fu.hbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fu.hbs.entities.RoomCategories;

@Repository
public interface RoomCategoriesRepository extends JpaRepository<RoomCategories, Long> {

	RoomCategories findByRoomCategoryId(Long id);

	List<RoomCategories> findByNumberPersonGreaterThanEqual(int numberOfPeople);

}
