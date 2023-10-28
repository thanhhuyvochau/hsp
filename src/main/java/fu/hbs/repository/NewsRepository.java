package fu.hbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fu.hbs.entities.New;

public interface NewsRepository extends JpaRepository<New, Long> {
    List<New> findByTitleContaining(String newsTitle);
    List<New>  findByUserId(Long userId);
}
