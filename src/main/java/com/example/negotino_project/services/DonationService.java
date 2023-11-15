package com.example.negotino_project.services;

import com.example.negotino_project.entities.Donation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DonationService
{
    Donation getDonation(int id);
    void addDonation(Donation donation);
    void addDonation(Donation donation, MultipartFile file) throws IOException;
    List<Donation> getRequestedDonations();
    List<Donation> getApprovedDonations();
    boolean approveDonation(int id);
    boolean discardDonation(int id);
    boolean discardAllDonations();
    boolean approveAllDonations();
    List<Donation> getTop5ApprovedDonations();

}
