package com.example.heatpaceadjuster;

// Source: http://maximumperformancerunning.blogspot.com/2013/07/temperature-dew-point.html
public class PaceCalculator
{
    public static Pace GetAdjustedPaceGivenWeather(Pace pace, Weather weather)
    {
        return FormulaToAdjusted(pace, weather);
    }

    public static Pace GetGoalPaceGivenWeather(Pace pace, Weather weather)
    {
        return FormulaToGoal(pace, weather);
    }

    //a = s * ( 1.0964f - (0.00217f * dp) + (0.0000122f * dp * dp) )
    // where    a = adjusted pace in seconds
    //          s = current pace in seconds
    //          d = degrees in F
    //          p = dewpoint in F
    // for      100 <= (d + p) <= 180
    private static Pace FormulaToAdjusted(Pace pace, Weather weather)
    {
        Pace adjustedPace = new Pace();
        final int dp = weather.degreesF + weather.dewpointF;
        if(dp < 100)
        {
            return pace;
        }
        else
        {
            final int s = (pace.min * 60) + pace.sec;
            final float multiple = 1.0964f - (0.00217f * dp) + (0.0000122f * dp * dp);
            final double adjustedSecondsDouble = s * multiple;
            final int adjustedSeconds = (int) Math.round(adjustedSecondsDouble);
            adjustedPace.sec = (adjustedSeconds % 60);
            adjustedPace.min = (adjustedSeconds - adjustedPace.sec) / 60;
        }
        if(dp >= 180)
        {
            //todo - display running not recommended dialogue
        }
        return adjustedPace;
    }

    private static Pace FormulaToGoal(Pace pace, Weather weather)
    {
        Pace goalPace = new Pace();
        final int dp = weather.degreesF + weather.dewpointF;
        final int a = (pace.min * 60) + pace.sec;
        final float multiple = 1.0964f - (0.00217f * dp) + (0.0000122f * dp * dp);
        if(multiple == 0)
            return pace;
        final double secondsDouble = a / multiple;
        final int seconds = (int) Math.round(secondsDouble);
        goalPace.sec = (seconds % 60);
        goalPace.min = (seconds - goalPace.sec) / 60;
        return goalPace;
    }

    /*
    degrees + dew point.
100 or less:   no pace adjustment
101 to 110:   0% to 0.5% pace adjustment
111 to 120:   0.5% to 1.0% pace adjustment
121 to 130:   1.0% to 2.0% pace adjustment
131 to 140:   2.0% to 3.0% pace adjustment
141 to 150:   3.0% to 4.5% pace adjustment
151 to 160:   4.5% to 6.0% pace adjustment
161 to 170:   6.0% to 8.0% pace adjustment
171 to 180:   8.0% to 10.0% pace adjustment
Above 180:   hard running not recommended
     */
}

