package com.esiea.airqual;

import android.Manifest;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.support.design.widget.Snackbar;


public class MainActivity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private City nearestCity;
    private RestApiAirVisual restApiAirVisual;
    private Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        registerBroadcastConnectivity();
        checkPermissionsStorage(); //ask user "Write external storage" permission
        createSnack();
        //checkConnection();
        downloadData();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("city", nearestCity.getData().getCity());
                extras.putString("state", nearestCity.getData().getState());
                extras.putString("country", nearestCity.getData().getCountry());
                intent.putExtras(extras);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Airqual.getInstance().setConnectivityListener(this);
    }




    private void downloadData() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiAirVisual.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        restApiAirVisual = retrofit.create(RestApiAirVisual.class);

        Call<City> call1 = restApiAirVisual.getNearestCity(RestApiAirVisual.APIKEY);
        call1.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                if (response.isSuccessful()) {
                    Log.d("callbackApi1","success");
                    nearestCity = response.body();
                    makeSecondApiCall();
                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.d("callbackApi1","failed");
                t.printStackTrace();
            }
        });
    }

    private void makeSecondApiCall() {
        final String country = nearestCity.getData().getCountry();
        Call<StatesInCountry> call2 = restApiAirVisual.getStatesInCountry(country,RestApiAirVisual.APIKEY);
        call2.enqueue(new Callback<StatesInCountry>() {
            @Override
            public void onResponse(Call<StatesInCountry> call, Response<StatesInCountry> response) {
                if (response.isSuccessful()) {
                    Log.d("callbackApi2","success");
                    StatesInCountry statesOfFrance = response.body();
                    showList(statesOfFrance.getListStates());
                    saveCountry(country);
                }
            }

            @Override
            public void onFailure(Call<StatesInCountry> call, Throwable t) {
                Log.d("callbackApi2","failed");
                t.printStackTrace();
            }
        });
    }

    private void saveCountry(String country){
        SharedPreferences sharedPref = getSharedPreferences("DataShared",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Country",country);
        editor.commit();

        Toast.makeText(this, "Your country has been saved : "+country , Toast.LENGTH_SHORT).show();
    }

    private void showList(List<State> listToShow) {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // définit l'adaptateur
        mAdapter = new StateAdapter(listToShow);
        recyclerView.setAdapter(mAdapter);
    }


    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if(!isConnected) {
            showSnack(isConnected);
        }
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        if (!isConnected) {
            snackbar.show();
        }else if(isConnected){
            snackbar.dismiss();
        }
    }

    private void createSnack(){
        String message = "no internet connection";
        snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_INDEFINITE);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void registerBroadcastConnectivity(){
        registerReceiver(new ConnectivityReceiver(),new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void checkPermissionsStorage(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

    }
}
