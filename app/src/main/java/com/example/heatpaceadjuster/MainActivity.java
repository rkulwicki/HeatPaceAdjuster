package com.example.heatpaceadjuster;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.heatpaceadjuster.MESSAGE";
    public WeatherClient weatherClient;
    private FusedLocationProviderClient fusedLocationClient;
    public Weather currentWeather = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create weather client connection:
        weatherClient = WeatherClient.getInstance();
        currentWeather = new Weather(Units.IMPERIAL);//todo - make this get the current set Units

        //add validation:
        EditText editGoalPaceText = (EditText) findViewById(R.id.editGoalPace);
        editGoalPaceText.addTextChangedListener(new TextValidator(editGoalPaceText) {
            @Override
            public void validate(TextView textView, String text) {/* Validation code here. Don't need.  */}
        });

        EditText editAdjustedPace = (EditText) findViewById(R.id.editAdjustedPace);
        editAdjustedPace.addTextChangedListener(new TextValidator(editAdjustedPace) {
            @Override
            public void validate(TextView textView, String text) {/* Validation code here. Don't need. */}
        });

        RequestLocationPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        MainActivity mainActivityReference = this;
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentWeather.SetLocationAndCityStateAndDayHour(location, mainActivityReference);
                        weatherClient.GetCurrentTempAndDewPoint(currentWeather, mainActivityReference);
                    }
                });
    }

    //todo - rename this, because this is going to be the connection to Strava
    public void sendAdjustPace(View view) {
        //todo - display a calculated pace from the entered pace
        Intent intent = new Intent(this, DisplayAdjustedPaceActivity.class);
        EditText editText = (EditText) findViewById(R.id.editGoalPace);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void sendCurrentAdjustPace(View view) {
        EditText editGoalPaceText = (EditText) findViewById(R.id.editGoalPace);
        String messageGoalPace = editGoalPaceText.getText().toString();

        EditText editAdjustedPace = (EditText) findViewById(R.id.editAdjustedPace);
        String messageAdjustedPace = editAdjustedPace.getText().toString();

        //todo - use currentWeather which will come from Weather Client
        Weather weather = new Weather(70, 70, Units.IMPERIAL);

        if (messageAdjustedPace.length() == 0) {
            Pace goalPace = new Pace();
            if (goalPace.tryParse(messageGoalPace)) {
                Pace adjustedPace = PaceCalculator.GetAdjustedPaceGivenWeather(goalPace, weather);
                editAdjustedPace.setText(adjustedPace.toString());
            } else {
                editAdjustedPace.setText(messageAdjustedPace);
            }
        } else if (messageGoalPace.length() == 0) {
            Pace adjustedPace = new Pace();
            if (adjustedPace.tryParse(messageAdjustedPace)) {
                Pace goalPace = PaceCalculator.GetGoalPaceGivenWeather(adjustedPace, weather);
                editGoalPaceText.setText(goalPace.toString());
            } else {
                editGoalPaceText.setText(messageGoalPace);
            }
        }
    }

    public void RequestLocationPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.get(
                                    Manifest.permission.ACCESS_FINE_LOCATION);
                            Boolean coarseLocationGranted = result.get(
                                    Manifest.permission.ACCESS_COARSE_LOCATION);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );
        //todo -- ?
        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    public void displayCurrentWeather(Weather weather)
    {
        runOnUiThread(() -> {
            TextView textView = findViewById(R.id.CurrentTemperature);
            textView.setText(String.valueOf(weather.degrees));

            if(weather.location != null)
            {
                TextView textViewTime = findViewById(R.id.CurrentDateTime);
                textViewTime.setText(weather.DayHourToString());
                TextView textViewCityState = findViewById(R.id.CityState);
                textViewCityState.setText(weather.CityStateToString());
            }
        });
    }
}