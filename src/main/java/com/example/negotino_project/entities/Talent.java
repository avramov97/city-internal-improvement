package com.example.negotino_project.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "talents")
public class Talent
{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "occupation")
    private String occupation;
    @Column(name = "text", length = 14000)
    private String text;
    @Column(name = "facebook")
    private String facebook;
    @Column(name = "instagram")
    private String instagram;
    @Column(name = "img_name")
    private String imgName;
    @Column(name = "img_type")
    private String imgType;
    @Lob
    @Column(name = "img_data")
    private byte[] imgData;
    @Column(name = "approved")
    private boolean approved;

    public Talent()
    {
    }

//    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getOccupation()
    {
        return occupation;
    }

    public void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getFacebook()
    {
        return facebook;
    }

    public void setFacebook(String facebook)
    {
        this.facebook = facebook;
    }

    public String getInstagram()
    {
        return instagram;
    }

    public void setInstagram(String instagram)
    {
        this.instagram = instagram;
    }

    public boolean isApproved()
    {
        return approved;
    }

    public void setApproved(boolean approved)
    {
        this.approved = approved;
    }

    public String getImgName()
    {
        return imgName;
    }

    public void setImgName(String imgName)
    {
        this.imgName = imgName;
    }

    public String getImgType()
    {
        return imgType;
    }

    public void setImgType(String imgType)
    {
        this.imgType = imgType;
    }

    public byte[] getImgData()
    {
        return imgData;
    }

    public void setImgData(byte[] imgData)
    {
        this.imgData = imgData;
    }
}
