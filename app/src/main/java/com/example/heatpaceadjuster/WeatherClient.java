package com.example.heatpaceadjuster;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherClient {

    private static WeatherClient singleInstance = null;

    private WeatherClient()
    {
    }

    public static WeatherClient getInstance()
    {
        if(singleInstance == null)
            singleInstance = new WeatherClient();

        return singleInstance;
    }

    public void GetCurrentTempAndDewPoint(Weather weather, MainActivity main)
    {
        var task = new HTTPReqTask().execute(weather, main);
    }

    private static final String GET_URL = "https://api.open-meteo.com/v1/forecast?";
            //"https://api.open-meteo.com/v1/forecast?latitude=42.57&longitude=-87.94&hourly=temperature_2m,dewpoint_2m&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&timezone=America%2FChicago";

    //https://blog.codavel.com/how-to-integrate-httpurlconnection
    private class HTTPReqTask extends AsyncTask<Object, Void, Void> {



        @Override
        protected Void doInBackground(Object... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            var currentWeather = (Weather) params[0];
            var main = (MainActivity) params[1];
            try {
                double roundedLat = (double) Math.round(currentWeather.location.getLatitude() * 100) / 100;
                double roundedLong = (double) Math.round(currentWeather.location.getLongitude() * 100) / 100;
                url = new URL("https://api.open-meteo.com/v1/forecast?latitude="+
                        String.valueOf(roundedLat)+"&longitude="+
                        String.valueOf(roundedLong)+
                        "&hourly=temperature_2m,dewpoint_2m&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch");
                urlConnection = (HttpURLConnection) url.openConnection();
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

                    //todo -
                    // 1. parse out the response
                    // 2. determine which time to use (based on location's time zone)
                    // 3. set weather degrees/dewpoint from that

                    //TODO  - GET THE DEWPOINT, AND ALSO GET THE CORRECT DEGREES.
                    DSASDFASDF
                    JSONObject jo = new JSONObject(response.toString());
                    JSONArray tempsJA = jo.getJSONObject("hourly").getJSONArray("temperature_2m"); // todo - move this parsing into a separate, more specific class.
                    currentWeather.degrees = (int) Math.round((Double) tempsJA.get(0)); //todo - 0 should be the index that represents the correct current time based on the time zone.

                    main.displayCurrentWeather(currentWeather);
                } else {
                    System.out.println("GET request to \""+ GET_URL + "\" did not work.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }
}