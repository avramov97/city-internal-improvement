package com.example.negotino_project.bussiness_logic;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Map;

public class Username
{

    public static String get(Authentication authentication)
    {
        String username = "Guest";

        if(authentication != null)
        {
            if(authentication instanceof OAuth2AuthenticationToken)
            {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                Map<String, Object> details = (Map<String, Object>) oauthToken.getPrincipal().getAttributes();

//                System.out.println(details.get("name"));
//                System.out.println(details);
                username = details.get("name").toString();
            }
            else
            {
                username = authentication.getName();
            }
        }

        return username;
    }
}
