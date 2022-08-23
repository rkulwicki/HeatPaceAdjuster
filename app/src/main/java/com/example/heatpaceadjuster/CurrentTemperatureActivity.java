package com.example.heatpaceadjuster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CurrentTemperatureActivity extends AppCompatActivity {

    public CurrentTemperatureActivity(){}

    public void setCurrentWeather(Weather weather)
    {
        TextView textView = findViewById(R.id.CurrentTemperature);
        textView.setText(String.valueOf(weather.degrees));
    }

}
