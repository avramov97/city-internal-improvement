package com.example.negotino_project.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "donations")
public class Donation
{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;
    @Column(name = "type")
    private DonationType type;
    @Column(name = "title")
    private String title;
    @Column(name = "contact")
    private String contact;
    @Column(name = "text", length = 14000)
    private String text;
    @Column(name = "author") // = username
    private String author;
    @Column(name = "img_name")
    private String imgName;
    @Column(name = "img_type")
    private String imgType;
    @Lob
    @Column(name = "img_data")
    private byte[] imgData;

    @Column(name = "approved")
    private boolean approved;

    public Donation() { }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public DonationType getType()
    {
        return type;
    }

    public void setType(DonationType type)
    {
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public boolean isApproved()
    {
        return approved;
    }

    public void setApproved(boolean approved)
    {
        this.approved = approved;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
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
