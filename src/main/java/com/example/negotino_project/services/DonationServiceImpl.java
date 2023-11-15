package com.example.negotino_project.services;

import com.example.negotino_project.entities.Donation;
import com.example.negotino_project.entities.GoodThing;
import com.example.negotino_project.entities.Talent;
import com.example.negotino_project.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DonationServiceImpl implements DonationService
{
    private final DonationRepository donationRepository;

    @Autowired
    public DonationServiceImpl(DonationRepository donationRepository)
    {
        this.donationRepository = donationRepository;
    }

    @Override
    public Donation getDonation(int id)
    {
        return this.donationRepository.findById(id).orElse(null);
    }

    @Override
    public void addDonation(Donation donation)
    {
        this.donationRepository.save(donation);
    }

    @Override
    public void addDonation(Donation donation, MultipartFile file) throws IOException
    {
        String imgName = file.getOriginalFilename();
        donation.setImgName(imgName);
        donation.setImgType(file.getContentType());

        donation.setImgData(file.getBytes());

        this.donationRepository.save(donation);
    }

    @Override
    public List<Donation> getRequestedDonations()
    {
        List<Donation> requestedDonations = this.donationRepository.findAllByApprovedFalse();
        Collections.reverse(requestedDonations);

        return requestedDonations;
    }

    @Override
    public List<Donation> getApprovedDonations()
    {
        List<Donation> approvedDonations = this.donationRepository.findAllByApprovedTrue();
        Collections.reverse(approvedDonations);

        return approvedDonations;
    }

    @Override
    public boolean approveDonation(int id)
    {
        Optional<Donation> getRequestedDonation = this.donationRepository.findById(id);

        if(getRequestedDonation.isPresent())
        {
            getRequestedDonation.get().setApproved(true);
            this.donationRepository.save(getRequestedDonation.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean discardDonation(int id)
    {
        Optional<Donation> getRequestedDonation = this.donationRepository.findById(id);

        if(getRequestedDonation.isPresent())
        {
            this.donationRepository.delete(getRequestedDonation.get());

            return true;
        }

        return false;
    }

    @Override
    public boolean approveAllDonations()
    {
        List<Donation> getRequestedDonations = this.donationRepository.findAllByApprovedFalse();

        if(getRequestedDonations != null)
        {
            for(Donation donation : getRequestedDonations)
            {
                donation.setApproved(true);
                this.donationRepository.save(donation);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean discardAllDonations()
    {
        List<Donation> getRequestedDonations = this.donationRepository.findAllByApprovedFalse();

        if(getRequestedDonations != null)
        {
            for(Donation donation : getRequestedDonations)
            {
                this.donationRepository.delete(donation);
            }

            return true;
        }

        return false;
    }

    @Override
    public List<Donation> getTop5ApprovedDonations()
    {
        List<Donation> top5ApprovedDonations = this.donationRepository.findTop5ByApprovedTrueOrderByIdDesc();

        return top5ApprovedDonations;
    }
}
