package com.esiea.airqual;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestApiAirVisual {

    public static final String ENDPOINT = "api.airvisual.com/v2/countries?";


    //@Headers("API_KEY ")
    //api.airvisual.com/v2/countries?key=xYLsavXgCimFG3ZMN

    @GET("api.airvisual.com/v2/countries?")
    Call<Cities> getListPlane(@Query("country") String country, @Query("key") String apiKey);

}
