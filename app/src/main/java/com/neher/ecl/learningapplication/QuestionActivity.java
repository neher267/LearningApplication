package com.neher.ecl.learningapplication;

import android.content.DialogInterface;
import android.content.Intent;
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

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{


    private Button continue_btn;
    private Button login_btn;

    private RadioButton option_1;
    private RadioButton option_2;
    private RadioButton option_3;
    private RadioButton option_4;

    private TextView question;
    private TextView result;

    private Question nextQuestion;

    private ArrayList<Question> questions = new ArrayList<>();

    private ArrayList<Question> questionList = new ArrayList<>();
    private QuestionDB questionDB;

    private RequestQueue requestQueue;

    private static final String TAG = "MainActivity";
    private String url = "http://192.168.0.196/learning-game/public/json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        requestQueue = Volley.newRequestQueue(this);
        question = findViewById(R.id.question_id);
        option_1 = findViewById(R.id.option_1);
        option_2 = findViewById(R.id.option_2);
        option_3 = findViewById(R.id.option_3);
        option_4 = findViewById(R.id.option_4);
        continue_btn = findViewById(R.id.continue_id);
        login_btn = findViewById(R.id.login_id);

        option_1.setOnClickListener(this);
        option_2.setOnClickListener(this);
        option_3.setOnClickListener(this);
        option_4.setOnClickListener(this);
        continue_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        result = findViewById(R.id.result);
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "ok");
        questions = Question.getQuestions();
        startGame(questions);
        nextQuestion = questions.get(1);
        nextQuestion(nextQuestion);
        questionDB = new QuestionDB(this);
        questionDB.getWritableDatabase();

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.option_1){
            Log.d(TAG, "Option one is clicked");
            showDialog("ok");


        }else if(view.getId() == R.id.option_2){
            Log.d(TAG, "Option two is clicked");


        }else if(view.getId() == R.id.option_3){
            Log.d(TAG, "Option three is clicked");


        }else if(view.getId() == R.id.option_4){
            Log.d(TAG, "Option fore is clicked");


        }else if(view.getId() == R.id.login_id){
            Log.d(TAG, "Login button is clicked");
            //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(QuestionActivity.this, LoginActivity.class));


        }else if(view.getId() == R.id.continue_id){
            Log.d(TAG, "Option continue button is clicked");
            //ArrayList<Question> questions =  jsonParse();

            final Cursor questions = questionDB.index();

            if (questions.getCount()>0)
            {
                Toast.makeText(this,  String.valueOf(questions.getCount()), Toast.LENGTH_SHORT).show();
                Log.d(TAG, String.valueOf(questions.getCount()));

            }
            else {
                Log.d(TAG, "There is now row");
            }

            while (questions.moveToNext())
            {
                new Thread(new Runnable() {
                    public void run() {

                    }
                }).start();


                showDialog("he he");
                Log.d(TAG, questions.getString(2));
                Toast.makeText(this,  questions.getString(2), Toast.LENGTH_SHORT).show();
            }

            //jsonParse();

            //nextQuestion(questions.get(2));
        }
    }

    public void jsonParse()
    {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String qus = jsonObject.getString("question");
                                String sub = jsonObject.getString("subject");
                                String option_1 = jsonObject.getString("a");
                                String option_2 = jsonObject.getString("b");
                                String option_3 = jsonObject.getString("c");
                                String option_4 = jsonObject.getString("d");
                                String ans = jsonObject.getString("ans");

                                int weight = jsonObject.getInt("weight");

                                //result.append(qus + " "+ ans +" "+weight);
                                Question question = new Question(qus, sub, option_1, option_2, option_3,option_4, ans,weight);
                                questionList.add(question);
                                questionDB.store(question);
                                Log.d(TAG, question.getQuestion());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(objectRequest);
    }

    public void startGame(ArrayList<Question> questions){

        for(Question item : questions){
            question.setText("Q: "+item.getQuestion());
            option_1.setText(item.getOption_1());
            option_2.setText(item.getOption_2());
            option_3.setText(item.getOption_3());
            option_4.setText(item.getOption_4());
        }

    }

    public void nextQuestion(Question item){
        question.setText(item.getQuestion());
        option_1.setText(item.getOption_1());
        option_2.setText(item.getOption_2());
        option_3.setText(item.getOption_3());
        option_4.setText(item.getOption_4());
    }

    public void checkResult(){

    }

    public void showDialog(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Great!");
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
