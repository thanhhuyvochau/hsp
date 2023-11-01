package fu.hbs.service.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import fu.hbs.entities.New;

public interface NewsService {
    Page<New> searchByTitle(String newsTitle, Pageable pageable);

    Page<New> getAllNews(Pageable pageable);

    New findById(Long newsId);
    
    List<New> findByUserId(Long UserId);
    
    List<New> getAllNews();
}

