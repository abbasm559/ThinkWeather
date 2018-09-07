package com.example.real.thinkers.thinkweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.real.thinkers.thinkweather.models.Weather;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    String [] cities= {"Dhaka", "Chittagong", "Barisal", "Klulna", "Rajshahi", "Rangpur", "Sylhet", "Mymensingh", "Comilla", "New Delhi",
            "Islamabad", "Kabul", "Yerevan", "Baku", "Manama", "Kuwait City", "Thimphu", "Bandar Seri Begawan", "Phnom Penh", "Beijing",
            "Nicosia", "Tbilisi", "Jakarta", "Tehran", "Baghdad", "Jerusalem", "Tokyo", "Amman", "Astana", "Bishkek", "Vientiane", "Beirut", "Kuala Lumpur",
            "Male", "Kathmandu", "Muscat", "Jerusalem (East)", "Doha", "Moscow", "Riyadh"};
    private String TAG = "MainActivity";
    Button refreshButton;
    String cityName="Dhaka";
    String url;
    String temperatureCount;
    RadioButton radioFahrenheit,radioCentigrde;
    AutoCompleteTextView autoCompleteTv;
    TextView city, date, temperature, conditions,unitsTv;
    //forecast data---------------------------------------
    /*TextView nextDay1, nextDay2, nextDay3,nextDay4, nextDay5;
    TextView nextDay1Max, nextDay2Max, nextDay3Max, nextDay4Max, nextDay5Max;
    TextView nextDay1Min, nextDay2Min, nextDay3Min, nextDay4Min, nextDay5Min;
    ImageView nextDay1Img, nextDay2Img,nextDay3Img,nextDay4Img,nextDay5Img;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshButton = findViewById(R.id.refreshButton);
        city =findViewById(R.id.city);
        date =  findViewById(R.id.date);
        temperature =  findViewById(R.id.temperature);
        conditions =  findViewById(R.id.conditions);
        autoCompleteTv = findViewById(R.id.autoCompleteTv);
        radioFahrenheit= findViewById(R.id.fahrenheit);
        radioCentigrde= findViewById(R.id.centigrade);
        unitsTv= findViewById(R.id.units);

        autoCompleteTv=findViewById(R.id.autoCompleteTv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,cities);
        autoCompleteTv.setAdapter(adapter);
        autoCompleteTv.setThreshold(1);


        //forecast data introducing--------------------------------------------
        /*nextDay1 = findViewById(R.id.nextDay1); nextDay1Max = findViewById(R.id.maxTempNextDay1);
        nextDay2 = findViewById(R.id.nextDay2); nextDay2Max = findViewById(R.id.maxTempNextDay2);
        nextDay3 = findViewById(R.id.nextDay3); nextDay3Max = findViewById(R.id.maxTempNextDay3);
        nextDay4 = findViewById(R.id.nextDay4); nextDay4Max = findViewById(R.id.maxTempNextDay4);
        nextDay5 = findViewById(R.id.nextDay5); nextDay5Max = findViewById(R.id.maxTempNextDay5);
        nextDay1Min = findViewById(R.id.minTempNextDay1);   nextDay1Img = findViewById(R.id.nextDayImg1);
        nextDay2Min = findViewById(R.id.minTempNextDay2);   nextDay2Img = findViewById(R.id.nextDayImg2);
        nextDay3Min = findViewById(R.id.minTempNextDay3);   nextDay3Img = findViewById(R.id.nextDayImg3);
        nextDay4Min = findViewById(R.id.minTempNextDay4);   nextDay4Img = findViewById(R.id.nextDayImg4);
        nextDay5Min = findViewById(R.id.minTempNextDay5);   nextDay5Img = findViewById(R.id.nextDayImg5);*/


        radioFahrenheit.setChecked(true);
        radioFahrenheit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                change();
            }
        });
        radioCentigrde.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                change();
            }
        });

        refreshButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            cityName = autoCompleteTv.getText().toString();
                            if (cityName.equals("")){
                                autoCompleteTv.setError("Please input a city");
                            }else {
                        url = "yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+cityName
                                +"%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
                        change();
                    }}
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
                   temperatureCount = response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp();
                    temperature.setText(temperatureCount);
                    unitsTv.setText("°F");
                }else if (radioCentigrde.isChecked()){
                    double tempInDouble = Double.parseDouble(temperatureCount);
                    double tempInCentigrade = (tempInDouble-40.0)*(100.0/180.0);
                    DecimalFormat df = new DecimalFormat("#.#");
                    String tempInDecimal = df.format((tempInCentigrade));
                    temperature.setText(tempInDecimal);
                    unitsTv.setText("°C");
                }

                city.setText(cityName);
                date.setText(buildDate);
                conditions.setText(condition);



                //forecast data set up-----------------------------------------

               // nextDay1.setText(response.body().getQuery().getResults().getChannel().getItem().getForecast().lastIndexOf(0));


            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }


    }

