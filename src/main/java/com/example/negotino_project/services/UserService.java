package com.example.negotino_project.services;

import com.example.negotino_project.entities.User;
import com.example.negotino_project.model.binding.UserRegisterBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface UserService extends UserDetailsService
{
    boolean createUser(UserRegisterBindingModel user);
    boolean changeRole(String username, String newRole);
    void deleteUser(String username);
    Set<User> getAllUsers();
}
