package com.example.negotino_project.controllers;

import com.example.negotino_project.bussiness_logic.ImageEncoder;
import com.example.negotino_project.bussiness_logic.Logs;
import com.example.negotino_project.bussiness_logic.Username;
import com.example.negotino_project.entities.Event;
import com.example.negotino_project.entities.Log;
import com.example.negotino_project.entities.Operation;
import com.example.negotino_project.services.EventService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventsController
{
    private final EventService eventService;
    private final String defaultImageEvent;
    private final LogService logService;
    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Autowired
    public EventsController(EventService eventService, String defaultImageEvent, LogService logService)
    {
        this.eventService = eventService;
        this.defaultImageEvent = defaultImageEvent;
        this.logService = logService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String events(Model model, Authentication authentication)
    {
        String username = Username.get(authentication);
        model.addAttribute("username",username);

        model.addAttribute("sdf", sdf);
        ImageEncoder imageEncoder = new ImageEncoder();
        model.addAttribute("defaultImageEvent", defaultImageEvent);
        model.addAttribute("imageEncoder", imageEncoder);

        List<Event> approvedEvents = this.eventService.getApprovedEvents();
        model.addAttribute("approvedEvents", approvedEvents);

        if(authentication != null)
        {
            Event event = new Event();
            model.addAttribute("event", event);
        }

//        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MODERATOR")))
        {
            List<Event> requestedEvents = this.eventService.getRequestedEvents();
            model.addAttribute("requestedEvents", requestedEvents);
        }

        return "pages/events.html";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String addEvent(@ModelAttribute Event event,  @RequestParam("files") MultipartFile file, Model model, Authentication authentication) throws IOException
    {
        event.setApproved(false);

        if(!file.isEmpty())
        {
            this.eventService.addEvent(event,file);
        }
        else
        {
            this.eventService.addEvent(event);
        }

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MODERATOR")))
        {
            model.addAttribute("sdf", sdf);
            List<Event> requestedEvents = this.eventService.getRequestedEvents();
            model.addAttribute("requestedEvents", requestedEvents);
            ImageEncoder imageEncoder = new ImageEncoder();
            model.addAttribute("defaultImageEvent", defaultImageEvent);
            model.addAttribute("imageEncoder", imageEncoder);
        }

        Log log = Logs.setupLog(authentication, "Events", Operation.Add);
        this.logService.insertLog(log);

        return "pages/events.html :: #req-events";
    }

    @PostMapping("/add-from-home")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String addEventFromHome(@ModelAttribute Event event,  @RequestParam("files") MultipartFile file, Model model, Authentication authentication) throws IOException
    {
        event.setApproved(false);

        if(!file.isEmpty())
        {
            this.eventService.addEvent(event,file);
        }
        else
        {
            this.eventService.addEvent(event);
        }

        Log log = Logs.setupLog(authentication, "Events", Operation.Add);
        this.logService.insertLog(log);

        return "pages/index.html :: #random";
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String approveEvent(@RequestParam("eventApproveId") int id, Authentication authentication)
    {
        boolean approved = this.eventService.approveEvent(id);

        Log log = Logs.setupLog(authentication, "Events", Operation.Approve);
        this.logService.insertLog(log);

        return "redirect:/events";
    }

    @PostMapping("/discard")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String discardEvent(@RequestParam("eventDiscardId") int id, Authentication authentication)
    {
        boolean discarded = this.eventService.discardEvent(id);

        Log log = Logs.setupLog(authentication, "Events", Operation.Discard);
        this.logService.insertLog(log);

        return "redirect:/events";
    }

    @PostMapping("/discard-all")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String discardAllEvents(Authentication authentication)
    {
        boolean discardedAll = this.eventService.discardAllEvents();

        Log log = Logs.setupLog(authentication, "Events", Operation.DiscardAll);
        this.logService.insertLog(log);

        return "redirect:/events";
    }

    @PostMapping("/approve-all")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String approveAllEvents(Authentication authentication)
    {
        boolean approvedAll = this.eventService.approveAllEvents();

        Log log = Logs.setupLog(authentication, "Events", Operation.ApproveAll);
        this.logService.insertLog(log);

        return "redirect:/events";
    }

    @GetMapping("/single-event/{id}")
    public String getSingleEvent(@PathVariable(name = "id") int id, Model model, Authentication authentication)
    {
        String username = Username.get(authentication);
        model.addAttribute("username",username);

        Event event = this.eventService.getEvent(id);

        if(event != null && event.isApproved())
        {
            ImageEncoder imageEncoder = new ImageEncoder();
            model.addAttribute("sdf", sdf);
            model.addAttribute("defaultImageEvent", defaultImageEvent);
            model.addAttribute("imageEncoder", imageEncoder);
            model.addAttribute("event",event);
            List<Event> top5ApprovedEvents = this.eventService.getTop5ApprovedEvents();
            model.addAttribute("top5ApprovedEvents", top5ApprovedEvents);

            return "pages/single-event.html";
        }
        else
        {
            return "error/400.html";
        }
    }
}
