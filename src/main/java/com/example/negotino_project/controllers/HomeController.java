package com.example.negotino_project.controllers;


import com.example.negotino_project.bussiness_logic.CoronaInfo;
import com.example.negotino_project.bussiness_logic.ImageEncoder;
import com.example.negotino_project.bussiness_logic.Username;
import com.example.negotino_project.entities.Corona.Country;
import com.example.negotino_project.entities.*;
import com.example.negotino_project.entities.Corona.Global;
import com.example.negotino_project.model.binding.UserRegisterBindingModel;
import com.example.negotino_project.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomeController
{
    private final NewsService newsService;
    private final EventService eventService;
    private final GoodThingsService goodThingsService;
    private final DonationService donationService;
    private final TalentService talentService;
    private final JSONObject getApiResponseBean;
    private final String defaultImageNews;
    private final String defaultImageGoodThing;
    private final String defaultImageEvent;
    private final String defaultImageDonation;
    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat sdfEvents = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
    private static final DateFormat sdfEventsDate = new SimpleDateFormat("dd.MM.yyyy");
    private static final DateFormat sdfEventsTime = new SimpleDateFormat("HH:mm");

    @Autowired
    public HomeController(GoodThingsService goodThingsService, NewsService newsService, EventService eventService, DonationService donationService, TalentService talentService, JSONObject getApiResponseBean, String defaultImageNews, String defaultImageGoodThing, String defaultImageEvent, String defaultImageDonation)
    {
        this.goodThingsService = goodThingsService;
        this.newsService = newsService;
        this.eventService = eventService;
        this.donationService = donationService;
        this.talentService = talentService;
        this.getApiResponseBean = getApiResponseBean;
        this.defaultImageNews = defaultImageNews;
        this.defaultImageGoodThing = defaultImageGoodThing;
        this.defaultImageEvent = defaultImageEvent;
        this.defaultImageDonation = defaultImageDonation;
    }

    @RequestMapping("/user")
    public Principal user(Principal principal)
    {
        return principal;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal Authentication authentication) throws IOException
    {
        String username = Username.get(authentication);
        model.addAttribute("username",username);

        ObjectMapper mapper = new ObjectMapper();
     //   JSONObject obj = CoronaInfo.getApiResponse(); --> this for live checking.
        Global global = CoronaInfo.getGlobalInfo(getApiResponseBean, mapper);
        List<Country> countries = CoronaInfo.getCountriesInfo(getApiResponseBean, mapper);
        Country mkd =  CoronaInfo.getMacedoniaInfo(getApiResponseBean, mapper);
        model.addAttribute("global",global);
        model.addAttribute("countries",countries);
        model.addAttribute("mkd",mkd);

        model.addAttribute("sdf", sdf);
        model.addAttribute("sdfEvents", sdfEvents);
        model.addAttribute("sdfEventsDate", sdfEventsDate);
        model.addAttribute("sdfEventsTime", sdfEventsTime);
        ImageEncoder imageEncoder = new ImageEncoder();
        model.addAttribute("defaultImageNews", defaultImageNews);
        model.addAttribute("defaultImageEvent", defaultImageEvent);
        model.addAttribute("defaultImageGoodThing", defaultImageGoodThing);
        model.addAttribute("defaultImageDonation", defaultImageDonation);
        model.addAttribute("defaultImageTalent", ImageEncoder.noImageTalent);
        model.addAttribute("imageEncoder", imageEncoder);

        List<News> approvedNews = this.newsService.getApprovedNews();
        model.addAttribute("approvedNews", approvedNews);
        LinkedHashMap<String, Long> topThreeAuthors = this.goodThingsService.getTopThreeAuthors();
        model.addAttribute("topThreeAuthors", topThreeAuthors);
        List<Event> top6ApprovedEvents = this.eventService.getTop6ApprovedEvents();
        model.addAttribute("top6ApprovedEvents", top6ApprovedEvents);
        List<GoodThing> top10ApprovedGoodThings = goodThingsService.getTop10ApprovedGoodThings();
        model.addAttribute("top10ApprovedGoodThings", top10ApprovedGoodThings);
        List<Donation> approvedDonations = donationService.getApprovedDonations();
        model.addAttribute("approvedDonations", approvedDonations);
        List<Talent> approvedTalents = this.talentService.getApprovedTalents();
        model.addAttribute("approvedTalents", approvedTalents);
        Event event = new Event();
        model.addAttribute("event", event);

        UserRegisterBindingModel user = new UserRegisterBindingModel();
        model.addAttribute("user",user);

        return "pages/index.html";
    }

    @GetMapping("/search")
    public String searchTitle(@RequestParam("searchTitle") String searchTitle, Model model, Authentication authentication) {
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
