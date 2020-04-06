package cn.guba.igu8.utils;

import java.util.Calendar;

public class DateUtils {

    public static boolean isRestDay() {
        boolean isRestDay = false;
        Calendar cal = Calendar.getInstance();

        int curday = cal.get(Calendar.DAY_OF_WEEK);
        if (curday < 2 || curday > 6) {
            isRestDay = true;
        }
        return isRestDay;
    }
}
