package com.esiea.planespotter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Avions {
    @SerializedName("time")
    @Expose //permet d'utiliser un nom différent du nom dans le json
    private Integer time;
    @SerializedName("states")
    @Expose
    private List<List<String>> states;

    public List<List<String>> getState() {
        return states;
    }

}
