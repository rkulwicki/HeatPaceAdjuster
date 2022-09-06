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
                        "&hourly=temperature_2m,dewpoint_2m&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch"+
                        "&timezone="+currentWeather.timezone);
                urlConnection = (HttpURLConnection) url.openConnection();

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

                    // The API response I get starts on the current day's 0 hour (given the timezone). Therefore I
                    // use currentWeather.hour to get the current hourly temperature
                    JSONObject jo = new JSONObject(response.toString());
                    JSONArray temperature2m = jo.getJSONObject("hourly").getJSONArray("temperature_2m"); // todo - move this parsing into a separate, more specific class.
                    JSONArray dewpoint2m = jo.getJSONObject("hourly").getJSONArray("dewpoint_2m"); // todo - move this parsing into a separate, more specific class.
                    currentWeather.degrees = (int) Math.round((Double) temperature2m.get(currentWeather.hour));
                    currentWeather.dewpoint = (int) Math.round((Double) dewpoint2m.get(currentWeather.hour));

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