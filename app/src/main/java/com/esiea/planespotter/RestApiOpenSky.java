package com.esiea.planespotter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiOpenSky {

    public static final String ENDPOINT = "https://opensky-network.org/api/";
    // avions survolants la region parisienne https://opensky-network.org/api/states/all?lamin=47.9659&lomin=1.2744&lamax=49.3994&lomax=3.5044

    @GET("states/all")
    Call<Avions> getListPlane(@Query("lamin") String lamin, @Query("lamax") String lamax, @Query("lomin") String lomin,@Query("lomax") String lomax);

}
