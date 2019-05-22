package com.esiea.airqual;

import android.content.Intent;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private City nearestCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
         downloadData("FRANCE");
    }

    private void downloadData(String country) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiAirVisual.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestApiAirVisual restApiAirVisual = retrofit.create(RestApiAirVisual.class);

        Call<City> call1 = restApiAirVisual.getNearestCity(RestApiAirVisual.APIKEY);
        call1.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                if (response.isSuccessful()) {
                    Log.d("callbackApi1","success");
                    nearestCity = response.body();
                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.d("callbackApi1","failed");
                t.printStackTrace();
            }
        });

        Call<StatesInCountry> call2 = restApiAirVisual.getStatesInCountry(country,RestApiAirVisual.APIKEY);
        call2.enqueue(new Callback<StatesInCountry>() {
            @Override
            public void onResponse(Call<StatesInCountry> call, Response<StatesInCountry> response) {
                if (response.isSuccessful()) {
                    Log.d("callbackApi2","success");
                    StatesInCountry statesOfFrance = response.body();
                    showList(statesOfFrance.getListStatesString());
                }
            }

            @Override
            public void onFailure(Call<StatesInCountry> call, Throwable t) {
                Log.d("callbackApi2","failed");
                t.printStackTrace();
            }
        });

    }

    private void showList(List<String> listToShow) {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // définit l'adaptateur
        mAdapter = new MyAdapter(listToShow,1);
        recyclerView.setAdapter(mAdapter);
    }

}
