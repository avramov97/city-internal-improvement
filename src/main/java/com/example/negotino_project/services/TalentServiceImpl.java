package com.example.negotino_project.services;

import com.example.negotino_project.entities.GoodThing;
import com.example.negotino_project.entities.Talent;
import com.example.negotino_project.repository.TalentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TalentServiceImpl implements TalentService
{
    private final TalentRepository talentRepository;

    @Autowired
    public TalentServiceImpl(TalentRepository talentRepository)
    {
        this.talentRepository = talentRepository;
    }


    @Override
    public Talent getTalent(int id)
    {
        return this.talentRepository.findById(id).orElse(null);
    }

    @Override
    public void addTalent(Talent talent)
    {
        this.talentRepository.save(talent);
    }

    @Override
    public void addTalent(Talent talent, MultipartFile file) throws IOException
    {
        String imgName = file.getOriginalFilename();
        talent.setImgName(imgName);
        talent.setImgType(file.getContentType());

        talent.setImgData(file.getBytes());

        this.talentRepository.save(talent);
    }

    @Override
    public List<Talent> getRequestedTalents()
    {
        List<Talent> requestedTalents = this.talentRepository.findAllByApprovedFalse();
        Collections.reverse(requestedTalents);

        return requestedTalents;
    }

    @Override
    public List<Talent> getApprovedTalents()
    {
        List<Talent> approvedTalents = this.talentRepository.findAllByApprovedTrue();
        Collections.reverse(approvedTalents);

        return approvedTalents;
    }

    @Override
    public boolean approveTalent(int id)
    {
        Optional<Talent> getRequestedTalent = this.talentRepository.findById(id);

//        if(getTalent.ifPresent(talent1 -> talent1.setApproved(true));)

        if(getRequestedTalent.isPresent())
        {
            getRequestedTalent.get().setApproved(true);
            this.talentRepository.save(getRequestedTalent.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean discardTalent(int id)
    {
        Optional<Talent> getRequestedTalent = this.talentRepository.findById(id);

        if(getRequestedTalent.isPresent())
        {
            this.talentRepository.delete(getRequestedTalent.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean approveAllTalents()
    {
        List<Talent> getRequestedTalents = this.talentRepository.findAllByApprovedFalse();

        if(getRequestedTalents != null)
        {
            for(Talent talent : getRequestedTalents)
            {
                talent.setApproved(true);
                this.talentRepository.save(talent);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean discardAllTalents()
    {
        List<Talent> getRequestedTalents = this.talentRepository.findAllByApprovedFalse();

        if(getRequestedTalents != null)
        {
            for(Talent talent : getRequestedTalents)
            {
                this.talentRepository.delete(talent);
            }

            return true;
        }

        return false;
    }

    @Override
    public List<Talent> getTop10ApprovedTalents()
    {
        List<Talent> top10ApprovedTalents = this.talentRepository.findTop10ByApprovedTrueOrderByIdDesc();

        return top10ApprovedTalents;
    }


}
