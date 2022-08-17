package com.example.heatpaceadjuster;

import android.location.Location;

public class Weather {
    public int degreesF;
    public int dewpointF;
    public Location location;
    //todo add enum for Celc and Far

    public Weather(){
        this.degreesF = 0;
        this.dewpointF = 0;
    }

    public Weather(int degreesF, int dewpointF){
        this.degreesF = degreesF;
        this.dewpointF = dewpointF;
    }
}
