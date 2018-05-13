package com.neher.ecl.learningapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


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
        sharedPref= this.getSharedPreferences(Env.sp.sp_name, MODE_PRIVATE);
        wormUpButton = findViewById(R.id.worm_up_btn);
        logInBtn = findViewById(R.id.log_in_id);
        wormUpButton.setOnClickListener(this);
        logInBtn.setOnClickListener(this);

        connectivity = new Connectivity(MainActivity.this);

        String accessToken = sharedPref.getString(Env.sp.access_token, "no");

        Log.d(TAG, "Access Token: "+accessToken);
        Log.d(TAG, "Game Score: "+sharedPref.getInt(Env.sp.game_score, 0));

        if(!accessToken.equals("no"))
        {
            finish();
            if(connectivity.getConnectionStatus())
            {
                Log.d(TAG, "Connection Status: Yes");
                Log.d(TAG, "Downloading new questions");
                new UpdateUserInfo(MainActivity.this).getResponse();
                new ObjectRequestForQuestions(MainActivity.this).getResponse();
            }
            else
            {
                startActivity(new Intent(this, GameActivity.class));
            }
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
            }
        }
        else if(view.getId() == R.id.log_in_id)
        {
            Connectivity connectivity = new Connectivity(MainActivity.this);
            if(connectivity.getConnectionStatus())
            {
                Log.d(TAG, "Connection Status: Yes");
                finish();
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
