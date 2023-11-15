package com.example.negotino_project.repository;

import com.example.negotino_project.entities.Donation;
import com.example.negotino_project.entities.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TalentRepository extends JpaRepository<Talent, Integer>
{
    List<Talent> findAllByApprovedFalse();
    List<Talent> findAllByApprovedTrue();
    List<Talent> findTop10ByApprovedTrueOrderByIdDesc();
}
