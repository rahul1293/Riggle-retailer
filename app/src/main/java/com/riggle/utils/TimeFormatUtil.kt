package com.riggle.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String.getDateFromUtc(): Date? {

    val df = SimpleDateFormat(Constants.DataKeys.DATE_TIME_TIMEZONE, Locale.ENGLISH)
    df.timeZone = TimeZone.getTimeZone("UTC")

    if (this.isEmpty()) {
        return null
    }

    try {
        return df.parse(this)
    } catch (ex: ParseException) {
        // ex.printStackTrace()
        try {
            val df = SimpleDateFormat(Constants.DataKeys.DATE_TIME, Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            return df.parse(this)
        } catch (ex: ParseException) {
            ex.printStackTrace()
            return null
        }
    }

}

fun Date.date(): String? {
    val df =
        SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    return df.format(this)
}

fun Date.time(): String? {
    val df =
        SimpleDateFormat("HH:mm ", Locale.getDefault())

    return df.format(this)
}

fun String?.getServerFormat(): String?{

    return this?.changeDateFormat()

}

fun String.changeDateFormat(): String? {

    val df = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)

    if (this.isEmpty()) {
        return null
    }

    var dateFrom : Date? = null
    try {
        dateFrom = df.parse(this)
    } catch (ex: ParseException) {
         ex.printStackTrace()
    }

    val df2 =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return df2.format(dateFrom)

}

