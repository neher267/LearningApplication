package com.neher.ecl.learningapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView continue_btn;
    private TextView scoreView;
    private RadioButton option_1;
    private RadioButton option_2;
    private RadioButton option_3;
    private RadioButton option_4;

    private TextView question;
    private QuestionDB questionDB;
    private Cursor cursor;

    private int score;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String TAG = QuestionActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        sharedPref= this.getSharedPreferences(Env.sp.sp_name, MODE_PRIVATE);

        editor = sharedPref.edit();

        score = sharedPref.getInt("main_score", 0);

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

        questionDB = new QuestionDB(this);
        questionDB.getWritableDatabase();

        /*Cursor cursor = questionDB.index();
        while (cursor.moveToNext())
        {
            String qsn = cursor.getString(cursor.getColumnIndex(QuestionDB.COL_QUESTION));
            String status = cursor.getString(cursor.getColumnIndex(QuestionDB.COL_STATUS));
            int id = cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID));

            Log.d(TAG, "Id: "+String.valueOf(id)+" "+qsn+" : Status = "+status);
        }*/

        nextQuestion();

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
        cursor = questionDB.getQuestion();

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
            Log.d(TAG, "There is now row");
            startActivity(new Intent(this, ThankYouActivity.class));
        }
    }



    public void checkResult(String user_ans){
        Log.d(TAG, user_ans);

        if(cursor.moveToFirst())
        {
            String ans = cursor.getString(cursor.getColumnIndex(QuestionDB.COL_ANS));
            Log.d(TAG, ans);

            if(ans.equals(user_ans))
            {
                questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.right_ans);
                score++;
                editor.putInt("main_score", score);
                editor.commit();
                scoreView.setText("Score: "+score);
                showDialog("Great!", "Your answer is correct");
                nextQuestion();
            }
            else if(user_ans.equals("continue"))
            {
                questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.read_question);
                nextQuestion();
            }
            else{
                questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.error_ans);
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
