package com.esiea.airqual;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class DetailActivity extends Activity {

    private String city;
    private String state;
    private String country;
    private City selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_city);

        Bundle extras = getIntent().getExtras();
        city = extras.getString("city");
        state = extras.getString("state");
        country = extras.getString("country");

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

        Call<City> call = restApiAirVisual.getCityInfo(city,state,country,RestApiAirVisual.APIKEY);

        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                Log.d("callback4ApiActivitycities","success");
                selectedCity = response.body();

                if (selectedCity != null) {
                    showCityDetail();
                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.d("callback4Api","failed");
                t.printStackTrace();
            }
        });
    }


    private void showCityDetail() {


        //ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        CustomGauge gauge2 = findViewById(R.id.gauge2);
        TextView aqius = (TextView)findViewById(R.id.tv_aqius);
        TextView temperature = (TextView)findViewById(R.id.tv_tp);
        TextView humidity = (TextView)findViewById(R.id.tv_hu);
        TextView pression = (TextView)findViewById(R.id.tv_pr);
        TextView wind = (TextView)findViewById(R.id.tv_ws);
        String url = "https://www.airvisual.com/images/"+selectedCity.getData().getCurrent().weather.getIc()+".png";
        ImageView imageView = (ImageView) findViewById(R.id.weather_icon);

        Picasso.get()
                .load(url)
                .fit().centerInside()
                .into(imageView);

        int aqi = selectedCity.getData().getCurrent().pollution.getAqius();
        aqius.setText(Integer.toString(aqi));
        temperature.setText("TEMPERATURE : " + Integer.toString(selectedCity.getData().getCurrent().weather.getTp()) + "°");
        humidity.setText("HUMIDITY : " + Integer.toString(selectedCity.getData().getCurrent().weather.getHu()) + " %");
        pression.setText("PRESSION : " + Integer.toString(selectedCity.getData().getCurrent().weather.getPr()) + " hPa");
        wind.setText("WIND : " + Integer.toString(selectedCity.getData().getCurrent().weather.getWd()) + " m/s" + Integer.toString(selectedCity.getData().getCurrent().weather.getWd()));
        gauge2.setValue(aqi);
        if(aqi<=50){
            //TODO colors
        }else if(aqi>50 && aqi<=100){
            //gauge2.setStrokeColor(Integer.parseInt("0x32cd32", 16));
        }else if(aqi>100 && aqi<=150){

        }else if(aqi>150 && aqi<=200){

        }else if(aqi>200 && aqi<=300){

        }else if(aqi>300 && aqi<=500){

        }



    }


}
