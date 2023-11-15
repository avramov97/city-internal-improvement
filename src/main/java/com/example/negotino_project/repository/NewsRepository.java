package com.example.negotino_project.repository;

import com.example.negotino_project.entities.News;
import com.example.negotino_project.entities.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer>
{
    List<News> findAllByApprovedFalse();
    List<News> findAllByApprovedTrue();
    List<News> findAllByTitleContains(String title);
}
