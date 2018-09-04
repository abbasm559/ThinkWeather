package com.example.real.thinkers.thinkweather;


import com.example.real.thinkers.thinkweather.models.Weather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface WeatherApiInterface {
    String cityName = "Barisal";

    String BASE_URL = "https://query.yahooapis.com/v1/public/";
    @GET("yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+cityName
            +"%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
    Call<Weather> getWeather();

    class Factory {

        private static WeatherApiInterface service;
        public static WeatherApiInterface getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(WeatherApiInterface.class);
                return service;
            }
            else {
                return service;
            }
        }
    }
}