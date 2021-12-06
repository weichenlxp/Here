package com.lxp.here.utils

import java.text.SimpleDateFormat
import java.util.*

/* 功能描述
 * @author lixianpeng
 * @since 2021-12-04
 */

object DateUtils {
    private const val DATE_FORMATTER = "yyyy-MM-dd";

    fun dateToString(date: Date) :String {
        return SimpleDateFormat(DATE_FORMATTER, Locale.CHINESE).format(date)
    }

    fun stringToDate(string: String) :Date {
        return SimpleDateFormat(DATE_FORMATTER).parse(string)
    }

    fun dateToDate(date: Date) : Date {
        return stringToDate(dateToString(date))
    }

    fun dateToCalendar(date: Date):Calendar {
        val calendar:Calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }

    fun getLastDayOfMonth(date: Date):Date {
        val calendar:Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_YEAR, -1)
        return dateToDate(calendar.time)
    }

    fun getDaysIntervalString(start: String, end: String): Int {
        val format: SimpleDateFormat = SimpleDateFormat(DATE_FORMATTER)
        val dateStart: Date? = format.parse(start)
        val dateEnd: Date? = format.parse(end)
        val diff = dateEnd!!.time - dateStart!!.time
        return (diff / (24 * 60 * 60 * 1000)).toInt()
    }

    fun addValue(date: Date, value: Int, type: String):Date{
        val calendar:Calendar = Calendar.getInstance()
        calendar.time = date
        when (type) {
            "天" -> calendar.add(Calendar.DAY_OF_YEAR, value)
            "月" -> calendar.add(Calendar.MONTH, value)
            "年" -> calendar.add(Calendar.YEAR, value)
        }
        return calendar.time
    }

    fun dateAddDay(date: Date, days:Int):Date {
        val calendar:Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }

    fun dateAddMonth(date: Date, months: Int): Date? {
        val calendar:Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, months)
        return calendar.time
    }

    fun dateAddYear(date: Date?, years: Int): Date? {
        val calendar:Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.YEAR, years)
        return calendar.time
    }

    fun compareDate(date1:Date, date2:Date):Int {
        return if (date1.time>date2.time) 1 else -1
    }

    fun getYearOfDate(date: Date):Int {
        val calendar:Calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }
}