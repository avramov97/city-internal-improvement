package com.example.negotino_project.repository;

import com.example.negotino_project.entities.Donation;
import com.example.negotino_project.entities.Event;
import com.example.negotino_project.entities.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Integer>
{
    List<Donation> findAllByApprovedFalse();
    List<Donation> findAllByApprovedTrue();
    List<Donation> findTop5ByApprovedTrueOrderByIdDesc();
}
