package com.example.negotino_project.services;

import com.example.negotino_project.entities.Log;

import java.util.List;

public interface LogService
{
    boolean insertLog(Log log);
    void removeLogsById(List<String> list);
    List<Log> getAll();
    void removeAllLogs();
}
