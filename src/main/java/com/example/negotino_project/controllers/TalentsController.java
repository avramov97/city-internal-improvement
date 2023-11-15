package com.example.negotino_project.controllers;

import com.example.negotino_project.bussiness_logic.ImageEncoder;
import com.example.negotino_project.bussiness_logic.Logs;
import com.example.negotino_project.bussiness_logic.Username;
import com.example.negotino_project.entities.Log;
import com.example.negotino_project.entities.Operation;
import com.example.negotino_project.entities.Talent;
import com.example.negotino_project.services.LogService;
import com.example.negotino_project.services.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/talents")
public class TalentsController
{
    private final TalentService talentService;
    private final LogService logService;
//    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads/talents";

    @Autowired
    public TalentsController(TalentService talentService, LogService logService)
    {
        this.talentService = talentService;
        this.logService = logService;
    }

    @GetMapping("")
    public String talents(Model model, Authentication authentication)
    {
        model.addAttribute("username", Username.get(authentication));

        ImageEncoder imageEncoder = new ImageEncoder();
        model.addAttribute("defaultImageTalent", ImageEncoder.noImageTalent);
        model.addAttribute("imageEncoder", imageEncoder);

        List<Talent> approvedTalents = this.talentService.getApprovedTalents();
        model.addAttribute("approvedTalents", approvedTalents);

        if(authentication != null)
        {
            Talent talent = new Talent();
            model.addAttribute("talent", talent);
        }

        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MODERATOR")))
        {
            List<Talent> requestedTalents = this.talentService.getRequestedTalents();
            model.addAttribute("requestedTalents", requestedTalents);
        }

        return "pages/talents.html";
    }

    //    ResponseEntity<String>
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String addTalent(@ModelAttribute Talent talent, Model model, @RequestParam("files") MultipartFile file, Authentication authentication) throws IOException
    {
        talent.setApproved(false);

        if(!file.isEmpty())
        {
            this.talentService.addTalent(talent,file);
        }
        else
        {
            this.talentService.addTalent(talent);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MODERATOR")))
        {
            List<Talent> requestedTalents = this.talentService.getRequestedTalents();
            model.addAttribute("requestedTalents", requestedTalents);
            ImageEncoder imageEncoder = new ImageEncoder();
            model.addAttribute("defaultImageTalent", ImageEncoder.noImageTalent);
            model.addAttribute("imageEncoder", imageEncoder);
        }

        Log log = Logs.setupLog(authentication, "Talents", Operation.Add);
        this.logService.insertLog(log);

        return "pages/talents.html :: #req-talents";
//        return ResponseEntity.ok("requestedTalents");
//        return "redirect:/talents";
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String approveTalent(@RequestParam("talentApproveId") int id, Authentication authentication)
    {
        boolean approved = this.talentService.approveTalent(id);

        Log log = Logs.setupLog(authentication, "Talents", Operation.Approve);
        this.logService.insertLog(log);

        return "redirect:/talents";
    }

    @PostMapping("/discard")    // another method for approved talents should be added
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String discardTalent(@RequestParam("talentDiscardId") int id, Authentication authentication)
    {
        boolean discarded = this.talentService.discardTalent(id);

        Log log = Logs.setupLog(authentication, "Talents", Operation.Discard);
        this.logService.insertLog(log);

        return "redirect:/talents";
    }

    @PostMapping("/discard-all")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String discardAllTalents(Authentication authentication)
    {
        boolean discarded = this.talentService.discardAllTalents();

        Log log = Logs.setupLog(authentication, "Talents", Operation.DiscardAll);
        this.logService.insertLog(log);

        return "redirect:/talents";
    }

    @PostMapping("/approve-all")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String approveAllTalents(Authentication authentication)
    {
        boolean discarded = this.talentService.approveAllTalents();

        Log log = Logs.setupLog(authentication, "Talents", Operation.ApproveAll);
        this.logService.insertLog(log);

        return "redirect:/talents";
    }

    @GetMapping("/single-talent/{id}")
    public String getSingleTalent(@PathVariable(name = "id") int id, Model model, Authentication authentication)
    {
        model.addAttribute("username", Username.get(authentication));
        Talent talent = this.talentService.getTalent(id);

        if(talent != null && talent.isApproved())
        {
            ImageEncoder imageEncoder = new ImageEncoder();
            model.addAttribute("defaultImageTalent", ImageEncoder.noImageTalent);
            model.addAttribute("imageEncoder", imageEncoder);
            model.addAttribute("talent", talent);
            List<Talent> top10ApprovedTalents = this.talentService.getTop10ApprovedTalents();
            model.addAttribute("top10ApprovedTalents", top10ApprovedTalents);

            return "pages/single-talent.html";
        }
        else
        {
            return "error/400.html";
        }
    }

}
