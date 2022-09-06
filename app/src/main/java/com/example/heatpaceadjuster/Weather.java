package com.example.heatpaceadjuster;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import us.dustinj.timezonemap.TimeZoneMap;

public class Weather{
    public int degrees;
    public int dewpoint;
    public int wind;
    public Location location; //todo - make this private set, public get
    public String city;
    public String state;
    public int hour;
    public String dayOfWeek;
    private Units units;
    public String timezone;

    //Note - Units is only set in the constructor to prevent other mismatches in data
    public Weather(Units units){
        this.degrees = 0;
        this.dewpoint = 0;
        this.units = units;
        this.wind = 0;
        this.location = null;
        this.city = "";
        this.state = "";
        this.hour = 0;
        this.dayOfWeek = "";
    }

    public Weather(int degrees, int dewpoint, Units unit){
        this.degrees = degrees;
        this.dewpoint = dewpoint;
        this.units = unit;
        this.wind = 0;
        this.location = null;
        this.hour = 0;
        this.dayOfWeek = "";
    }

    public String CityStateToString(){
        if(this.location == null)
            return "unknown";

        if(city != "" && state != "")
            return city + ", " + state;
        if(city == "" && state != "")
            return state;
        if(city != "" && state == "")
            return city;
        return "unknown";
    }

    public String DayHourToString(){
        int remainder = this.hour % 12;
        int isPM = this.hour - remainder;
        if(isPM > 0)
        {
            if(this.hour == 12)
                return this.dayOfWeek + " " + this.hour + ":00 PM";
            return this.dayOfWeek + " " + remainder + ":00 PM";
        }
        return this.dayOfWeek + " " + this.hour + ":00 AM";
    }

    public void InitializeWeather(Location location, Context context)
    {
        if(location == null)
            return;

        this.location = location;

        //Set City and State
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder. getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            this.city = addresses.get(0).getLocality();
            this.state = addresses.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Set Day and Hour
        Calendar c = new GregorianCalendar(GetTimezone(this.location));
        c.setTimeInMillis(this.location.getTime());
        String dayOfWeek = "";
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayOfWeek = "Monday";
                break;
            case 2:
                dayOfWeek = "Tuesday";
                break;
            case 3:
                dayOfWeek = "Wednesday";
                break;
            case 4:
                dayOfWeek = "Thursday";
                break;
            case 5:
                dayOfWeek = "Friday";
                break;
            case 6:
                dayOfWeek = "Saturday";
                break;
            case 7:
                dayOfWeek = "Sunday";
                break;
        }
        this.dayOfWeek = dayOfWeek;
        this.hour = c.get(Calendar.HOUR_OF_DAY);

        //get timezone
        try{
            TimeZoneMap map = TimeZoneMap.forRegion(this.location.getLatitude() - 1,
                    this.location.getLongitude() - 1,
                    this.location.getLatitude() + 1,
                    this.location.getLongitude() + 1);
            String place = map.getOverlappingTimeZone(this.location.getLatitude(), this.location.getLongitude()).getZoneId();
            TimeZone t = TimeZone.getTimeZone(place);
            this.timezone = t.getID();
        }catch (Exception e){
            //TODO - add logging?
        }
    }

    public void SetLocation(Location location)
    {
        if(location == null)
            return;

        this.location = location;
    }

    public Units GetUnits(){
        return this.units;
    }

    //todo - move this to a more appropriate place - something location based? idk
    private TimeZone GetTimezone(Location location)
    {
        TimeZoneMap map = TimeZoneMap.forRegion(location.getLatitude() - 1, location.getLongitude() - 1, location.getLatitude() + 1, location.getLongitude() + 1);
        String place = map.getOverlappingTimeZone(location.getLatitude(), location.getLongitude()).getZoneId();
        TimeZone timeZone = TimeZone.getTimeZone(place);
        return timeZone;
    }
}
