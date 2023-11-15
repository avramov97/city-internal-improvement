package com.example.negotino_project.services;

import com.example.negotino_project.entities.UserRole;

public interface UserRoleService
{
    UserRole findRole(String authority);
}
