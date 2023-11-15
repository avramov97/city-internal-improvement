package com.example.negotino_project.services;

import com.example.negotino_project.entities.Log;
import com.example.negotino_project.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService
{
    private final LogsRepository logsRepository;

    @Autowired
    public LogServiceImpl(LogsRepository logsRepository)
    {
        this.logsRepository = logsRepository;
    }

    @Override
    public boolean insertLog(Log log)
    {
        return this.logsRepository.save(log) != null;
    }

    @Override
    public void removeLogsById(List<String> list)
    {
        for(String uuid : list)
        {
            Log log = this.logsRepository.findLogById(uuid);
            this.logsRepository.delete(log);
        }
    }

    @Override
    public List<Log> getAll()
    {
        return this.logsRepository.findAll().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void removeAllLogs()
    {
        this.logsRepository.deleteAll();
    }
}
