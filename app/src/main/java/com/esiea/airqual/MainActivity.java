package com.esiea.airqual;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
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

        Call<StatesInCountry> call = restApiAirVisual.getStatesInCountry("FRANCE",RestApiAirVisual.APIKEY);

        call.enqueue(new Callback<StatesInCountry>() {
            @Override
            public void onResponse(Call<StatesInCountry> call, Response<StatesInCountry> response) {
                Log.d("callbackApi","sucess");
                StatesInCountry statesOfFrance = response.body();

                showList(statesOfFrance.getListStatesString());
            }

            @Override
            public void onFailure(Call<StatesInCountry> call, Throwable t) {
                Log.d("callbackApi","failed");
                t.printStackTrace();
            }
        });

        Call<Cities> call2 = restApiAirVisual.getCitiesInState("Ile-de-France","FRANCE",RestApiAirVisual.APIKEY);

        call2.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call2, Response<Cities> response2) {
                Log.d("callback2Api","sucess");
                Cities citiesOfState = response2.body();
                //TODO showList(...) in a new activity;
            }

            @Override
            public void onFailure(Call<Cities> call2, Throwable t) {
                Log.d("callback2Api","failed");
                t.printStackTrace();
            }
        });

    }

    private void showList(List<String> state) {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // définit l'adaptateur
        mAdapter = new MyAdapter(state);
        recyclerView.setAdapter(mAdapter);
    }

}
