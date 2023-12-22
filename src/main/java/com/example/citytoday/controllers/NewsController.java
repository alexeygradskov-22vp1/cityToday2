package com.example.citytoday.controllers;

import com.example.citytoday.models.Image;
import com.example.citytoday.models.News;
import com.example.citytoday.services.ImageService;
import com.example.citytoday.services.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;




@Controller
public class NewsController {
    private final NewsService newsService;
    private final ImageService imageService;

    public NewsController(NewsService newsService, ImageService imageService) {
        this.newsService = newsService;
        this.imageService = imageService;
    }


    @GetMapping("/main")
    public String getMainPage(Model model) {
        model.addAttribute("news", newsService.getApprovedNews());
        model.addAttribute("bckgndPath", "/images/background.jpg");
        model.addAttribute("time", LocalDateTime.now().toString());
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "main";
    }

    @PostMapping("/news/write")
    public String write(@RequestParam("text") String text, @RequestParam("desc") String desc, @RequestParam("image") MultipartFile file) {
        News news = new News();
        news.setText(text);
        news.setDescription(desc);
        Long id = imageService.upload(file);
        Image image = new Image();
        image.setId(id);
        image.setPath(String.valueOf(id));
        news.setImage(image);
        newsService.create(news);
        return "redirect:/main";
    }

    @GetMapping("/news/write")
    public String addNews(Model model) {
        model.addAttribute("newsForm", new News());
        return "addNews";
    }

    @GetMapping("/news/{news_id}")
    public String getNewsPage(@PathVariable("news_id") Long newsID, Model model) {
        newsService.counterUpdate(newsID);
        News news = newsService.getById(newsID);
        model.addAttribute("news", news);

        return "news";
    }

    @PostMapping("/news/approve/{news_id}")
    public String approveNews(@PathVariable("news_id") Long id) {
        newsService.approve(id);
        return "redirect:/main";
    }

    @GetMapping("/moderate")
    public String getModeratePage(Model model) {
        List<News> news = newsService.getWaitingNews();
        model.addAttribute("news", news);
        return "moderate";
    }

    @PostMapping("/news/delete/{news_id}")
    public String deleteNews(@PathVariable("news_id") Long id) {
        newsService.delete(id);
        return "redirect:/moderate";
    }

    @RequestMapping("/time")
    @ResponseBody
    public String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM  yyyy HH:mm:ss")).toString();
    }
}
