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
    private List<City> listCities = null;

    public List<String> getListCities() {
        List<String> listStatesString = new ArrayList<>(listCities.size());
        for (City s : listCities) {
            listStatesString.add(s.getData().getCity());
        }
        return listStatesString;
    }

    public String getStatus() {
        return status;
    }
}
