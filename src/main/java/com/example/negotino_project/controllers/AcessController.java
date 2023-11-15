package com.example.negotino_project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AcessController
{
    @GetMapping("/unauthorized")
    public String unauthorized()
    {
        return "error/unauthorized.html";
    }

}
