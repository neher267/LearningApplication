package com.neher.ecl.learningapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView wormUpButton;
    private static final String TAG = "MainActivity";
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref= this.getSharedPreferences(Env.USER_INFO_SHARD_PRE, MODE_PRIVATE);
        wormUpButton = findViewById(R.id.worm_up_btn);
        wormUpButton.setOnClickListener(this);


        Log.d(TAG, sharedPref.getString("access_token", "no"));

        if(sharedPref.getString("access_token", "no") == "no")
        {
            startActivity(new Intent(this, RegisterActivity.class));
            startActivity(new Intent(this, LoginActivity.class));
        }



    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.worm_up_btn)
        {
            Log.d(TAG, sharedPref.getString("worm_up_qsn_download", "no"));
            if(sharedPref.getString("worm_up_qsn_download", "no") == "no")
            {
                new MyJsonObjectRequest(this).getResponse(Env.WARM_UP_URL);
                Log.d(TAG, "Worm Up Questions download and saved in database");
            }

            startWormUpTest();
        }
    }

    public void startWormUpTest()
    {
        Log.d(TAG, "Worm up Test started.");
        startActivity(new Intent(this, QuestionActivity.class));
    }
}
