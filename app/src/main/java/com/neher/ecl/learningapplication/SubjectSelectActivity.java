package com.neher.ecl.learningapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SubjectSelectActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_select_activity);

        TextView englishSubjectView = findViewById(R.id.subject_id_en);
        TextView mathSubjectView = findViewById(R.id.subject_id_math);

        englishSubjectView.setOnClickListener(this);
        mathSubjectView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPref= this.getSharedPreferences(Env.sp.sp_name, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        if (view.getId() == R.id.subject_id_en)
        {
            editor.putInt(Env.sp.subject, Env.sp.subject_en_val);
        }

        else
        {
            editor.putInt(Env.sp.subject, Env.sp.subject_math_val);
        }

        finish();

        editor.commit();

        startActivity(new Intent(SubjectSelectActivity.this, GameActivity.class));
    }
}
