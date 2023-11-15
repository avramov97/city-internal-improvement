package com.example.negotino_project.entities;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="logs")
public class Log
{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @Column(name="user")
    private String user;

    @Column(name="operation")
    @Enumerated(EnumType.STRING)
    private Operation operation;

    @Column(name="tableName")
    private String tableName;

    @Column(name="date")
    private String date;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public Operation getOperation()
    {
        return operation;
    }

    public void setOperation(Operation operation)
    {
        this.operation = operation;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "Log{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", operation=" + operation +
                ", tableName='" + tableName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
