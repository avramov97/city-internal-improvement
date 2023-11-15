package com.example.negotino_project.controllers;

import com.example.negotino_project.bussiness_logic.Username;
import com.example.negotino_project.entities.Log;
import com.example.negotino_project.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value="/logs")
public class LogsController
{
    @Autowired
    private final LogService logService;

    public LogsController(LogService logService)
    {
        this.logService = logService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String getLogs(Model model, Authentication authentication)
    {
        String username = Username.get(authentication);
        model.addAttribute("username",username);

        List<Log> list = this.logService.getAll();

        model.addAttribute("list",list);

        return "pages/logs.html";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('ROOT-ADMIN')")
    public String deleteLogs(@RequestParam("logs") List<String> list)
    {
        this.logService.removeLogsById(list);

        return "redirect:/logs/all";
    }

    @GetMapping("/delete/all")
    @PreAuthorize("hasAuthority('ROOT-ADMIN')")
    public String deleteAllLogs()
    {
        this.logService.removeAllLogs();

        return "redirect:/logs/all";
    }

}
