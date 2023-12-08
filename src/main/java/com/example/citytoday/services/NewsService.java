package com.example.citytoday.services;

import com.example.citytoday.models.News;
import com.example.citytoday.reps.NewsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsService {
    private static final String APPROVED="Успешно";
    private static final String WAITING = "Ожидает";
    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> getApprovedNews() {
        List<News> news = newsRepository.getNewsByStatus(APPROVED);

        for (News el:news
             ) {
            el.getImage().setPath(el.getImage().getPath().substring(25));
        }
        return news;
    }

    public List<News> getWaitingNews() {
        return newsRepository.getNewsByStatus(WAITING);
    }

    public void create(News news) {
        news.setStatus(WAITING);
        news.setDateTime(String.valueOf(LocalDateTime.now()));
        newsRepository.save(news);
    }

    public void counterUpdate(Long id){
        News news = newsRepository.findById(id).orElse(null);
        news.setCount(news.getCount()==null?1:news.getCount()+1);
        newsRepository.save(news);
    }

    public News getById(Long id) {
        News news = newsRepository.findById(id).orElse(null);
        news.getImage().setPath(news.getImage().getPath().substring(25));
        return news;
    }

    public void approve(Long id){
       News replaced =  newsRepository.findById(id).orElse(null);
        assert replaced != null;
        replaced.setStatus(APPROVED);
        newsRepository.save(replaced);
    }

    public void delete(Long id){
        newsRepository.deleteById(id);
    }


}
