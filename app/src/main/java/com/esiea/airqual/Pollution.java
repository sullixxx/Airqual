package com.esiea.airqual;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Pollution {
    @SerializedName("ts")
    @Expose
    private String ts;
    @SerializedName("aqius")
    @Expose
    private Integer aqius;
    @SerializedName("mainus")
    @Expose
    private String mainus;
    @SerializedName("aqicn")
    @Expose
    private Integer aqicn;
    @SerializedName("maincn")
    @Expose
    private String maincn;
}
