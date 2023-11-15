package com.example.negotino_project.controllers;

import com.example.negotino_project.bussiness_logic.Username;
import com.example.negotino_project.entities.Talent;
import com.example.negotino_project.entities.User;
import com.example.negotino_project.entities.UserRole;
import com.example.negotino_project.services.LogService;
import com.example.negotino_project.services.UserRoleService;
import com.example.negotino_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Controller
@RequestMapping("/organization")
public class OrganizationController
{
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public OrganizationController(LogService logService, UserService userService, UserRoleService userRoleService)
    {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN')")
    public String organization(Model model, Authentication authentication)
    {
        String username = Username.get(authentication);
        model.addAttribute("username",username);

        Set<User> users = this.userService.getAllUsers();
        model.addAttribute("users", users);

        UserRole root_admin = this.userRoleService.findRole("ROOT-ADMIN");
        model.addAttribute("root_admin", root_admin);
        UserRole admin = this.userRoleService.findRole("ADMIN");
        model.addAttribute("admin", admin);
        UserRole moderator = this.userRoleService.findRole("MODERATOR");
        model.addAttribute("moderator", moderator);
        UserRole role_user = this.userRoleService.findRole("ROLE_USER");
        model.addAttribute("role_user", role_user);

        return "pages/organization.html";
    }

    @PostMapping("/change-role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String changeRole(@RequestParam("username") String username, String change)
    {
        boolean changed = this.userService.changeRole(username,change);

        return "redirect:/organization";
    }

    @PostMapping("/delete-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@RequestParam("username") String username)
    {
        this.userService.deleteUser(username);

        return "redirect:/organization";
    }
}
