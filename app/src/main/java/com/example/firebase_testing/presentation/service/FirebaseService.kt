package com.example.firebase_testing.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.firebase_testing.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

private const val TAG = "FirebaseService"
class FirebaseService: FirebaseMessagingService() {

    //Apps that use Firebase Cloud Messaging should implement onNewToken()
    // in order to observe token changes
    /**
    Called when a new token for the default Firebase project is generated.

    This is invoked after app install when a token is first generated, and again if the token changes.
     */
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    /**
    Called when a message is received.

    This is also called when a notification message is received while the app is in the foreground.
    The notification parameters can be retrieved with RemoteMessage.getNotification().
     */
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.i(TAG, "onMessageReceived: $p0")

        p0.notification?.body?.let {
            sendNotification(it)
        }
    }

    /**
     * Called when the Firebase Cloud Messaging server deletes pending messages. This may be due to:

    Too many messages stored on the Firebase Cloud Messaging server. This can occur when the app's
    servers send a bunch of non-collapsible messages to Firebase Cloud Messaging servers while the device is offline.
    The device hasn't connected in a long time and the app server has recently
    (within the last 4 weeks) sent a message to the app on that device.

    It is recommended that the app do a full sync with the app server after receiving this call
     */
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
    }

    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)
    }

    override fun handleIntent(intent: Intent) {
        super.handleIntent(intent)
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}