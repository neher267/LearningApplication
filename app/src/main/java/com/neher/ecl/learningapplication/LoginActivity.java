package com.neher.ecl.learningapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{


    private AutoCompleteTextView mMobileView;
    private EditText mPasswordView;
    private static final String TAG = LoginActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mMobileView = (AutoCompleteTextView) findViewById(R.id.mobile);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Log.d(TAG, String.valueOf(id));
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        String mobile = mMobileView.getText().toString();
        String password = mPasswordView.getText().toString();

        new UserLoginTask(mobile, password).login();
    }

    public class UserLoginTask {

        private final String mMobile;
        private final String mPassword;


        UserLoginTask(String mobile, String password) {
            mMobile = mobile;
            mPassword = password;
        }

        public boolean login()
        {
            StringRequest loginRequest = new StringRequest(Request.Method.POST, Env.remote.login_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject object = jsonObject.getJSONObject("data");
                                Context context = getApplicationContext();
                                SharedPreferences sharedPref = context.getSharedPreferences(Env.sp.sp_name, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();

                                editor.putString(Env.sp.user_name, object.getString("name"));
                                editor.putString(Env.sp.user_dob, object.getString("dob"));
                                editor.putString(Env.sp.user_mobile, object.getString("mobile"));
                                editor.putInt(Env.sp.game_score, object.getInt("marks"));
                                editor.putString(Env.sp.access_token, "yes");

                                editor.commit();

                                Log.d(TAG, "User Data was saved successfully");

                                new ObjectRequestForQuestions(LoginActivity.this).getResponse();

                                finish();

                                Log.d(TAG, "Authentication success!");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

                    params.put("username", mMobile);
                    params.put("password", mPassword);
                    return params;
                }
            };
            Singleton.getInstance(LoginActivity.this).addToRequestque(loginRequest);
            return true;
        }
    }
}