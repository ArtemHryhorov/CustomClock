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

        private const val DEFAULT_TIMEZONE = "GMT+03:00"
        private const val HOURS_PATTERN = "hh"
        private const val MINUTES_PATTERN = "mm"
        private const val SECONDS_PATTERN = "ss"
        private val TIME_ZONE_MAP = hashMapOf(
            1 to "GMT-07:00",
            2 to "GMT-06:00",
            3 to "GMT-05:00",
            4 to "GMT-04:00",
            5 to "GMT-03:00",
            6 to "GMT",
            7 to "GMT+01:00",
            8 to "GMT+02:00",
            9 to "GMT+03:00",
            10 to "GMT+04:00",
            11 to "GMT+05:30",
            12 to "GMT+08:00",
            13 to "GMT+09:00",
            14 to "GMT+10:00",
            15 to "GMT+12:00"
        )
    }
}