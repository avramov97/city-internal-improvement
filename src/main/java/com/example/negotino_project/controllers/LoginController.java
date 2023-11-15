package com.example.negotino_project.controllers;

import com.example.negotino_project.model.binding.UserRegisterBindingModel;
import com.example.negotino_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController
{
    private final UserService userService;
    private boolean newRegister;

    @Autowired
    public LoginController(UserService userService)
    {
        this.userService = userService;
        this.newRegister=false;
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String loginPage(Model model)
    {
        if(newRegister==true)
        {
            model.addAttribute("newRegister", newRegister);
            newRegister = false;
        }

        UserRegisterBindingModel user = new UserRegisterBindingModel(); // new userLoginBinding??
        model.addAttribute("user", user);
        return "login/login.html";
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public String registerPage(Model model)
    {
        String error = null;
        model.addAttribute("error", error);
        UserRegisterBindingModel user = new UserRegisterBindingModel();
        model.addAttribute("user",user);

        return "login/register.html";
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public String registerPost(@ModelAttribute("user") UserRegisterBindingModel userRegisterBindingModel, HttpServletResponse response, Model model)
    {

        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword()))
        {
            response.setHeader("PASSWORDS_UNMATCH", "1");
            String error = "Пасвордите не се совпаѓаат.";
            model.addAttribute("error", error);
            return "login/register.html :: #error";
        }

        boolean created = this.userService.createUser(userRegisterBindingModel);

        if(created == false)
        {
            response.setHeader("TAKEN_USERNAME", "1");
            String error = "Корисничко име веќе постои.";
            model.addAttribute("error", error);
            return "login/register.html :: #error";
        }

        this.newRegister = true;
        return "login/login.html";
    }

}
