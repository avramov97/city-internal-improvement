package com.example.negotino_project.bussiness_logic;

import com.example.negotino_project.entities.Log;
import com.example.negotino_project.entities.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Logs
{
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


    public static Log setupLog(Authentication authentication, String table, Operation operation)
    {
         Log log = new Log();
         Date date = new Date();
         log.setUser(authentication.getName());
         log.setOperation(operation);
         log.setTableName(table);
         log.setDate(dateFormat.format(date));

         if(authentication != null)
         {
             String username = null;

             if(authentication instanceof OAuth2AuthenticationToken)
             {
                 OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                 Map<String, Object> details = (Map<String, Object>) oauthToken.getPrincipal().getAttributes();

                 username = details.get("name").toString();
             }
             else
             {
                 username = authentication.getName();
             }

             log.setUser(username);
         }

         else
         {
             log.setUser("Guest"); // should never come here
         }

         return log;
    }
}
