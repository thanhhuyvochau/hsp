package fu.hbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fu.hbs.entities.New;
import fu.hbs.entities.User;
import fu.hbs.exceptionHandler.UserNotFoundException;
import fu.hbs.service.dao.NewsService;
import fu.hbs.service.dao.UserService;

@Controller
public class NewsController {

    private final NewsService newsService;
    private  UserService userService;
    public NewsController(NewsService newsService, UserService userService) {
        this.newsService = newsService;
        this.userService = userService;
    }

    @GetMapping("/news")
    public String getAllNews(Model model, @RequestParam(required = false) String newsTitle, @PageableDefault(size = 6) Pageable pageable) throws UserNotFoundException {
        Page<New> news;
        List<User> users = new ArrayList<>() ;
        User user = new User();
        if (newsTitle != null && !newsTitle.isEmpty()) {
            news = newsService.searchByTitle(newsTitle, pageable);
        } else {
            news = newsService.getAllNews(pageable);
        }        

        for (int i = 0; i < news.getContent().size(); i++) {
        	user = userService.findById(news.getContent().get(i).getUserId());
        	
		}
        model.addAttribute("user", user);
        model.addAttribute("news", news.getContent());
        model.addAttribute("newsTitle", newsTitle);
        model.addAttribute("currentPage", news.getNumber());
        model.addAttribute("totalPages", news.getTotalPages());

        return "news/listNews";
    }
}
