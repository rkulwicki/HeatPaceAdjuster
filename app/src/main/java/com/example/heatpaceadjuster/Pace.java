package com.example.heatpaceadjuster;

public class Pace {
    public int min;
    public int sec;
    public Units units;

    public Pace(){
        min = 0;
        sec = 0;
        units = Units.IMPERIAL;
    }

    public Pace(int min, int sec){
        this.min = min;
        this.sec = sec;
        this.units = Units.IMPERIAL;
    }
    public Pace(int min, int sec, Units units){
        this.min = min;
        this.sec = sec;
        this.units = units;
    }

    //todo - start debugging here. The string coming in is empty
    public boolean tryParse(String s){
        int i = s.indexOf(":");
        try {
            if (i != -1) { //found colon - try get two before and two after.
                if (i != 0) { // ex. 09:59
                    String beforeColon = s.substring(0, i);
                    String afterColon = s.substring(i + 1);
                    if (beforeColon.length() > 1 && beforeColon.startsWith("0")) {
                        beforeColon = beforeColon.substring(1);
                        this.min = Integer.parseInt(beforeColon);
                    } else {
                        this.min = Integer.parseInt(beforeColon);
                    }
                    if (afterColon.length() > 1 && afterColon.startsWith("0")) {
                        afterColon = afterColon.substring(1);
                        this.sec = Integer.parseInt(afterColon);
                    } else {
                        this.sec = Integer.parseInt(afterColon);
                    }
                } else { // ex. :50
                    String afterColon = s.substring(1);
                    this.sec = Integer.parseInt(afterColon);
                    this.min = 0;
                }
            } else { //ex. 7
                this.sec = 0;
                int tempMin = Integer.parseInt(s);
                if (tempMin > 0 && tempMin < 100)
                    this.min = tempMin;
            }
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public String toString(){
        String time = "";
        if(this.min >= 0 && this.min < 100 && this.sec < 100 && this.sec >=0){
            time += min + ":";
            if(sec < 10)
                time += "0";
            time += sec;
        }
        else{
            time += "00:00";
        }
        if(this.units == Units.IMPERIAL)
            time += " /mi";
        else if (this.units == Units.METRIC)
            time += " /km";
        return time;
    }
}
