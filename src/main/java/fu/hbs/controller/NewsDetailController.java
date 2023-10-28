package fu.hbs.controller;

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

        // Đưa tin tức vào model để hiển thị trong trang news_detail.html
        model.addAttribute("news", news);

        return "newsDetail";
    }
}
