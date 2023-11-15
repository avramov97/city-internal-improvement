package com.example.negotino_project.common.factories;

import com.example.negotino_project.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;

public final class UserRoleFactory
{
    @Autowired


    public UserRoleFactory()
    {

    }

    public final UserRole createUserRole(String authority)
    {
        UserRole userRole = new UserRole();
        userRole.setAuthority(authority);

        return userRole;
    }

}
