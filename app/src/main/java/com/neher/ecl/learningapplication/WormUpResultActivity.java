package com.neher.ecl.learningapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WormUpResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worm_up_result);

        TextView resultView = findViewById(R.id.result_id);
        TextView registerBtn = findViewById(R.id.register_id);

        String result = String.valueOf(getIntent().getIntExtra("worm_up_marks", 0));

        resultView.setText("Congratulations! \n Your Worm UP Score is:"+result);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(WormUpResultActivity.this, RegisterActivity.class));
            }
        });
    }
}
