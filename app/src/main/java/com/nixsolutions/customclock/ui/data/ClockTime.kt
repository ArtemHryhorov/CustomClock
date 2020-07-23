package com.nixsolutions.customclock.ui.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ClockTime(
    var hours: Double = 0.0,
    var minutes: Double = 0.0,
    var seconds: Double = 0.0,
    var isInitialDrawing: Boolean = true
) {

    fun getTimeZone(timeZoneKey: Int) = TIME_ZONE_MAP[timeZoneKey] ?: DEFAULT_TIMEZONE

    fun initializeDate(timeZone: String) {
        isInitialDrawing = true
        val date = Date()

        val hoursDateFormat = SimpleDateFormat(HOURS_PATTERN, Locale.ROOT)
        hoursDateFormat.timeZone = TimeZone.getTimeZone(timeZone)
        hours = hoursDateFormat.format(date).toDouble() % 12

        val minutesDateFormat = SimpleDateFormat(MINUTES_PATTERN, Locale.ROOT)
        minutesDateFormat.timeZone = TimeZone.getTimeZone(timeZone)
        minutes = minutesDateFormat.format(date).toDouble()

        val secondsDateFormat = SimpleDateFormat(SECONDS_PATTERN, Locale.ROOT)
        secondsDateFormat.timeZone = TimeZone.getTimeZone(timeZone)
        seconds = secondsDateFormat.format(date).toDouble()
    }

    fun updateDate() {
        seconds++

        if (seconds % 60.0 == 0.0) {
            minutes++
            seconds = 0.0
        }

        if (minutes % 60.0 == 0.0) {
            hours++
            minutes = 0.0
            hours %= 12
        }
    }

    fun reset() {
        isInitialDrawing = true
    }

    companion object {

        private val DEFAULT_TIMEZONE = Pair("GMT+03:00", "+3:00")
        private const val HOURS_PATTERN = "hh"
        private const val MINUTES_PATTERN = "mm"
        private const val SECONDS_PATTERN = "ss"
        private val TIME_ZONE_MAP = hashMapOf(
            1 to Pair("GMT-07:00", "-7:00"),
            2 to Pair("GMT-06:00", "-6:00"),
            3 to Pair("GMT-05:00", "-5:00"),
            4 to Pair("GMT-04:00", "-4:00"),
            5 to Pair("GMT-03:00", "-3:00"),
            6 to Pair("GMT", "0:00"),
            7 to Pair("GMT+01:00", "+1:00"),
            8 to Pair("GMT+02:00", "+2:00"),
            9 to Pair("GMT+03:00", "+3:00"),
            10 to Pair("GMT+04:00", "+4:00"),
            11 to Pair("GMT+05:30", "+5:30"),
            12 to Pair("GMT+08:00", "+8:00"),
            13 to Pair("GMT+09:00", "+9:00"),
            14 to Pair("GMT+10:00", "+10:00"),
            15 to Pair("GMT+12:00", "+12:00")
        )
    }
}