package com.example.heatpaceadjuster;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherClient {

    private static WeatherClient singleInstance = null;
    public Location currentLocation = null;
    public String currentWeather = null;

    private WeatherClient()
    {
    }

    public static WeatherClient getInstance()
    {
        if(singleInstance == null)
            singleInstance = new WeatherClient();

        return singleInstance;
    }

    public void GetCurrentTempAndDewPoint(Weather weather)
    {
        new HTTPReqTask().execute(weather);
    }

    private static final String GET_URL = "https://api.open-meteo.com/v1/forecast?";
            //"https://api.open-meteo.com/v1/forecast?latitude=42.57&longitude=-87.94&hourly=temperature_2m,dewpoint_2m&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&timezone=America%2FChicago";

    //https://blog.codavel.com/how-to-integrate-httpurlconnection
    private static class HTTPReqTask extends AsyncTask<Weather, Void, Void> {

        @Override
        protected Void doInBackground(Weather... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            Weather weather = params[0];
            try {
                double roundedLat = (double) Math.round(weather.location.getLatitude() * 100) / 100;
                double roundedLong = (double) Math.round(weather.location.getLongitude() * 100) / 100;
                url = new URL("https://api.open-meteo.com/v1/forecast?latitude="+
                        String.valueOf(roundedLat)+"&longitude="+
                        String.valueOf(roundedLong)+
                        "&hourly=temperature_2m,dewpoint_2m&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&timezone=America%2FChicago");
                urlConnection = (HttpURLConnection) url.openConnection();
                //set values from location
//                urlConnection.setRequestProperty("latitude", String.valueOf(weather.location.getLatitude()));
//                urlConnection.setRequestProperty("longitude", String.valueOf(weather.location.getLongitude()));
//                urlConnection.setRequestProperty("hourly", "temperature_2m,dewpoint_2m");
//                //todo - make this toggle-able between C/F ? (or maybe just convert later?)
//                urlConnection.setRequestProperty("temperature_unit", "fahrenheit");
//                urlConnection.setRequestProperty("windspeed_unit", "mph");
//                urlConnection.setRequestProperty("precipitation_unit", "inch");
                //todo - timezone is in GMT+0. use current location to get the correct temp.

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    System.out.println(response.toString());
                    //todo -
                    // 1. parse out the response
                    // 2. determine which time to use (based on location's time zone)
                    // 3. set weather degrees/dewpoint from that
                } else {
                    System.out.println("GET request not worked");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }
}