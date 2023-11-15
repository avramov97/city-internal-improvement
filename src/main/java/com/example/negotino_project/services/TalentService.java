package com.example.negotino_project.services;

import com.example.negotino_project.entities.Talent;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TalentService
{
    Talent getTalent(int id);
    void addTalent(Talent talent);
    void addTalent(Talent talent, MultipartFile file) throws IOException;
    List<Talent> getRequestedTalents();
    List<Talent> getApprovedTalents();
    boolean approveTalent(int id);
    boolean discardTalent(int id);
    boolean discardAllTalents();
    boolean approveAllTalents();
    List<Talent> getTop10ApprovedTalents();
}
