package com.example.negotino_project.config;

import com.example.negotino_project.bussiness_logic.ImageEncoder;
import com.example.negotino_project.common.factories.UserRoleFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@Configuration
public class ApplicationBeanConfiguration
{
    @Bean
    public static JSONObject getApiResponseBean() throws IOException
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.covid19api.com/summary")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String jsonString = response.body().string();
        JSONObject obj = new JSONObject(jsonString);

        return obj;
    }

    @Bean
    public UserRoleFactory userRoleFactory()
    {
        return new UserRoleFactory();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }

    @Bean
    public String defaultImageNews() throws IOException
    {
        return ImageEncoder.getDefaultImage("./src/main/resources/static/helpers/news_noimage");
    }

    @Bean
    public String defaultImageEvent() throws IOException
    {
        return ImageEncoder.getDefaultImage("./src/main/resources/static/helpers/event_noimage");
    }

    @Bean
    public String defaultImageGoodThing() throws IOException
    {
        return ImageEncoder.getDefaultImage("./src/main/resources/static/helpers/good-thing_noimage");
    }

    @Bean
    public String defaultImageDonation() throws IOException
    {
        return ImageEncoder.getDefaultImage("./src/main/resources/static/helpers/donation_noimage");
    }

//    @Bean
//    public String getDefaultImageTalents() throws IOException
//    {
//        String getDefaultImage = ImageEncoder.getDefaultImage();
//
//        return getDefaultImageGoodThing;
//    }
}
