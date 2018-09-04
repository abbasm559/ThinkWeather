package com.example.real.thinkers.thinkweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.real.thinkers.thinkweather.models.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    Button refreshButton;
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


        refreshButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
        WeatherApiInterface.Factory.getInstance().getWeather().enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                temperature.setText(response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp());
                city.setText(response.body().getQuery().getResults().getChannel().getLocation().getCity());
                date.setText(response.body().getQuery().getResults().getChannel().getLastBuildDate());
                conditions.setText(response.body().getQuery().getResults().getChannel().getItem().getCondition().getText());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
