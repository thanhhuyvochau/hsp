package fu.hbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fu.hbs.entities.RoomCategories;

import java.util.List;

@Repository("roomCategoriesRepository")
public interface RoomCategoriesRepository extends JpaRepository<RoomCategories, Long> {

    RoomCategories findByRoomCategoryId(Long id);

    List<RoomCategories> findByNumberPersonGreaterThanEqual(int numberOfPeople);


}
