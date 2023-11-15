package com.example.negotino_project.services;

import com.example.negotino_project.bussiness_logic.ImageEncoder;
import com.example.negotino_project.entities.Event;
import com.example.negotino_project.entities.GoodThing;
import com.example.negotino_project.repository.GoodThingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class GoodThingsServiceImpl implements GoodThingsService
{
    private final GoodThingRepository goodThingRepository;

    @Autowired
    public GoodThingsServiceImpl(GoodThingRepository goodThingRepository)
    {
        this.goodThingRepository = goodThingRepository;
    }

    @Override
    public GoodThing getGoodThing(int id)
    {
        return this.goodThingRepository.findById(id).orElse(null);
    }

    @Override
    public void addGoodThing(GoodThing goodThing)
    {
        this.goodThingRepository.save(goodThing);
    }

    @Override
    public void addGoodThing(GoodThing goodThing, MultipartFile file) throws IOException
    {
        String imgName = file.getOriginalFilename();
        goodThing.setImgName(imgName);
        goodThing.setImgType(file.getContentType());
        goodThing.setImgData(file.getBytes());

        this.goodThingRepository.save(goodThing);
    }

    @Override
    public List<GoodThing> getRequestedGoodThings()
    {
        List<GoodThing> requestedGoodThings =
        this.goodThingRepository.findAllByApprovedFalse();
        Collections.reverse(requestedGoodThings);

        return requestedGoodThings;
    }

    @Override
    public List<GoodThing> getApprovedGoodThings()
    {
        List<GoodThing> approvedGoodThings = this.goodThingRepository.findAllByApprovedTrue();
        Collections.reverse(approvedGoodThings);

        return approvedGoodThings;
    }

    @Override
    public boolean approveGoodThing(int id)
    {
        Optional<GoodThing> getRequestedGoodThing = this.goodThingRepository.findById(id);

        if(getRequestedGoodThing.isPresent())
        {
            getRequestedGoodThing.get().setApproved(true);
            this.goodThingRepository.save(getRequestedGoodThing.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean discardGoodThing(int id)
    {
        Optional<GoodThing> getRequestedGoodThing = this.goodThingRepository.findById(id);

        if(getRequestedGoodThing.isPresent())
        {
            this.goodThingRepository.delete(getRequestedGoodThing.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean approveAllRequestedGoodThings()
    {
        List<GoodThing> getRequestedGoodThings = this.goodThingRepository.findAllByApprovedFalse();

        if(getRequestedGoodThings != null)
        {
            for(GoodThing goodThing : getRequestedGoodThings)
            {
                goodThing.setApproved(true);
                this.goodThingRepository.save(goodThing);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean discardAllRequestedGoodThings()
    {
        List<GoodThing> getRequestedGoodThings = this.goodThingRepository.findAllByApprovedFalse();

        if(getRequestedGoodThings != null)
        {
            for(GoodThing goodThing : getRequestedGoodThings)
            {
                this.goodThingRepository.delete(goodThing);
            }

            return true;
        }

        return false;
    }

    @Override
    public LinkedHashMap<String,Long> getTopThreeAuthors()
    {
        List<Object[]> list = this.goodThingRepository.getTopThreeAuthors();

        if(list.size() >= 3)
        {
            LinkedHashMap<String, Long> topThreeAuthors = new LinkedHashMap<String, Long>();

            for (int i = 0; i < 3; i++)
            {
                String key = (String) list.get(i)[0];
                System.out.print(key + " - ");
                long value = (Long) list.get(i)[1];
                System.out.print(value + "\n");

                topThreeAuthors.put(key, value);
            }

            return topThreeAuthors;
        }

        return null;
    }

    @Override
    public List<GoodThing> getTop10ApprovedGoodThings()
    {
        List<GoodThing> top10ApprovedGoodThings = this.goodThingRepository.findTop10ByApprovedTrueOrderByIdDesc();

        return top10ApprovedGoodThings;
    }

}
