package com.example.negotino_project.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "news")
public class News
{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "text", length = 14000)
    private String text;
    @Column(name = "type")
    private NewsType type;
    @Column(name = "author") // = username
    private String author;
    @Column(name = "date")
    private Date date;
    @Column(name = "approved")
    private boolean approved;
    @Column(name = "img_name")
    private String imgName;
    @Column(name = "img_type")
    private String imgType;
    @Lob
    @Column(name = "img_data")
    private byte[] imgData;

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
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


    public NewsType getType()
    {
        return type;
    }

    public void setType(NewsType type)
    {
        this.type = type;
    }
    public boolean isApproved()
    {
        return approved;
    }

    public void setApproved(boolean approved)
    {
        this.approved = approved;
    }


    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
