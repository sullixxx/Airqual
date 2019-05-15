package com.esiea.airqual;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("city")
    @Expose
    private String city = null;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("current")
    @Expose
    private Current current;

    public String getCity() {
        return city;
    }

    public Current getCurrent() {
        return current;
    }
}
