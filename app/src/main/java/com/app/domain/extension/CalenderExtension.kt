@file:JvmName("DateUtils")

package com.app.domain.extension

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.ParseException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object D {
    val FORMAT = "yyyy-MM-dd HH:mm:ss"
}

val now get() = Calendar.getInstance().time
val calendar get() = Calendar.getInstance()

val Long.toDate get() = Date(this)
val Long.toUTC get() = toDate.toUTC
val time = now.time

val currentSecond get() = TimeUnit.MILLISECONDS.toSeconds(time)
val currentMinute get() = TimeUnit.MILLISECONDS.toMinutes(time)
val currentHour get() = TimeUnit.MILLISECONDS.toHours(time)

fun Date.isMonday() = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
fun Date.isTuesday() = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY
fun Date.isWednesday() = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY
fun Date.isThursday() = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY
fun Date.isFriday() = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
fun Date.isSaturday() = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
fun Date.isSunday() = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY

fun formatDate(
    format: String = D.FORMAT,
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getDefault()
): DateFormat {
    val sdf = SimpleDateFormat(format, locale)
    sdf.timeZone = timeZone
    return sdf
}

fun String?.parseDate(
    format: String = D.FORMAT,
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getDefault()
): Date? {
    return try {
        formatDate(format, locale, timeZone).parse(this!!)
    } catch (e: Exception) {
        null
    }

}

fun Long.parseDate(
    format: String = D.FORMAT,
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getDefault()
): String? {
    return try {
        formatDate(format, locale, timeZone).format(Date(this))
    } catch (e: Exception) {
        null
    }
}

//val currentUTC: Date
//    get() {
//        val calendar = calendar()
//        val ro = calendar.timeZone.rawOffset
//        val dst = calendar.timeZone.dstSavings
//        val isDayLight = TimeZone.getDefault().inDaylightTime(
//            calendar.time
//        )
//        var gmtMillis = calendar.timeInMillis - ro
//        if (isDayLight) {
//            gmtMillis = calendar.timeInMillis - ro.toLong() - dst.toLong()
//        }
//        return Date(gmtMillis)
//    }

val currentUTC: Date
    get() {
        return now.toUTC
    }

fun currentUTC(
    format: String = D.FORMAT,
    locale: Locale = Locale.getDefault()
): String {
    return formatDate(
        format,
        locale,
        TimeZone.getDefault()
    ).format(now.toUTC)
}

fun getDateFromToday(days: Int): String {
    var d = ""
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, days)
    val utcString = cal.toUTCFormat()

    utcString.parseDate(D.FORMAT)?.let {
        formatDate("EEE").format(it).let {
            d = it
        }
        formatDate("dd, $ yyyy  ", Locale.ENGLISH).format(it).let {
            d = d.plus(" ").plus(it).trim()
        }
        formatDate("MMM ").format(it).let {
            d = d.replace("$", it).trim()
        }
    }
    return d
}

fun Calendar.toUTCFormat(
    format: String = D.FORMAT,
    locale: Locale = Locale.getDefault()
): String {
    return formatDate(
        format,
        locale,
        TimeZone.getDefault()
    ).format(this.time.toUTC)
}

val Date.toUTC: Date
    get() {
        val calendar = calendar
        val ro = calendar.timeZone.rawOffset
        val dst = calendar.timeZone.dstSavings
        val isDayLight = TimeZone.getDefault().inDaylightTime(
            this
        )
        var gmtMillis = time - ro
        if (isDayLight) {
            gmtMillis = time - ro.toLong() - dst.toLong()
        }
        return Date(gmtMillis)
    }

fun tomorrow(): Date {
    val cal = calendar
    cal.add(Calendar.DAY_OF_YEAR, 1)
    return cal.time
}

fun addDays(days: Int): Date {
    val cal = calendar
    cal.add(Calendar.DAY_OF_YEAR, days)
    return cal.time
}

fun TimeUnit.days(diff: Long): Long {
    var days = TimeUnit.MILLISECONDS.toDays(diff)
    days / 365
    days %= 365
    days / 30
    days %= 30
    days / 7
    days %= 7
    return days
}

fun TimeUnit.year(diff: Long): Long {
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return days / 365
}

fun TimeUnit.week(diff: Long): Long {
    var days = TimeUnit.MILLISECONDS.toDays(diff)
    days / 365
    days %= 365
    days / 30
    days %= 30
    return days / 7
}

fun TimeUnit.months(diff: Long): Long {
    var days = TimeUnit.MILLISECONDS.toDays(diff)
    days / 365
    days %= 365
    return days / 30
}

fun String.toDate(
    format: String = D.FORMAT,
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getDefault()
): Date? {
    val dateFormatter = formatDate(format, locale, timeZone)
    return try {
        dateFormatter.parse(this)
    } catch (e: ParseException) {
        null
    }
}

fun Date.toString(
    format: String = D.FORMAT,
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getDefault()
): String {
    return formatDate(format, locale, timeZone).format(this)
}

fun Context.showDatePicker(
    callback: (outputDate: String) -> Unit,
    date: Int,
    startDate: String,
    endDate: String
) {

    val cldr: Calendar = Calendar.getInstance()
    val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
    val month: Int = cldr.get(Calendar.MONTH)
    val year: Int = cldr.get(Calendar.YEAR)
    val datePickerDialog = DatePickerDialog(
        this,
        AlertDialog.THEME_DEVICE_DEFAULT_DARK,
        { _, y, monthOfYear, dayOfMonth ->
            val mm = monthOfYear + 1
            var m = ""
            m = if (mm < 10) {
                "0$mm"
            } else {
                mm.toString()
            }
            val dd = if (dayOfMonth < 10) {
                "0$dayOfMonth"
            } else {
                dayOfMonth.toString()
            }
            callback("$dd/$m/$y")
        },
        year,
        month,
        day
    )

    when (date) {
        0 -> {
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        }
        1 -> {
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        }
        2 -> {
            cldr.add(Calendar.YEAR, -18)
            datePickerDialog.datePicker.maxDate = cldr.timeInMillis
        }
        3 -> {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            datePickerDialog.datePicker.minDate = sdf.parse(startDate)?.time!!
            datePickerDialog.datePicker.maxDate = sdf.parse(endDate)?.time!!
        }
        else -> {
            cldr.add(Calendar.YEAR, +5)
            datePickerDialog.datePicker.maxDate = cldr.timeInMillis
        }
    }
    datePickerDialog.show()
}

fun Context.showTimePicker(
    callback: (outputDate: String) -> Unit,
    time: Int,
    startTime: String,
    endTime: String
) {

    val cldr: Calendar = Calendar.getInstance()
    val mHour: Int = cldr.get(Calendar.HOUR_OF_DAY)
    val mMinute: Int = cldr.get(Calendar.MINUTE)

    // Launch Time Picker Dialog
    val timePickerDialog = TimePickerDialog(
        this,
        AlertDialog.THEME_DEVICE_DEFAULT_DARK,
        { _, hourOfDay, minute ->
            callback(convert24To12System(hourOfDay, minute)!!)
        }, mHour, mMinute, false
    )

    when (time) {
        0 -> {

        }
        else -> {

        }
    }
    timePickerDialog.show()
}


fun convert24To12System(hour: Int, minute: Int): String? {
    var hour = hour
    var time = ""
    var am_pm = ""
    if (hour < 12) {
        if (hour == 0) hour = 12
        am_pm = "AM"
    } else {
        if (hour != 12) hour -= 12
        am_pm = "PM"
    }
    var h = hour.toString() + ""
    var m = minute.toString() + ""
    if (h.length == 1) h = "0$h"
    if (m.length == 1) m = "0$m"
    time = "$h:$m $am_pm"
    return time
}



