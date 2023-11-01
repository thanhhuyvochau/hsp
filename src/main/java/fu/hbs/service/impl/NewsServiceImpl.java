package fu.hbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import fu.hbs.entities.New;
import fu.hbs.repository.NewsRepository;
import fu.hbs.service.dao.NewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public Page<New> getAllNews(Pageable pageable) {
    	List<New> news = new ArrayList<>();
        List<New> allNews = newsRepository.findAll(); // Lấy toàn bộ tin tức
        for (int i = 0; i < allNews.size(); i++) {
        	news = newsRepository.findByUserId(allNews.get(i).getUserId());
		};
        return paginateList(allNews, pageable); // Phân trang danh sách toàn bộ tin tức
    }

    @Override
    public Page<New> searchByTitle(String newsTitle, Pageable pageable) {
        List<New> filteredNews = newsRepository.findByTitleContaining(newsTitle); // Tìm kiếm trong danh sách tin tức
        return paginateList(filteredNews, pageable); // Phân trang kết quả tìm kiếm
    }

    // Phân trang danh sách
    private Page<New> paginateList(List<New> list, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<New> pageList;

        if (list.size() < startItem) {
            pageList = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            pageList = list.subList(startItem, toIndex);
        }

        return new PageImpl<>(pageList, pageable, list.size());
    }

    @Override
    public New findById(Long newsId) {
        Optional<New> news = newsRepository.findById(newsId);
        return news.orElse(null);
    }

	@Override
	public List<New> findByUserId(Long UserId) {
		List<New> news = (List<New>) newsRepository.findByUserId(UserId);
		return news;
	}
	
    @Override
    public List<New> getAllNews() {
        return newsRepository.findAll();
    }
}

