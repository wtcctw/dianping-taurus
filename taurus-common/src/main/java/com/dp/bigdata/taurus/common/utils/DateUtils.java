package com.dp.bigdata.taurus.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Author   mingdongli
 * 16/5/11  上午10:17.
 */
public class DateUtils {

    public static Date zeroHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date zeroSecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date zeroMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date tomorrow(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    public static Date nextHalfHour(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 30);
        return cal.getTime();
    }

    public static Date zeroOrThirtyMinute(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int min = cal.get(Calendar.MINUTE);
        if (min > 0 && min < 30) {
            cal.set(Calendar.MINUTE, 30);
        } else if (min > 30 && min <= 59) {
            cal.set(Calendar.MINUTE, 0);
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }
        return cal.getTime();
    }

    public static Calendar yesterday() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar yesterdayOfLastYear() {

        Calendar cal = yesterday();
        cal.add(Calendar.YEAR, -1);
        return cal;
    }

    public static Calendar NMonthAgo(int N) {

        Calendar cal = yesterday();
        cal.add(Calendar.MONTH, -N);
        return cal;
    }
}
