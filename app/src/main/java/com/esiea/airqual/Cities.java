package com.esiea.airqual;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Cities {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private List<City> listCities;

    public List<String> getListCities() {
        List<String> listCitiesString = new ArrayList<>(listCities.size());
        for (City s : listCities) {
            listCitiesString.add(s.getCity());
        }
        return listCitiesString;
    }


    public String getStatus() {
        return status;
    }
}
