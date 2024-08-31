package com.example.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private WeatherViewModel weatherViewModel;
    private SearchView searchView;
    private TextView weatherInfo;
    private TextView errorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        searchView = findViewById(R.id.searchView);
        weatherInfo = findViewById(R.id.weatherInfo);
        errorInfo = findViewById(R.id.errorInfo);
        Button searchButton = findViewById(R.id.searchButton);

        weatherViewModel.getWeather().observe(this, weatherModel -> {
            if (weatherModel != null) {
                String mainWeather = weatherModel.getWeather()[0].getMain();
                String description = weatherModel.getWeather()[0].getDescription();
                double temp = weatherModel.getMain().getTemp();

                weatherInfo.setText(String.format("Temperature: %.2f°C\nMain: %s\nDescription: %s",
                        temp, mainWeather, description));
                errorInfo.setText("");
            }
        });

        weatherViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                errorInfo.setText(error);
                weatherInfo.setText("");
            }
        });

        searchButton.setOnClickListener(v -> {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty()) {
                weatherViewModel.fetchWeather(query);
            }
        });
    }
}
