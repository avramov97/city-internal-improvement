package com.example.negotino_project.repository;

import com.example.negotino_project.entities.Donation;
import com.example.negotino_project.entities.Event;
import com.example.negotino_project.entities.GoodThing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer>
{
    List<Event> findAllByApprovedFalse();
    List<Event> findAllByApprovedTrue();
    List<Event> findTop5ByApprovedTrueOrderByIdDesc();
    List<Event> findTop6ByApprovedTrueOrderByIdDesc();
}
