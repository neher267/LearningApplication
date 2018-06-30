package com.neher.ecl.learningapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import static java.lang.Thread.sleep;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private TextView continue_btn;
    private TextView scoreView;
    private RadioButton option_1;
    private RadioButton option_2;
    private RadioButton option_3;
    private RadioButton option_4;

    private TextView question;
    private QuestionDB questionDB;
    private Cursor cursor;
    private ProgressBar progressBar;

    private int game_score_math, game_score_en;
    private int game_error_math, game_error_en;
    private int subject;
    private int solveTime;
    private Connectivity connectivity;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String TAG = GameActivity.class.getSimpleName();

    private int time;

    //private View slideView;
    //private SlideUp slideUp;

    private boolean clicked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setup();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView userNameView = headerView.findViewById(R.id.user_name_view);
        TextView userMobileView = headerView.findViewById(R.id.user_mobile_view);

        scoreView.setText("Score: "+ getTotalScore());

        userNameView.setText(sharedPref.getString(Env.sp.user_name, ""));
        userMobileView.setText(sharedPref.getString(Env.sp.user_mobile, ""));

        progressBar.setVisibility(View.VISIBLE);

        option_1.setOnClickListener(this);
        option_2.setOnClickListener(this);
        option_3.setOnClickListener(this);
        option_4.setOnClickListener(this);

        continue_btn.setOnClickListener(this);
        //slideView = findViewById(R.id.slideView);

        nextQuestion();
        //playTimeCount();


    }

    private void setup(){
        connectivity = new Connectivity(this);
        sharedPref = this.getSharedPreferences(Env.sp.sp_name, MODE_PRIVATE);
        editor = sharedPref.edit();

        game_score_math = sharedPref.getInt(Env.sp.game_score_math, 0);
        game_score_en = sharedPref.getInt(Env.sp.game_score_en, 0);

        game_error_math = sharedPref.getInt(Env.sp.game_error_math, 0);
        game_error_en = sharedPref.getInt(Env.sp.game_error_en, 0);

        subject = sharedPref.getInt(Env.sp.subject, 1);

        scoreView = findViewById(R.id.score_id);
        question = findViewById(R.id.question_id);
        option_1 = findViewById(R.id.option_1);
        option_2 = findViewById(R.id.option_2);
        option_3 = findViewById(R.id.option_3);
        option_4 = findViewById(R.id.option_4);
        continue_btn = findViewById(R.id.continue_id);
        progressBar = findViewById(R.id.progressBar);
        questionDB = new QuestionDB(this);
        questionDB.getWritableDatabase();
        solveTime = 10; //seconds
        time = 0;
    }

    private int getTotalScore(){
        return game_score_math + game_score_en;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_ranking) {

        } else if (id == R.id.nav_prize_list) {

            startActivity(new Intent(this, GiftsActivity.class));

        } else if (id == R.id.nav_wining_prize_list) {

        } else if (id == R.id.nav_switch) {
            startActivity(new Intent(this, SubjectSelectActivity.class));

        } else if (id == R.id.nav_term_con) {

        } else if (id == R.id.nav_sign_out) {
            if(connectivity.getConnectionStatus())
            {
                new ObjectRequestForQuestions(this).getResponse();
            }

            editor.putString(Env.sp.access_token, "no");
            editor.commit();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        clicked = true;

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
            Log.d(TAG, "Option skip button is clicked");
            checkResult("skip");

            /*slideView.setVisibility(View.VISIBLE);
            slideUp = new SlideUpBuilder(slideView)
                    .withStartState(SlideUp.State.HIDDEN)
                    .withStartGravity(Gravity.BOTTOM)

                    //.withSlideFromOtherView(anotherView)
                    //.withGesturesEnabled()
                    //.withHideSoftInputWhenDisplayed()
                    //.withInterpolator()
                    //.withAutoSlideDuration()
                    //.withLoggingEnabled()
                    //.withTouchableAreaPx()
                    //.withTouchableAreaDp()
                    //.withListeners()
                    //.withSavedState()
                    .build();*/
        }
    }

    public void nextQuestion()
    {

        cursor = questionDB.getQuestion(subject);

        if (cursor.moveToFirst()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    question.setText(cursor.getString(cursor.getColumnIndex(QuestionDB.COL_QUESTION)));
                    option_1.setText(cursor.getString(cursor.getColumnIndex(QuestionDB.COL_OPTION_A)));
                    option_2.setText(cursor.getString(cursor.getColumnIndex(QuestionDB.COL_OPTION_B)));
                    option_3.setText(cursor.getString(cursor.getColumnIndex(QuestionDB.COL_OPTION_C)));
                    option_4.setText(cursor.getString(cursor.getColumnIndex(QuestionDB.COL_OPTION_D)));
                }
            });


            new Thread(new MyThread(10)).start();

         }
        else {
            Log.d(TAG, "There is now row");
            startActivity(new Intent(this, ThankYouActivity.class));
            finish();

        }
    }

    public class MyThread implements Runnable{
        int time;

        MyThread(int time){
            this.time = time;
        }

        @Override
        public void run() {
            int ml = solveTime * 1000; // 10*1000 = 10000
            ml = ml/100; // 100
            time = 0;

            for (int i = 1; i<=100; i++)
            {
                if (clicked){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(100);

                        }
                    });
                    clicked = false;
                    break;
                }
                else {
                    time = i;
                    try {
                        sleep(ml);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(time);
                        }
                    });
                }

                Log.d(TAG, "run: i = "+i);
            }

            if (time == 100){
                questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.time_out);
                nextQuestion();
            }

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
                playSuccessMusic();
                addScore();
                questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.right_ans);
                scoreView.setText("Score: "+ getTotalScore());
                showDialog(getDialogTitle(), "Your answer is correct");
                nextQuestion();
            }

            else if(user_ans.equals("skip"))
            {
                questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.read_question);
                nextQuestion();
            }
            else{
                playErrorMusic();
                addError();
                questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.error_ans);
                showDialog(getDialogTitle(),"Your answer is wrong!");
                nextQuestion();
            }
        }
        else
        {
            Log.d(TAG, "There is no Question");
        }

    }

    private void playSuccessMusic()
    {
        MediaPlayer player = MediaPlayer.create(this, R.raw.hand_clap);
        player.setLooping(false);
        player.start();
    }

    private void playErrorMusic()
    {
        MediaPlayer player = MediaPlayer.create(this, R.raw.oohhh_no);
        player.setLooping(false);
        player.start();
    }

    private void playTimeCount()
    {
        MediaPlayer player = MediaPlayer.create(this, R.raw.time_count);
        player.setLooping(true);
        player.start();
    }


    private void addScore()
    {
        if(subject == Env.sp.subject_en_val)
        {
            game_score_en++;
            editor.putInt(Env.sp.game_score_en, game_score_en);
            editor.commit();
        }
        else
        {
            game_score_math++;
            editor.putInt(Env.sp.game_score_math, game_score_math);
            editor.commit();
        }
    }

    private void addError()
    {
        if(subject == Env.sp.subject_en_val)
        {
            game_error_en++;
            editor.putInt(Env.sp.game_error_en, game_error_en);
            editor.commit();
        }
        else
        {
            game_error_math++;
            editor.putInt(Env.sp.game_error_math, game_error_math);
            editor.commit();
        }
    }


    public String getDialogTitle()
    {

        return "";
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