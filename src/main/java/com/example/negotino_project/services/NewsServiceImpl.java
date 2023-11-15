package com.example.negotino_project.services;

import com.example.negotino_project.entities.News;
import com.example.negotino_project.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService
{
    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository)
    {
        this.newsRepository = newsRepository;
    }

    @Override
    public News getNews(int id)
    {
        return this.newsRepository.findById(id).orElse(null);
    }

    @Override
    public void addNews(News news)
    {
        this.newsRepository.save(news);
    }

    @Override
    public void addNews(News news, MultipartFile file) throws IOException
    {
        String imgName = file.getOriginalFilename();
        news.setImgName(imgName);
        news.setImgType(file.getContentType());

        news.setImgData(file.getBytes());

        this.newsRepository.save(news);
    }

    @Override
    public List<News> getRequestedNews()
    {
        List<News> requestedNews = this.newsRepository.findAllByApprovedFalse();
        Collections.reverse(requestedNews);

        return requestedNews;
    }

    @Override
    public List<News> getApprovedNews()
    {
        List<News> approvedNews = this.newsRepository.findAllByApprovedTrue();
        Collections.reverse(approvedNews);

        return approvedNews;
    }

    @Override
    public List<News> getApprovedNewsFromStart()
    {
        List<News> approvedNewsFromStart = this.newsRepository.findAllByApprovedTrue();

        return approvedNewsFromStart;
    }

    @Override
    public boolean approveNews(int id)
    {
        Optional<News> getRequestedNews = this.newsRepository.findById(id);

        if(getRequestedNews.isPresent())
        {
            getRequestedNews.get().setApproved(true);
            this.newsRepository.save(getRequestedNews.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean discardNews(int id)
    {
        Optional<News> getRequestedNews = this.newsRepository.findById(id);

        if(getRequestedNews.isPresent())
        {
            this.newsRepository.delete(getRequestedNews.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean approveAllRequestedNews()
    {
        List<News> getRequestedNews = this.newsRepository.findAllByApprovedFalse();

        if(getRequestedNews != null)
        {
            for(News news : getRequestedNews)
            {
                news.setApproved(true);
                this.newsRepository.save(news);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean discardAllRequestedNews()
    {
        List<News> getRequestedNews = this.newsRepository.findAllByApprovedFalse();

        if(getRequestedNews != null)
        {
            for(News news : getRequestedNews)
            {
                this.newsRepository.delete(news);
            }

            return true;
        }

        return false;
    }

    @Override
    public List<News> searchTitle(String title)
    {
        return this.newsRepository.findAllByTitleContains(title);
    }
}
