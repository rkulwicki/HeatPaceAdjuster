package com.example.heatpaceadjuster;

import android.location.Location;

public class Weather {
    public int degrees;
    public int dewpoint;
    public int wind;
    public Location location;
    private Units units;

    //Note - Units is only set in the constructor to prevent other mismatches in data
    public Weather(Units units){
        this.degrees = 0;
        this.dewpoint = 0;
        this.units = units;
        this.wind = 0;
        this.location = null;
    }

    public Weather(int degrees, int dewpoint, Units unit){
        this.degrees = degrees;
        this.dewpoint = dewpoint;
        this.units = unit;
        this.wind = 0;
        this.location = null;
    }

    public Units GetUnits(){
        return this.units;
    }

    public int GetDegreesInF()
    {
        if(this.units == Units.IMPERIAL)
        {

        }
        else if(this.units == Units.METRIC)
        {

        }
        return 0;
    }
    public int GetDegreesInC()
    {
        if(this.units == Units.IMPERIAL)
        {

        }
        else if(this.units == Units.METRIC)
        {

        }
        return 0;
    }
    public int GetWindInMPH()
    {
        if(this.units == Units.IMPERIAL)
        {

        }
        else if(this.units == Units.METRIC)
        {

        }
        return 0;
    }
    public int GetWindInKPH()
    {
        if(this.units == Units.IMPERIAL)
        {

        }
        else if(this.units == Units.METRIC)
        {

        }
        return 0;
    }


    // todo -- fill these out. Also, move these to a static class? Like UnitsConverter.
    private int ConvertFtoC(int f)
    {
        return 0;
    }
    private int ConvertCtoF(int f)
    {
        return 0;
    }
    private int ConvertMPHtoKPH(int f)
    {
        return 0;
    }
    private int ConvertKPHtoMPH(int f)
    {
        return 0;
    }

}
