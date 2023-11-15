package com.example.negotino_project.controllers;

import com.example.negotino_project.bussiness_logic.ImageEncoder;
import com.example.negotino_project.bussiness_logic.Logs;
import com.example.negotino_project.bussiness_logic.Username;
import com.example.negotino_project.entities.GoodThing;
import com.example.negotino_project.entities.Log;
import com.example.negotino_project.entities.Operation;
import com.example.negotino_project.services.GoodThingsService;
import com.example.negotino_project.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/good-things")
public class GoodThingsController
{
    private final GoodThingsService goodThingsService;
    private final String defaultImageGoodThing;
    private final LogService logService;

    @Autowired
    public GoodThingsController(GoodThingsService goodThingsService, String defaultImageGoodThing, LogService logService)
    {
        this.goodThingsService = goodThingsService;
        this.defaultImageGoodThing = defaultImageGoodThing;
        this.logService = logService;
    }

    @GetMapping("")
    public String goodThings(Model model, Authentication authentication)
    {
        String username = Username.get(authentication);
        model.addAttribute("username",username);

        LinkedHashMap<String, Long> topThreeAuthors = this.goodThingsService.getTopThreeAuthors();
        model.addAttribute("topThreeAuthors", topThreeAuthors);

        model.addAttribute("defaultImageGoodThing", defaultImageGoodThing);
        ImageEncoder imageEncoder = new ImageEncoder();
        model.addAttribute("imageEncoder", imageEncoder);

        List<GoodThing> approvedGoodThings = goodThingsService.getApprovedGoodThings();
        model.addAttribute("approvedGoodThings", approvedGoodThings);

        if(authentication != null)
        {
            GoodThing goodThing = new GoodThing();
            model.addAttribute("goodThing", goodThing);
        }

//        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MODERATOR")))
        {
            List<GoodThing> requestedGoodThings = goodThingsService.getRequestedGoodThings();
            model.addAttribute("requestedGoodThings", requestedGoodThings);
        }


        return "pages/good-things.html";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String addGoodThing(@ModelAttribute GoodThing goodThing, @RequestParam("files") MultipartFile file, Model model, Authentication authentication) throws IOException
    {
        goodThing.setApproved(false);

        if(!file.isEmpty())
        {
            this.goodThingsService.addGoodThing(goodThing,file);
        }
        else
        {
            this.goodThingsService.addGoodThing(goodThing);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MODERATOR")))
        {
            List<GoodThing> requestedGoodThings = goodThingsService.getRequestedGoodThings();
            model.addAttribute("requestedGoodThings", requestedGoodThings);
            ImageEncoder imageEncoder = new ImageEncoder();
            model.addAttribute("defaultImageGoodThing", defaultImageGoodThing);
            model.addAttribute("imageEncoder", imageEncoder);
        }

        Log log = Logs.setupLog(authentication, "Good Things", Operation.Add);
        this.logService.insertLog(log);

        return "pages/good-things.html :: #req-goodthings";

    }

    @PostMapping("/approve")
    public String approveGoodThing(@RequestParam("goodThingApproveId") int id, Authentication authentication)
    {
        boolean approved = this.goodThingsService.approveGoodThing(id);

        Log log = Logs.setupLog(authentication, "Good Things", Operation.Approve);
        this.logService.insertLog(log);

        return "redirect:/good-things";
    }

    @PostMapping("/discard")
    public String discardGoodThing(@RequestParam("goodThingDiscardId") int id, Authentication authentication)
    {
        boolean discarded = this.goodThingsService.discardGoodThing(id);

        Log log = Logs.setupLog(authentication, "Good Things", Operation.Discard);
        this.logService.insertLog(log);

        return "redirect:/good-things";
    }

    @PostMapping("/discard-all")
    public String discardAllGoodThings(Authentication authentication)
    {
        boolean discardedAll = this.goodThingsService.discardAllRequestedGoodThings();

        Log log = Logs.setupLog(authentication, "Good Things", Operation.DiscardAll);
        this.logService.insertLog(log);

        return "redirect:/good-things";
    }

    @PostMapping("/approve-all")
    public String approveAllGoodThings(Authentication authentication)
    {
        boolean approvedAll = this.goodThingsService.approveAllRequestedGoodThings();

        Log log = Logs.setupLog(authentication, "Good Things", Operation.ApproveAll);
        this.logService.insertLog(log);

        return "redirect:/good-things";
    }

    @GetMapping("/single-good-thing/{id}")
    public String getSingleGoodThing(@PathVariable(name = "id") int id, Model model, Authentication authentication)
    {
        String username = Username.get(authentication);
        model.addAttribute("username",username);

        GoodThing goodThing = this.goodThingsService.getGoodThing(id);

        if(goodThing != null && goodThing.isApproved())
        {
            ImageEncoder imageEncoder = new ImageEncoder();
            model.addAttribute("defaultImageGoodThing", defaultImageGoodThing);
            model.addAttribute("imageEncoder", imageEncoder);
            model.addAttribute("goodThing", goodThing);
            List<GoodThing> top10ApprovedGoodThings = this.goodThingsService.getTop10ApprovedGoodThings();
            model.addAttribute("top10ApprovedGoodThings", top10ApprovedGoodThings);

            return "pages/single-good-thing.html";
        }
        else
        {
            return "error/400.html";
        }
    }
}
