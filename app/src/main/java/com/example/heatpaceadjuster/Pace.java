package com.example.heatpaceadjuster;

public class Pace {
    public int min;
    public int sec;
    public Units units;

    public Pace(){
        min = 0;
        sec = 0;
        units = Units.MIN_PER_MI;
    }

    public Pace(int min, int sec){
        this.min = min;
        this.sec = sec;
        this.units = Units.MIN_PER_MI;
    }
    public Pace(int min, int sec, Units units){
        this.min = min;
        this.sec = sec;
        this.units = units;
    }

    public boolean tryParse(String s){
        int i = s.indexOf(":");
        if(i != -1){ //found colon - try get two before and two after.
            if(i != 0) {
                String beforeColon = s.substring(0, i);
                String afterColon = s.substring(i+1);
                //todo - finish
            }else{
                //todo - no minutes. ex. :09 or :5
            }
        }
        else{ //ex. 7

        }
        return true;
    }

    public String toString(){
        String time = "";
        if(this.min >= 0 && this.min < 100 && this.sec < 100 && this.sec >=0){
            time += min + ":";
            if(sec < 10)
                time += "0";
            time += sec + ":";
        }
        else{
            time += "00:00";
        }
        if(this.units == Units.MIN_PER_MI)
            time += " /mi";
        else if (this.units == Units.MIN_PER_KM)
            time += " /km";
        return time;
    }
}
