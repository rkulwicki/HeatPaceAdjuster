package com.example.heatpaceadjuster;

public class Weather {
    public int degreesF;
    public int dewpointF;
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
