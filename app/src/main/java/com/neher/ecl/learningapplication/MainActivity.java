package com.neher.ecl.learningapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView wormUpButton;
    private TextView logInBtn;
    private static final String TAG = "MainActivity";
    private SharedPreferences sharedPref;
    private Connectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref= this.getSharedPreferences(Env.USER_INFO_SHARD_PRE, MODE_PRIVATE);
        wormUpButton = findViewById(R.id.worm_up_btn);
        logInBtn = findViewById(R.id.log_in_id);
        wormUpButton.setOnClickListener(this);
        logInBtn.setOnClickListener(this);

        connectivity = new Connectivity(MainActivity.this);

        Log.d(TAG, "access_token: "+sharedPref.getString("access_token", "no"));
        Log.d(TAG, "main_score: "+sharedPref.getInt("main_score", 0));

        if(!sharedPref.getString("access_token", "no").equals("no"))
        {
            finish();
            if(connectivity.getConnectionStatus())
            {
                Log.d(TAG, "Connection Status: Yes");
                Log.d(TAG, "Download new questions");
                int last_id = sharedPref.getInt(Env.LAST_DOWNLOAD_QSN_ID, 0);
                new ObjectRequestForQuestions(MainActivity.this).getResponse(Env.QUESTIONS_URL, last_id);
            }
            else
            {
                startActivity(new Intent(this, QuestionActivity.class));
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.worm_up_btn)
        {
            Log.d(TAG, "worm_up_qsn_download: "+sharedPref.getString("worm_up_qsn_download", "no"));

            if(sharedPref.getString("worm_up_qsn_download", "no").equals("no"))
            {
                if(connectivity.getConnectionStatus())
                {
                    Log.d(TAG, "Connection Status: Yes");
                    new ObjectRequestForWormUpQuestions(this).getResponse(Env.WARM_UP_URL);
                    Log.d(TAG, "Worm Up Questions download and saved in database");
                }
                else
                {
                    Toast.makeText(this, "Please Check Your Internet Connection!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "Please Check Your Internet Connection!", Toast.LENGTH_LONG).show();
            }

        }
    }


}
