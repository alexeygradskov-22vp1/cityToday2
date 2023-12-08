package com.example.citytoday.reps;

import com.example.citytoday.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> getNewsByStatus(String status);
}
