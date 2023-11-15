package com.example.negotino_project.services;

import com.example.negotino_project.entities.Donation;
import com.example.negotino_project.entities.Event;
import com.example.negotino_project.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService
{
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository)
    {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event getEvent(int id)
    {
        return this.eventRepository.findById(id).orElse(null);
    }

    @Override
    public void addEvent(Event event)
    {
        this.eventRepository.save(event);
    }

    @Override
    public void addEvent(Event event, MultipartFile file) throws IOException
    {
        String imgName = file.getOriginalFilename();
        event.setImgName(imgName);
        event.setImgType(file.getContentType());
        event.setImgData(file.getBytes());

        this.eventRepository.save(event);
    }

    @Override
    public List<Event> getRequestedEvents()
    {
        List<Event> requestedEvents = this.eventRepository.findAllByApprovedFalse();
        Collections.reverse(requestedEvents);

        return requestedEvents;
    }

    @Override
    public List<Event> getApprovedEvents()
    {
        List<Event> approvedEvents = this.eventRepository.findAllByApprovedTrue();
        Collections.reverse(approvedEvents);

        return approvedEvents;
    }

    @Override
    public List<Event> getTop5ApprovedEvents()
    {
        List<Event> top5ApprovedEvents = this.eventRepository.findTop5ByApprovedTrueOrderByIdDesc();

        return top5ApprovedEvents;
    }

    @Override
    public List<Event> getTop6ApprovedEvents()
    {
        List<Event> top6ApprovedEvents = this.eventRepository.findTop6ByApprovedTrueOrderByIdDesc();

        return top6ApprovedEvents;
    }

    @Override
    public boolean approveEvent(int id)
    {
        Optional<Event> getRequestedEvent = this.eventRepository.findById(id);

        if(getRequestedEvent.isPresent())
        {
            getRequestedEvent.get().setApproved(true);
            this.eventRepository.save(getRequestedEvent.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean discardEvent(int id)
    {
        Optional<Event> getRequestedEvent = this.eventRepository.findById(id);

        if(getRequestedEvent.isPresent())
        {
            this.eventRepository.delete(getRequestedEvent.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean approveAllEvents()
    {
        List<Event> getRequestedEvents = this.eventRepository.findAllByApprovedFalse();

        if(getRequestedEvents != null)
        {
            for(Event event : getRequestedEvents)
            {
                event.setApproved(true);
                this.eventRepository.save(event);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean discardAllEvents()
    {
        List<Event> getRequestedEvents = this.eventRepository.findAllByApprovedFalse();

        if(getRequestedEvents != null)
        {
            for(Event event : getRequestedEvents)
            {
                this.eventRepository.delete(event);
            }

            return true;
        }

        return false;
    }
}
