package com.example.negotino_project;

import com.example.negotino_project.bussiness_logic.ImageEncoder;
import com.example.negotino_project.controllers.TalentsController;
import org.apache.commons.fileupload.FileUpload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;

@EnableJpaRepositories(basePackages = "com.example.negotino_project.repository")
@SpringBootApplication
public class NegotinoProjectApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(NegotinoProjectApplication.class, args);
    }
}
