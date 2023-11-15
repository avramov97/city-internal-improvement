package com.example.negotino_project.repository;

import com.example.negotino_project.entities.User;
import com.example.negotino_project.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>
{
    Optional<User> findByUsername(String username);

}
