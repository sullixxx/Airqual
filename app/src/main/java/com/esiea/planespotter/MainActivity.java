package com.esiea.planespotter;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

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
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        downloadData();
    }

    private void downloadData() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiOpenSky.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestApiOpenSky restApiOpenSky = retrofit.create(RestApiOpenSky.class);
        Call<Avions> call = restApiOpenSky.getListPlane("47.9659","1.2744","49.3994","3.5044" );

        call.enqueue(new Callback<Avions>() {
            @Override
            public void onResponse(Call<Avions> call, Response<Avions> response) {

                Avions av = response.body();
                showList(av.getState());
            }

            @Override
            public void onFailure(Call<Avions> call, Throwable t) {

            }
        });

    }

    private void showList(List<List<String>> state) {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // défini l'adaptateur
        mAdapter = new MyAdapter(state);
        recyclerView.setAdapter(mAdapter);
    }
}
