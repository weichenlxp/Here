package edu.hebut.here.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String DATE_FORMATTER = "yyyy-MM-dd";

    //Date->String
    public static String dateToString(Date date) {
        try {
            Log.e("date2Str", new SimpleDateFormat(DATE_FORMATTER).format(date));
            return new SimpleDateFormat(DATE_FORMATTER).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //String->Date
    public static Date stringToDate(String date) {
        try {
            Log.e("string2Date", String.valueOf(new SimpleDateFormat(DATE_FORMATTER).parse(date)));
            return new SimpleDateFormat(DATE_FORMATTER).parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Date->Date
    public static Date dateToDate(Date date) {
        return stringToDate(dateToString(date));
    }

    //Date->Calender
    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    //一个月的最后一天
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return dateToDate(calendar.getTime());
    }

    //两字符串日期相差天数
    public static int getDaysIntervalStr(String dateStart, String dateEnd) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMATTER);
        Date d1 = null;
        Date d2 = null;
        int diffDaysInt = 0;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateEnd);
            Log.e("getDaysIntervalStr, dateStart = ", d1.toString());
            Log.e("getDaysIntervalStr, dateEnd = ", d2.toString());
            // 毫秒ms
            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            Log.e("getDaysIntervalStr, diffDays = ", String.valueOf(diffDays));
            diffDaysInt = Long.valueOf(diffDays).intValue();
            Log.e("getDaysIntervalStr, diffDaysInt = ", String.valueOf(diffDaysInt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diffDaysInt;
    }

    public static Date addValue(Date date1, int value, String type) {
        Calendar date = Calendar.getInstance();
        date.setTime(date1);
        switch (type) {
            case "天":
                date.add(Calendar.DAY_OF_YEAR, value);
                break;
            case "月":
                date.add(Calendar.MONTH, value);
                break;
            case "年":
                date.add(Calendar.YEAR, value);
                break;
            default:
                break;
        }
        return date.getTime();
    }

    //Date+日
    public static Date dateAddDay(Date date1, int days) {
        Calendar date = Calendar.getInstance();
        date.setTime(date1);
        date.add(Calendar.DAY_OF_YEAR, days);
        return date.getTime();
    }

    //Date+月
    public static Date dateAddMonth(Date date1, int months) {
        Calendar date = Calendar.getInstance();
        date.setTime(date1);
        date.add(Calendar.MONTH, months);
        return date.getTime();
    }

    //Date+年
    public static Date dateAddYear(Date date1, int years) {
        Calendar date = Calendar.getInstance();
        date.setTime(date1);
        date.add(Calendar.YEAR, years);
        return date.getTime();
    }

    //比较字符串日期
    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat(DATE_FORMATTER);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            Log.e("compare_date, DATE1 = ", dt1.toString());
            Log.e("compare_date, DATE2 = ", dt2.toString());
            Log.e("compare_date, DATE1.getTime = ", String.valueOf(dt1.getTime()));
            Log.e("compare_date, DATE2.getTime = ", String.valueOf(dt2.getTime()));
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else {
                return -1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    //返回日期年份
    public static int getYearOfDate(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
}