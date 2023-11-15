package com.example.negotino_project.services;

import com.example.negotino_project.entities.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EventService
{
    Event getEvent(int id);
    void addEvent(Event event);
    void addEvent(Event event, MultipartFile file) throws IOException;
    List<Event> getRequestedEvents();
    List<Event> getApprovedEvents();
    List<Event> getTop6ApprovedEvents();
    List<Event> getTop5ApprovedEvents();
    boolean approveEvent(int id);
    boolean discardEvent(int id);
    boolean discardAllEvents();
    boolean approveAllEvents();
}
