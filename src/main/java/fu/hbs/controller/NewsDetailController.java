package fu.hbs.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fu.hbs.entities.New;
import fu.hbs.service.dao.NewsService;

@Controller
public class NewsDetailController {

	private final NewsService newsService;

	public NewsDetailController(NewsService newsService) {
		this.newsService = newsService;
	}

	@GetMapping("/news-details")
	public String getNewsDetail(@RequestParam("newId") Long newsId, Model model) {
		// Truy vấn tin tức dựa trên newsId
		New news = newsService.findById(newsId);
		List<New> distinctNews = newsService.getAllNews().stream()
				.filter(newsService -> newsService.getNewId() != newsId).collect(Collectors.toList());

		// Đưa tin tức vào model để hiển thị trong trang news_detail.html
		model.addAttribute("news", news);
		model.addAttribute("distinctNews", distinctNews);
		return "news/newsDetailCustomer";
	}
}
