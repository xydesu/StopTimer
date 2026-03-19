package me.xydesu.stoptimer.Utils;

public class TimeUtil {
    public static String formatTime(long seconds,
                                    String hourSingular, String hourPlural,
                                    String minuteSingular, String minutePlural,
                                    String secondSingular, String secondPlural) {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;

        String hourUnit = (h == 1) ? hourSingular : hourPlural;
        String minuteUnit = (m == 1) ? minuteSingular : minutePlural;
        String secondUnit = (s == 1) ? secondSingular : secondPlural;

        if (h > 0) {
            if (m == 0 && s == 0) return h + " " + hourUnit;
            if (s == 0) return h + " " + hourUnit + " " + m + " " + minuteUnit;
            return h + " " + hourUnit + " " + m + " " + minuteUnit + " " + s + " " + secondUnit;
        }
        if (m > 0) {
            if (s == 0) return m + " " + minuteUnit;
            return m + " " + minuteUnit + " " + s + " " + secondUnit;
        }
        return s + " " + secondUnit;
    }
}