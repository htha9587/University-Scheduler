package com.example.universityscheduler.Util;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.universityscheduler.MainActivity;
import com.example.universityscheduler.R;

/**
 * BroadcastReceiver class for the University Scheduler Alarms.
 */
public class AlarmsReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private static int NOTIFICATION_ID;
    private static final String primary_notification_channel = "primary_notification_channel";

    /**
     * Whenever a broadcast is received, this method is called, sending the app alerts.
     * @param context = the context of the receiver.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

             intent = new Intent(context, MainActivity.class);

            NOTIFICATION_ID = intent.getIntExtra("notification_id", -1);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity
                    (context, NOTIFICATION_ID, intent, PendingIntent
                            .FLAG_IMMUTABLE);

            String notificationTitle = intent.getStringExtra("mNotificationTitle");
            String notificationText = intent.getStringExtra("mNotificationText");

            initNotificationChannel(context, primary_notification_channel);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, primary_notification_channel)
                    .setSmallIcon(R.drawable.img_2)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setChannelId("notification_id")
                    .setContentText(notificationText)
                    .setContentTitle(notificationTitle);

            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID++, notification.build());
        }
        else {
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity
                    (context, NOTIFICATION_ID, intent, PendingIntent
                            .FLAG_UPDATE_CURRENT);

            String notificationTitle = intent.getStringExtra("mNotificationTitle");
            String notificationText = intent.getStringExtra("mNotificationText");

            initNotificationChannel(context, primary_notification_channel);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, primary_notification_channel)
                    .setSmallIcon(R.drawable.img_2)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setChannelId("notification_id")
                    .setContentText(notificationText)
                    .setContentTitle(notificationTitle);

            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID++, notification.build());
        }

    }

    /**
     * Alert notification channel for the WGU Scheduler.
     */
    private void initNotificationChannel(Context context, String PRIMARY_NOTIFICATION_CHANNEL) {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.S) {

            CharSequence alertName = "University Scheduler App Alert Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_NOTIFICATION_CHANNEL, alertName, importance);
            notificationChannel.setDescription("This will notify you about your Course and Assessment start/due dates. Please keep track of them.");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
