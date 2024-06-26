package edu.bluejack23_2.demarj.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.activities.IntroActivity

const val channelId = "notification_channel"
const val channelName = "edu.bluejack23_2.demarj.firebase"

class MyFirebaseNotification : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoveView(store_name: String, po_name: String): RemoteViews {
        val remoteView = RemoteViews("edu.bluejack23_2.demarj", R.layout.notification_layout)

        remoteView.setTextViewText(R.id.store_name_notif_hp, store_name)
        remoteView.setTextViewText(R.id.po_name_notif_hp, po_name)
        remoteView.setImageViewResource(R.id.logo_notif_hp, R.drawable.icon_apps)

        return remoteView
    }

    fun generateNotification(store_name: String, po_name: String) {
        val intent = Intent(this, IntroActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.icon_apps)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoveView(store_name, po_name))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }
}
