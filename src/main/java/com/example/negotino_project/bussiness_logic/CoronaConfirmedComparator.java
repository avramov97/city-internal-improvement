package com.example.negotino_project.bussiness_logic;

import com.example.negotino_project.entities.Corona.Country;

import java.util.Comparator;

public class CoronaConfirmedComparator implements Comparator<Country>
{
    @Override
    public int compare(Country a, Country b)
    {
        if(a.TotalConfirmed > b.TotalConfirmed)
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

}
