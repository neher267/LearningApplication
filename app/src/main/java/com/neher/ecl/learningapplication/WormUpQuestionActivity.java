package com.neher.ecl.learningapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WormUpQuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = WormUpQuestionActivity.class.getSimpleName();

    private TextView continue_btn;
    private TextView scoreView;
    private RadioButton option_1;
    private RadioButton option_2;
    private RadioButton option_3;
    private RadioButton option_4;

    private TextView question;
    private QuestionDB questionDB;
    private Cursor cursor;

    private int worm_up_marks;
    private int worm_up_marks_weight;

    private int worm_up_wrong_ans;
    private int worm_up_wrong_ans_weight;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question = findViewById(R.id.question_id);
        scoreView = findViewById(R.id.score_id);
        option_1 = findViewById(R.id.option_1);
        option_2 = findViewById(R.id.option_2);
        option_3 = findViewById(R.id.option_3);
        option_4 = findViewById(R.id.option_4);
        continue_btn = findViewById(R.id.continue_id);

        option_1.setOnClickListener(this);
        option_2.setOnClickListener(this);
        option_3.setOnClickListener(this);
        option_4.setOnClickListener(this);
        continue_btn.setOnClickListener(this);

        sharedPref= this.getSharedPreferences(Env.sp.sp_name, MODE_PRIVATE);

        editor = sharedPref.edit();

        worm_up_marks = sharedPref.getInt("worm_up_marks", 0);
        worm_up_marks_weight = sharedPref.getInt("worm_up_marks_weight", 0);

        worm_up_wrong_ans = sharedPref.getInt("worm_up_wrong_ans", 0);
        worm_up_wrong_ans_weight = sharedPref.getInt("worm_up_wrong_ans_weight", 0);

        if(!sharedPref.getString("access_token", "no").equals("no"))
        {
            finish();
            startActivity(new Intent(this, QuestionActivity.class));
        }
        else
        {
            questionDB = new QuestionDB(this);
            questionDB.getWritableDatabase();
            nextQuestion();
        }

        Log.d(TAG, "onCreate method is calling.");

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.option_1){
            Log.d(TAG, "Option one is clicked");
            checkResult("a");
            option_1.setChecked(false);

        }else if(view.getId() == R.id.option_2){
            Log.d(TAG, "Option two is clicked");
            checkResult("b");
            option_2.setChecked(false);

        }else if(view.getId() == R.id.option_3){
            Log.d(TAG, "Option three is clicked");
            checkResult("c");
            option_3.setChecked(false);


        }else if(view.getId() == R.id.option_4){
            Log.d(TAG, "Option fore is clicked");
            checkResult("d");
            option_4.setChecked(false);

        }else if(view.getId() == R.id.continue_id){
            Log.d(TAG, "Option continue button is clicked");
            checkResult("continue");
        }
    }



    public void nextQuestion()
    {
        Log.d(TAG, "nextQuestion method is calling");

        cursor = questionDB.getWormUpQuestion();

        if (cursor.moveToFirst()) {
            String qsn = cursor.getString(cursor.getColumnIndex(QuestionDB.COL_QUESTION));
            question.setText(qsn);
            option_1.setText(cursor.getString(cursor.getColumnIndex(QuestionDB.COL_OPTION_A)));
            option_2.setText(cursor.getString(cursor.getColumnIndex(QuestionDB.COL_OPTION_B)));
            option_3.setText(cursor.getString(cursor.getColumnIndex(QuestionDB.COL_OPTION_C)));
            option_4.setText(cursor.getString(cursor.getColumnIndex(QuestionDB.COL_OPTION_D)));

            Log.d(TAG, qsn);
        }

        else {
            finish();
            Log.d(TAG, "Worm Up Test is over.");
            Intent intent = new Intent(WormUpQuestionActivity.this, WormUpResultActivity.class);
            intent.putExtra("worm_up_marks", worm_up_marks);
            startActivity(intent);

        }
    }



    public void checkResult(String user_ans){
        Log.d(TAG, user_ans);
        if(cursor.moveToFirst())
        {
            questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.wormup_read_question);

            String ans = cursor.getString(cursor.getColumnIndex(QuestionDB.COL_ANS));
            Log.d(TAG, ans);

            if(ans.equals(user_ans))
            {
                worm_up_marks_weight += cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_WEIGHT));
                ++worm_up_marks;

                editor.putInt("worm_up_marks", worm_up_marks);
                editor.putInt("worm_up_marks_weight", worm_up_marks_weight);
                editor.commit();

                scoreView.setText("Score: "+worm_up_marks);
                showDialog("Great!", "Your answer is correct");

                nextQuestion();
            }

            else if(user_ans.equals("continue"))
            {
                questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.wormup_read_question);
                nextQuestion();
            }

            else{
                worm_up_wrong_ans_weight += cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_WEIGHT));
                ++worm_up_wrong_ans;

                editor.putInt("worm_up_wrong_ans", worm_up_wrong_ans);
                editor.putInt("worm_up_wrong_ans_weight", worm_up_wrong_ans_weight);
                editor.commit();

                showDialog("Opps!","Your answer is wrong!");
                nextQuestion();
            }
        }
        else
        {
            Log.d(TAG, "There is no Question");
        }

    }



    public void showDialog(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create();
        builder.show();
    }
}
