package com.example.negotino_project.controllers;

import com.example.negotino_project.bussiness_logic.ImageEncoder;
import com.example.negotino_project.bussiness_logic.Logs;
import com.example.negotino_project.bussiness_logic.Username;
import com.example.negotino_project.entities.Donation;
import com.example.negotino_project.entities.Log;
import com.example.negotino_project.entities.Operation;
import com.example.negotino_project.services.DonationService;
import com.example.negotino_project.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/donations")
public class DonationsController {
    private final DonationService donationService;
    private final String defaultImageDonation;
    private final LogService logService;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Autowired
    public DonationsController(DonationService donationService, String defaultImageDonation, LogService logService)
    {
        this.donationService = donationService;
        this.defaultImageDonation = defaultImageDonation;
        this.logService = logService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String donations(Authentication authentication, Model model) throws IOException
    {
        String username = Username.get(authentication);
        model.addAttribute("username",username);

        ImageEncoder imageEncoder = new ImageEncoder();
        model.addAttribute("defaultImageDonation", defaultImageDonation);
        model.addAttribute("imageEncoder", imageEncoder);

        List<Donation> approvedDonations = donationService.getApprovedDonations();
        model.addAttribute("approvedDonations", approvedDonations);

        if(authentication != null)
        {
            Donation donation = new Donation();
            model.addAttribute("donation", donation);
        }

     //   @AuthenticationPricipal new feature same as line 57
//        authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MODERATOR")))
        {
            List<Donation> requestedDonations = donationService.getRequestedDonations();
            model.addAttribute("requestedDonations", requestedDonations);
        }

        return "pages/donations.html";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String addTalent(@ModelAttribute Donation donation, @RequestParam("files") MultipartFile file, Model model, Authentication authentication) throws IOException
    {
        donation.setApproved(false);
        donation.setAuthor(Username.get(authentication));

        if(!file.isEmpty())
        {
            this.donationService.addDonation(donation,file);
        }
        else
        {
            this.donationService.addDonation(donation);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MODERATOR")))
        {
            List<Donation> requestedDonations = this.donationService.getRequestedDonations();
            model.addAttribute("requestedDonations", requestedDonations);
            ImageEncoder imageEncoder = new ImageEncoder();
            model.addAttribute("defaultImageDonation", defaultImageDonation);
            model.addAttribute("imageEncoder", imageEncoder);
        }

        Log log = Logs.setupLog(authentication, "Donations", Operation.Add);
        this.logService.insertLog(log);

        return "pages/donations.html :: #req-donations";
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String approveDonation(@RequestParam("donationApproveId") int id, Authentication authentication)
    {
        boolean approved = this.donationService.approveDonation(id);

        Log log = Logs.setupLog(authentication, "Donations", Operation.Approve);
        this.logService.insertLog(log);

        return "redirect:/donations";
    }

    @PostMapping("/discard")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String discardDonation(@RequestParam("donationDiscardId") int id, Authentication authentication)
    {
        boolean discarded = this.donationService.discardDonation(id);

        Log log = Logs.setupLog(authentication, "Donations", Operation.Discard);
        this.logService.insertLog(log);


        return "redirect:/donations";
    }

    @PostMapping("/discard-all")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String discardAllDonations(Authentication authentication)
    {
        boolean discardedAll = this.donationService.discardAllDonations();

        Log log = Logs.setupLog(authentication, "Donations", Operation.DiscardAll);
        this.logService.insertLog(log);

        return "redirect:/donations";
    }

    @PostMapping("/approve-all")
    public String approveAllDonations(Authentication authentication)
    {
        boolean approvedAll = this.donationService.approveAllDonations();
        Log log = Logs.setupLog(authentication, "Donations", Operation.ApproveAll);
        this.logService.insertLog(log);

        return "redirect:/donations";
    }

    @GetMapping("/single-donation/{id}")
    public String getSingleDonation(@PathVariable(name = "id") int id, Model model, Authentication authentication)
    {
        String username = Username.get(authentication);
        model.addAttribute("username",username);

        Donation donation = this.donationService.getDonation(id);

        if(donation != null && donation.isApproved())
        {
            ImageEncoder imageEncoder = new ImageEncoder();
            model.addAttribute("defaultImageDonation", defaultImageDonation);
            model.addAttribute("imageEncoder", imageEncoder);
            model.addAttribute("donation", donation);
            List<Donation> top5ApprovedDonations = this.donationService.getTop5ApprovedDonations();
            model.addAttribute("top5ApprovedDonations", top5ApprovedDonations);

            return "pages/single-donation.html";
        }
        else
        {
            return "error/400.html";
        }
    }


}
