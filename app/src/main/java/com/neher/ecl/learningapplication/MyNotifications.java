package com.neher.ecl.learningapplication;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class MyNotifications {
    private NotificationManagerCompat notificationManagerCompat;
    private Context context;

    public MyNotifications(Context context) {
        this.context = context;
    }

    public void sentNotification(){
        notificationManagerCompat = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_satisfied)
                .setContentTitle("Success!")
                .setContentText("You are successfully registered in Learner Application. Thank you and best of luck. Go Ahead!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .build();

        notificationManagerCompat.notify(1, notification);

    }

}
