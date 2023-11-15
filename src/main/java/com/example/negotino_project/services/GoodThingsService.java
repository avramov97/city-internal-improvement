package com.example.negotino_project.services;

import com.example.negotino_project.entities.GoodThing;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface GoodThingsService
{
    GoodThing getGoodThing(int id);
    void addGoodThing(GoodThing goodThing);
    void addGoodThing(GoodThing goodThing, MultipartFile file) throws IOException;
    List<GoodThing> getRequestedGoodThings();
    List<GoodThing> getApprovedGoodThings();
    List<GoodThing> getTop10ApprovedGoodThings();
    boolean discardGoodThing(int id);
    boolean approveGoodThing(int id);
    boolean discardAllRequestedGoodThings();
    boolean approveAllRequestedGoodThings();

    LinkedHashMap<String,Long> getTopThreeAuthors();
}
