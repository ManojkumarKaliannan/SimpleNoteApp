package com.task.noteapp.utills

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.ShowSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun getCurrentDateAndTime(currentTimeMillis: Long): String {
    TimeZone.getDefault()
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss a", Locale.getDefault())
    return dateFormat.format( Date(currentTimeMillis))
}
fun getCurrentDate(currentTimeMillis: Long): String {
    TimeZone.getDefault()
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format( Date(currentTimeMillis))
}

