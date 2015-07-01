package com.example.adam.pubtrans.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.MainActivity;

/**
 * Created by Adam on 1/07/2015.
 */
public class StopAlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        RingtoneManager ringtoneManager;
        ringtoneManager = new RingtoneManager(context);
        ringtoneManager.stopPreviousRingtone();

        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
