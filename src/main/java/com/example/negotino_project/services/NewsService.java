package com.example.negotino_project.services;


import com.example.negotino_project.entities.News;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NewsService
{
    News getNews(int id);
    void addNews(News news);
    void addNews(News news, MultipartFile file) throws IOException;
    List<News> getRequestedNews();
    List<News> getApprovedNews();
    List<News> getApprovedNewsFromStart();
    boolean approveNews(int id);
    boolean discardNews(int id);
    boolean approveAllRequestedNews();
    boolean discardAllRequestedNews();
    List<News> searchTitle(String title);
}
