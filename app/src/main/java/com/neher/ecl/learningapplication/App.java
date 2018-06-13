package com.neher.ecl.learningapplication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App  extends Application{

    public static final String CHANNEL_1_ID =  "cahannel_1";
    public static final String CHANNEL_2_ID =  "cahannel_2";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
           channel1.setDescription("This is Channel One.");

           NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Channel Tow", NotificationManager.IMPORTANCE_LOW);
           channel2.setDescription("This is chanel two");

           NotificationManager manager = getSystemService(NotificationManager.class);

           manager.createNotificationChannel(channel1);
           manager.createNotificationChannel(channel2);
        }
    }
}