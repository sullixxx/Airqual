package com.esiea.airqual;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CitiesActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String state;
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitycities);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_2);
        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        country = retrieveCountry();
        downloadData();

    }

    private void downloadData() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiAirVisual.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestApiAirVisual restApiAirVisual = retrofit.create(RestApiAirVisual.class);

        Call<Cities> call = restApiAirVisual.getCitiesInState(state,country,RestApiAirVisual.APIKEY);

        call.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                Log.d("callback2ApiActivitycities","success");
                Cities citiesOfState = response.body();
                if (citiesOfState != null) {
                    showList(citiesOfState.getListCities());
                }
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                Log.d("callback2Api","failed");
                t.printStackTrace();
            }
        });

    }

    private void showList(List<City> listToShow) {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // définit l'adaptateur
        mAdapter = new CitiesAdapter(listToShow,state,country);
        recyclerView.setAdapter(mAdapter);
    }

    private String retrieveCountry(){
        final String DEFAULT = "FRANCE";
        SharedPreferences sharedPreferences = getSharedPreferences("DataShared", MODE_PRIVATE);
        String country = sharedPreferences.getString("Country",DEFAULT);
        if(!country.equals("USA")){
            Toast.makeText(this,"loading details success",Toast.LENGTH_SHORT).show();
        }
        return country;
    }

}
