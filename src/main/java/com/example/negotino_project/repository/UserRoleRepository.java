package com.example.negotino_project.repository;

import com.example.negotino_project.entities.User;
import com.example.negotino_project.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, String>
{
    Optional<UserRole> findByAuthority(String authority);
}
