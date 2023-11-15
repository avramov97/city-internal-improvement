package com.example.negotino_project.repository;

import com.example.negotino_project.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepository extends JpaRepository<Log, Integer>
{
    Log findLogById(String id);
    void deleteLogById(String id);
    void deleteLogsById(List<String> list);
}
