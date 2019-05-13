package com.esiea.airqual;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatesInCountry {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private List<State> listStates = null;

    public List<State> getListStates() {
        return listStates;
    }
    public String getStatus() {
        return status;
    }
}
