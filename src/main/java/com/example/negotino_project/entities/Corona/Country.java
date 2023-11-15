package com.example.negotino_project.entities.Corona;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import javax.xml.crypto.Data;

public class Country
{
    public String ID;
    public int NewRecovered;
    public int NewDeaths;
    public int TotalRecovered;
    public int TotalConfirmed;
    public String Country;
    public String CountryCode;
    public String Slug;
    public int NewConfirmed;
    public int TotalDeaths;
    public String Date;
    public JsonNode Premium;

}
