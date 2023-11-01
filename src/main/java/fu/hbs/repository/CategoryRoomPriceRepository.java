package fu.hbs.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fu.hbs.entities.CategoryRoomPrice;

import java.util.List;

@Repository
public interface CategoryRoomPriceRepository extends JpaRepository<CategoryRoomPrice, Long> {
    CategoryRoomPrice findByRoomCategoryId(Long id);

    CategoryRoomPrice findByRoomPriceId(Long id);
//
//    @Query(value = "SELECT cr.* FROM category_room_price cr WHERE room_category_id = ?1 AND price_type = 'Weekday'", nativeQuery = true)
//    List<CategoryRoomPriceDTO> getAllCategoryRoomPrice(Long id);

}
