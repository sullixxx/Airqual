package com.esiea.airqual;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Weather {
    @SerializedName("ts")
    @Expose
    private String ts;
    @SerializedName("hu")
    @Expose
    private Integer hu;
    @SerializedName("ic")
    @Expose
    private String ic;
    @SerializedName("pr")
    @Expose
    private Integer pr;
    @SerializedName("tp")
    @Expose
    private Integer tp;
    @SerializedName("wd")
    @Expose
    private Integer wd;
    @SerializedName("ws")
    @Expose
    private Double ws;
}
