package com.esiea.airqual;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
    private String State;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitycities);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_2);
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

        Call<Cities> call = restApiAirVisual.getCitiesInState("Ile-de-France","FRANCE",RestApiAirVisual.APIKEY);

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
            public void onFailure(Call<Cities> call2, Throwable t) {
                Log.d("callback2Api","failed");
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
        mAdapter = new MyAdapter(listToShow,2);
        recyclerView.setAdapter(mAdapter);
    }

}
