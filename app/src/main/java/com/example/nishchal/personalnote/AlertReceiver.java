package com.example.nishchal.personalnote;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver{

    String reminder, additional_note;
    int id;

    @Override
    public void onReceive(Context context, Intent intent) {

        id = (int) intent.getExtras().get("requestcode");
        reminder = intent.getExtras().getString("reminder");
        additional_note = intent.getExtras().getString("additional_note");

        createNotification(context, reminder, additional_note, "Reminder");

    }

    private void createNotification(Context context, String msg, String msgText, String msgAlert) {

        PendingIntent notifiIntent = PendingIntent.getActivity(context, id, new Intent(context,MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_calender)
                .setContentTitle(msg)
                .setContentText(msgText)
                .setTicker(msgAlert);

        mBuilder.setContentIntent(notifiIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());

    }
}