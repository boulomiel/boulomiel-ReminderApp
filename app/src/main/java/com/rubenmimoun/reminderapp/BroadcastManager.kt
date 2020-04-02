package com.rubenmimoun.reminderapp

import android.R
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class BroadcastManager : BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val yourDate = intent?.getStringExtra("date")
            val yourHour = intent?.getStringExtra("hour")
            val toDo = intent?.getStringExtra("todo")
            val date: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            val hour: DateFormat = SimpleDateFormat("HH:mm:ss")
            if (date.equals(yourDate) && hour.equals(yourHour)) {
                val it = Intent(context, MainActivity::class.java)
                createNotification(context!!, it, "Don't Forget !", "Event of the day : ", toDo)
            }
        } catch (e: java.lang.Exception) {
            Log.i("date", "error == " + e.message)
        }
    }

    fun createNotification(
        context: Context,
        intent: Intent?,
        ticker: CharSequence?,
        title: CharSequence?,
        content : CharSequence?
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val p = PendingIntent.getActivity(context, 0, intent, 0)
        val builder = NotificationCompat.Builder(context)
        builder.setTicker(ticker)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.alert_dark_frame)
        builder.setContentIntent(p)
        val n: Notification = builder.build()
        //create the notification
        n.vibrate = longArrayOf(150, 300, 150, 400)
        n.flags = Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(R.drawable.alert_light_frame, n)
        //create a vibration
        try {
            val som: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val toque = RingtoneManager.getRingtone(context, som)
            toque.play()
        } catch (e: Exception) {
        }
    }
}


