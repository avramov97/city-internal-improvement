package com.example.negotino_project.controllers;

import com.example.negotino_project.bussiness_logic.ImageEncoder;
import com.example.negotino_project.bussiness_logic.Logs;
import com.example.negotino_project.bussiness_logic.Username;
import com.example.negotino_project.entities.*;
import com.example.negotino_project.services.EventService;
import com.example.negotino_project.services.GoodThingsService;
import com.example.negotino_project.services.LogService;
import com.example.negotino_project.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController
{
    private final NewsService newsService;
    private final LogService logService;
    private final EventService eventService;
    private final GoodThingsService goodThingsService;
    private final String defaultImageNews;
    private final String defaultImageEvent;
    private final String defaultImageGoodThing;
    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat sdf2 = new SimpleDateFormat("HH:mm");

    @Autowired
    public NewsController(NewsService newsService, LogService logService, EventService eventService, GoodThingsService goodThingsService, String defaultImageNews, String defaultImageEvent, String defaultImageGoodThing)
    {
        this.newsService = newsService;
        this.logService = logService;
        this.eventService = eventService;
        this.goodThingsService = goodThingsService;
        this.defaultImageNews = defaultImageNews;
        this.defaultImageEvent = defaultImageEvent;
        this.defaultImageGoodThing = defaultImageGoodThing;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String news(Model model, Authentication authentication)
    {
        model.addAttribute("username",Username.get(authentication));

        model.addAttribute("sdf", sdf);
        ImageEncoder imageEncoder = new ImageEncoder();
        model.addAttribute("defaultImageNews", defaultImageNews);
        model.addAttribute("defaultImageEvent", defaultImageEvent);
        model.addAttribute("imageEncoder", imageEncoder);

        List<News> approvedNews = this.newsService.getApprovedNews();
        model.addAttribute("approvedNews", approvedNews);
        List<Event> top6ApprovedEvents = this.eventService.getTop6ApprovedEvents();
        model.addAttribute("top6ApprovedEvents", top6ApprovedEvents);
        List<GoodThing> top10ApprovedGoodThings = goodThingsService.getTop10ApprovedGoodThings();
        model.addAttribute("top10ApprovedGoodThings", top10ApprovedGoodThings);

//      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MODERATOR")))
        {
            List<News> requestedNews = this.newsService.getRequestedNews();
            model.addAttribute("requestedNews", requestedNews);
        }

        return "pages/news.html";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String addNewsGet(Model model, Authentication authentication)
    {
        model.addAttribute("username", Username.get(authentication));

        model.addAttribute("sdf", sdf);
        ImageEncoder imageEncoder = new ImageEncoder();
        model.addAttribute("defaultImageNews", defaultImageNews);
        model.addAttribute("imageEncoder", imageEncoder);

        List<News> approvedNews = this.newsService.getApprovedNews();
        model.addAttribute("approvedNews", approvedNews);
        News news = new News();
        model.addAttribute("news",news);

        return "pages/add-news.html";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String addNewsPost(@ModelAttribute News news, @RequestParam("files") MultipartFile file, Model model, Authentication authentication) throws IOException
    {
        news.setApproved(false);
        news.setAuthor(Username.get(authentication));
        Date date = new Date();
        news.setDate(date);
        if(!file.isEmpty())
        {
            this.newsService.addNews(news,file);
        }
        else
        {
            this.newsService.addNews(news);
        }

       ImageEncoder imageEncoder = new ImageEncoder();
       model.addAttribute("defaultImageTalent", ImageEncoder.noImageTalent);
       model.addAttribute("imageEncoder", imageEncoder);

        Log log = Logs.setupLog(authentication, "News", Operation.Add);
        this.logService.insertLog(log);

        return "pages/add-news.html :: #noid ";
//        return "redirect:/news/add";
    }


    @PostMapping("/approve")
    public String approveNews(@RequestParam("newsApproveId") int id, Authentication authentication)
    {
        boolean approved = this.newsService.approveNews(id);

        Log log = Logs.setupLog(authentication, "News", Operation.Approve);
        this.logService.insertLog(log);

        return "redirect:/news";
    }

    @PostMapping("/discard")
    public String discardNews(@RequestParam("newsDiscardId") int id, Authentication authentication)
    {
        boolean discarded = this.newsService.discardNews(id);

        Log log = Logs.setupLog(authentication, "News", Operation.Discard);
        this.logService.insertLog(log);

        return "redirect:/news";
    }

    @PostMapping("/discard-all")
    public String discardAllNews(Authentication authentication)
    {
        boolean discarded = this.newsService.discardAllRequestedNews();

        Log log = Logs.setupLog(authentication, "News", Operation.DiscardAll);
        this.logService.insertLog(log);

        return "redirect:/news";
    }

    @PostMapping("/approve-all")
    public String approveAllNews(Authentication authentication)
    {
        boolean discarded = this.newsService.approveAllRequestedNews();

        Log log = Logs.setupLog(authentication, "News", Operation.ApproveAll);
        this.logService.insertLog(log);

        return "redirect:/news";
    }

    @GetMapping("/single-post/{id}")
    public String getSinglePost(@PathVariable(name = "id") int id, Model model, Authentication authentication)
    {
        model.addAttribute("username", Username.get(authentication));

        News news = this.newsService.getNews(id);

        if(news != null && news.isApproved())
        {
            ImageEncoder imageEncoder = new ImageEncoder();
            model.addAttribute("sdf", sdf);
            model.addAttribute("defaultImageNews", defaultImageNews);
            model.addAttribute("imageEncoder", imageEncoder);
            model.addAttribute("news",news);
            List<News> approvedNews = this.newsService.getApprovedNews();
            model.addAttribute("approvedNews", approvedNews);

            return "pages/single-post.html";
        }
        else
        {
            return "error/400.html";
        }
    }

    @GetMapping("/search")
    public String getSinglePost(@RequestParam("searchTitle") String searchTitle, Model model, Authentication authentication)
    {
        model.addAttribute("username", Username.get(authentication));

        model.addAttribute("searchTitle", searchTitle);
        List<News> foundNews = this.newsService.searchTitle(searchTitle);

        model.addAttribute("foundNews", foundNews);
        ImageEncoder imageEncoder = new ImageEncoder();
        model.addAttribute("sdf", sdf);
        model.addAttribute("defaultImageNews", defaultImageNews);
        model.addAttribute("imageEncoder", imageEncoder);
        List<News> approvedNews = this.newsService.getApprovedNews();
        model.addAttribute("approvedNews", approvedNews);

        return "pages/search-news.html";
    }

}
