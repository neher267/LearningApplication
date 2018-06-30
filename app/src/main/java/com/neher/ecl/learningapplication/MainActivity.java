package com.neher.ecl.learningapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mancj.slideup.SlideUp;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView wormUpButton;
    private TextView logInBtn;
    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPref;
    private Connectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "neher")
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle("Test Title")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        managerCompat.notify(1124578, builder.build());*/


        sharedPref= this.getSharedPreferences(Env.sp.sp_name, MODE_PRIVATE);
        wormUpButton = findViewById(R.id.worm_up_btn);
        logInBtn = findViewById(R.id.log_in_id);
        wormUpButton.setOnClickListener(this);
        logInBtn.setOnClickListener(this);

        connectivity = new Connectivity(MainActivity.this);

        String accessToken = sharedPref.getString(Env.sp.access_token, "no");

        Log.d(TAG, "Access Token: "+accessToken);

        Log.d(TAG, "User Name: "+sharedPref.getString(Env.sp.user_mobile, "0"));
        Log.d(TAG, "User Password: "+sharedPref.getString(Env.sp.user_password, "0"));

        /*MyNotifications notifications = new MyNotifications(this);

        notifications.sentNotification();*/

        if(!accessToken.equals("no"))
        {
            if(connectivity.getConnectionStatus())
            {
                Log.d(TAG, "Connection Status: Yes");
                Log.d(TAG, "Downloading new questions");
                new ObjectRequestForQuestions(MainActivity.this).getResponse();
            }
            else
            {
                startActivity(new Intent(this, SubjectSelectActivity.class));
            }

            finish();

        }
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.worm_up_btn)
        {
            String wormUpQsnDownloaded = sharedPref.getString(Env.sp.worm_up_qsn_downloaded, "no");

            Log.d(TAG, "Worm Up Questions Downloaded: "+wormUpQsnDownloaded);

            if(wormUpQsnDownloaded.equals("no"))
            {
                if(connectivity.getConnectionStatus())
                {
                    Log.d(TAG, "Connection Status: Yes");
                    Log.d(TAG, "Worm Up Questions downloading...");
                    new ObjectRequestForWormUpQuestions(this).getResponse(Env.remote.warm_up_url);
                }
                else
                {
                    showSnackbarForInternetConnection(view);
                }
            }
            else{
                Log.d(TAG, "Worm up Activity is calling");
                startActivity(new Intent(MainActivity.this, WormUpQuestionActivity.class));
                finish();
            }
        }
        else if(view.getId() == R.id.log_in_id)
        {
            Connectivity connectivity = new Connectivity(MainActivity.this);
            if(connectivity.getConnectionStatus())
            {
                Log.d(TAG, "Connection Status: Yes");
                startActivity(new Intent(this, LoginActivity.class));
            }
            else
            {
                showSnackbarForInternetConnection(view);
            }
        }
    }

    public void showSnackbarForInternetConnection(View view)
    {
        Snackbar snackbar = Snackbar.make(view, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Settings", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });

        snackbar.show();
    }

}
