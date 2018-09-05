package com.example.real.thinkers.thinkweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.real.thinkers.thinkweather.models.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    Button refreshButton;
    String cityName;
    String url;
    RadioButton radioFahrenheit,radioCentigrde;
    EditText inputCity;
    TextView city, date, temperature, conditions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshButton = findViewById(R.id.refreshButton);
        city =findViewById(R.id.city);
        date =  findViewById(R.id.date);
        temperature =  findViewById(R.id.temperature);
        conditions =  findViewById(R.id.conditions);
        inputCity = findViewById(R.id.editTextInput);
        radioFahrenheit= findViewById(R.id.fahrenheit);
        radioCentigrde= findViewById(R.id.centigrade);
        radioFahrenheit.setChecked(true);
        radioFahrenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               change();
            }
        });
        radioCentigrde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });

        refreshButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            cityName = inputCity.getText().toString();
                        url = "yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+cityName
                                +"%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
                        change();
                    }
                }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        change();
    }

    private void change() {
        WeatherApiInterface.Factory.getInstance().getWeather(url).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                String cityName= response.body().getQuery().getResults().getChannel().getLocation().getCity();
                String buildDate= response.body().getQuery().getResults().getChannel().getLastBuildDate();
                String condition= response.body().getQuery().getResults().getChannel().getItem().getCondition().getText();

                if (radioFahrenheit.isChecked()){
                   String temperatureCount = response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp();
                    temperature.setText(temperatureCount);
                }else if (radioCentigrde.isChecked()){
                }
                city.setText(cityName);
                date.setText(buildDate);
                conditions.setText(condition);

            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
