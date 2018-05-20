package com.neher.ecl.learningapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.TextView;

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

    private int game_score;
    private int game_error;

    private int subject;

    private Connectivity connectivity;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String TAG = GameActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        connectivity = new Connectivity(this);
        sharedPref = this.getSharedPreferences(Env.sp.sp_name, MODE_PRIVATE);


        subject = sharedPref.getInt(Env.sp.subject, 1);

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

        userNameView.setText(sharedPref.getString(Env.sp.user_name, ""));
        userMobileView.setText(sharedPref.getString(Env.sp.user_mobile, ""));



        editor = sharedPref.edit();

        game_score = sharedPref.getInt(Env.sp.game_score, 0);
        game_error = sharedPref.getInt(Env.sp.game_error, 0);

        question = findViewById(R.id.question_id);
        scoreView = findViewById(R.id.score_id);
        option_1 = findViewById(R.id.option_1);
        option_2 = findViewById(R.id.option_2);
        option_3 = findViewById(R.id.option_3);
        option_4 = findViewById(R.id.option_4);
        continue_btn = findViewById(R.id.continue_id);

        scoreView.setText("Score: "+String.valueOf(game_score));

        option_1.setOnClickListener(this);
        option_2.setOnClickListener(this);
        option_3.setOnClickListener(this);
        option_4.setOnClickListener(this);
        continue_btn.setOnClickListener(this);

        questionDB = new QuestionDB(this);
        questionDB.getWritableDatabase();
        nextQuestion();
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
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            Log.d(TAG, "Option skip button is clicked");
            checkResult("skip");
        }
    }


    public void nextQuestion()
    {
        cursor = questionDB.getQuestion(subject);

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
                game_score++;
                editor.putInt(Env.sp.game_score, game_score);
                editor.commit();
                scoreView.setText("Score: "+game_score);
                showDialog(getDialogTitle(), "Your answer is correct");
                nextQuestion();
            }

            else if(user_ans.equals("skip"))
            {
                questionDB.update(cursor.getInt(cursor.getColumnIndex(QuestionDB.COL_ID)), Env.db.read_question);
                nextQuestion();
            }
            else{
                game_error++;
                editor.putInt(Env.sp.game_error, game_error);
                editor.commit();
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
