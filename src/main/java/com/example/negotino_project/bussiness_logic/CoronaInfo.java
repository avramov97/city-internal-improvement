package com.example.negotino_project.bussiness_logic;

import com.example.negotino_project.entities.Corona.Country;
import com.example.negotino_project.entities.Corona.Global;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CoronaInfo
{

    public static JSONObject getApiResponse() throws IOException
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

    public static Global getGlobalInfo(JSONObject obj, ObjectMapper mapper) throws IOException
    {
        JSONObject globalJSON = obj.getJSONObject("Global");
        Global global = mapper.readValue(globalJSON.toString(), new TypeReference<Global>(){});

        return global;
    }

    public static List<Country> getCountriesInfo(JSONObject obj, ObjectMapper mapper) throws IOException
    {
        JSONArray arr = obj.getJSONArray("Countries");
        String countriesJSON = arr.toString();
        List<Country> countries = mapper.readValue(countriesJSON, new TypeReference<List<Country>>(){});
        Collections.sort(countries, new CoronaConfirmedComparator());

        return countries;
    }

    public static Country getMacedoniaInfo(JSONObject obj, ObjectMapper mapper) throws IOException
    {
        Country country;
        JSONArray arr = obj.getJSONArray("Countries");
        JSONObject jsonObject = arr.getJSONObject(100);
        String countryCode = jsonObject.getString("CountryCode");
        String slug = jsonObject.getString("Slug");

        if(countryCode.equals("MK") && slug.equals("macedonia"))
        {
            country = mapper.readValue(jsonObject.toString(), new TypeReference<Country>(){});
            return country;
        }
        else
        {
            String countriesJSON = arr.toString();
            List<Country> countries = mapper.readValue(countriesJSON, new TypeReference<List<Country>>(){});
            for(Country currCountry : countries)
            {
                if(currCountry.CountryCode.equals("MK") && currCountry.Slug.equals("macedonia"))
                {
                    return currCountry;
                }
            }
        }

        return null;
    }

}
