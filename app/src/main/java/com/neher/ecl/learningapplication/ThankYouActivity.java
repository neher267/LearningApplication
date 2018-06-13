package com.neher.ecl.learningapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ThankYouActivity extends AppCompatActivity {

    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thenk_you);

        SharedPreferences sharedPref = this.getSharedPreferences(Env.sp.sp_name, MODE_PRIVATE);
        score = findViewById(R.id.score);
        int game_score = sharedPref.getInt(Env.sp.game_score_en, 0) + sharedPref.getInt(Env.sp.game_score_math, 0);

        score.setText("Score: "+game_score);
    }
}
