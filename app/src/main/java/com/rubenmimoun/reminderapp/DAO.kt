package com.rubenmimoun.reminderapp

import android.content.Context
import android.view.Window
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun isDateValidFormat(value: String): Boolean {
    var date: Date? = null

    try {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        date = sdf.parse(value)
        if (value != sdf.format(date)) {
            date = null
        }
    } catch (ex: ParseException) {
        ex.printStackTrace()
    }
    return date != null
}

fun isHourValidFormat(value: String): Boolean {
    var date: Date? = null

    try {
        val sdf = SimpleDateFormat("hh:mm")
        date = sdf.parse(value)
        if (value != sdf.format(date)) {
            date = null
        }
    } catch (ex: ParseException) {
        ex.printStackTrace()
    }
    return date != null
}