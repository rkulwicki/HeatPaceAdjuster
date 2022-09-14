package com.example.heatpaceadjuster;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

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
                        currentWeather.InitializeWeather(location, mainActivityReference);
                        weatherClient.GetCurrentTempAndDewPoint(currentWeather, mainActivityReference);
                    }
                });
    }

    public void AuthorizeStrava(View view)
    {
        Uri intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", "90891")
                .appendQueryParameter("redirect_uri", "http://localhost") //todo - change this.
                HERE
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("approval_prompt", "auto")
                .appendQueryParameter("scope", "activity:write,read")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
        startActivity(intent);
    }

    //todo - rename this, because this is going to be the connection to Strava
    public void sendStravaConnection(View view) {
        //todo - display a calculated pace from the entered pace


        //initialize connection with



        Intent stravaIntent = new Intent(this, DisplayStravaActivity.class);
        EditText editText = (EditText) findViewById(R.id.editGoalPace);
        String message = editText.getText().toString();
        stravaIntent.putExtra(EXTRA_MESSAGE, message);

        //this is really the only thing I need.
        startActivity(stravaIntent);
    }

    public void sendCurrentAdjustPace(View view) {
        EditText editGoalPaceText = (EditText) findViewById(R.id.editGoalPace);
        String messageGoalPace = editGoalPaceText.getText().toString();

        EditText editAdjustedPace = (EditText) findViewById(R.id.editAdjustedPace);
        String messageAdjustedPace = editAdjustedPace.getText().toString();

        if(currentWeather == null)
            return; //todo - add more robust logic if we haven't loaded the current weather yet.

        if (messageAdjustedPace.length() == 0) {
            Pace goalPace = new Pace();
            if (goalPace.tryParse(messageGoalPace)) {
                Pace adjustedPace = PaceCalculator.GetAdjustedPaceGivenWeather(goalPace, currentWeather);
                editAdjustedPace.setText(adjustedPace.toString());
            } else {
                editAdjustedPace.setText(messageAdjustedPace);
            }
        } else if (messageGoalPace.length() == 0) {
            Pace adjustedPace = new Pace();
            if (adjustedPace.tryParse(messageAdjustedPace)) {
                Pace goalPace = PaceCalculator.GetGoalPaceGivenWeather(adjustedPace, currentWeather);
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

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    public void displayCurrentWeather(Weather weather)
    {
        runOnUiThread(() -> {
            TextView textViewDegrees = findViewById(R.id.CurrentTemperature);
            textViewDegrees.setText(String.valueOf(weather.degrees));

            TextView textViewDewpoint = findViewById(R.id.CurrentDewpoint);
            textViewDewpoint.setText(String.valueOf(weather.dewpoint));

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