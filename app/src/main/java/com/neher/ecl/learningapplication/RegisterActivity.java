package com.neher.ecl.learningapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */

    private static final String TAG = RegisterActivity.class.getSimpleName();


    private EditText mNameView;
    private EditText mMobileView;
    private RadioGroup mViewGender;
    private EditText dob;
    private EditText mPasswordView;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mMobileView = findViewById(R.id.mobile);
        mNameView = findViewById(R.id.name);
        mViewGender = findViewById(R.id.gender);

        dob = findViewById(R.id.dob);
        dob.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                int month = i1+1;
                String date = i+"-"+month+"-"+i2;
                dob.setText(date);
            }
        };

        mPasswordView = (EditText) findViewById(R.id.password);

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });


        notificationManager = NotificationManagerCompat.from(this);
    }



    private void attemptRegistration() {

        String name = mNameView.getText().toString();
        String mobile = mMobileView.getText().toString();
        String date_of_birth = dob.getText().toString();
        String password = mPasswordView.getText().toString();
        Log.d(TAG, password);
        
        int id = mViewGender.getCheckedRadioButtonId();
        
        String gender = "";
        if (id == R.id.gender_m)
        {
            gender = "0";
        }
        else {
            gender = "1";
        }

        new UserRegistrationTask(name, mobile, gender, date_of_birth, password).register();
    }

    public class UserRegistrationTask {

        private final String name;
        private final String mobile;
        private final String gender;
        private final String dob;
        private final String password;

        UserRegistrationTask(String name, String mobile, String gender, String dob, String password) {
            this.name = name;
            this.mobile = mobile;
            this.gender = gender;
            this.dob = dob;
            this.password = password;
        }

        public boolean register()
        {
            StringRequest RegisterRequest = new StringRequest(Request.Method.POST, Env.remote.register_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);

                            SharedPreferences sharedPref = RegisterActivity.this.getSharedPreferences(Env.sp.sp_name, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putString(Env.sp.user_name, name);
                            editor.putString(Env.sp.user_mobile, mobile);
                            editor.putString(Env.sp.user_gender, gender);
                            editor.putString(Env.sp.user_dob, dob);
                            editor.putString(Env.sp.user_password, password);
                            editor.putString(Env.sp.access_token, "yes");
                            editor.commit();

                            new ObjectRequestForQuestions(RegisterActivity.this).getResponse();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.d(TAG, String.valueOf(error));
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    SharedPreferences sharedPref = RegisterActivity.this.getSharedPreferences(Env.sp.sp_name, Context.MODE_PRIVATE);

                    params.put("username", mobile);
                    params.put("name", name);
                    params.put("gender", gender);
                    params.put("dob", dob);
                    params.put("password", password);
                    params.put("wormup_score", String.valueOf(sharedPref.getInt(Env.sp.worm_up_score, 0)));
                    params.put("wormup_score_weight", String.valueOf(sharedPref.getInt(Env.sp.worm_up_score_weight, 0)));
                    params.put("wormup_error", String.valueOf(sharedPref.getInt(Env.sp.worm_up_error, 0)));
                    params.put("wormup_error_weight", String.valueOf(sharedPref.getInt(Env.sp.worm_up_error_weight, 0)));

                    return params;
                }
            };

            Singleton.getInstance(RegisterActivity.this).addToRequestque(RegisterRequest);

            return true;
        }
    }

}

