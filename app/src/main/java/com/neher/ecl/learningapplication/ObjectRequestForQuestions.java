package com.neher.ecl.learningapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 4/12/2018.
 */

public class ObjectRequestForQuestions {

    private Context context;
    private static final String TAG = "ObjectRequestForQue";

    public ObjectRequestForQuestions(Context context)
    {
        this.context = context;
    }

    public void getResponse()
    {
        StringRequest jsonObjectRequestsForQuestions = new StringRequest(Request.Method.POST, Env.remote.questions_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");

                            QuestionDB questionDB = new QuestionDB(context);
                            questionDB.getWritableDatabase();

                            for (int i = 0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String qus = jsonObject.getString("question");
                                int sub = jsonObject.getInt("subject");
                                String option_1 = jsonObject.getString("a");
                                String option_2 = jsonObject.getString("b");
                                String option_3 = jsonObject.getString("c");
                                String option_4 = jsonObject.getString("d");
                                String ans = jsonObject.getString("ans");
                                int weight = jsonObject.getInt("weight");

                                Question question = new Question(qus, sub, option_1, option_2, option_3, option_4, ans, weight, Env.db.unread_question);
                                questionDB.store(question);
                            }

                            Log.d(TAG, "Questions are Downloaded and also saved in database.");

                            context.startActivity(new Intent(context, SubjectSelectActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Question download unsuccessfully!");
                        Log.d(TAG, String.valueOf(error));
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        SharedPreferences preferences = context.getSharedPreferences(Env.sp.sp_name, Context.MODE_PRIVATE);
                        Map<String, String> map = new HashMap<>();

                        Log.d(TAG, preferences.getString(Env.sp.user_mobile, "Mobile"));
                        Log.d(TAG, preferences.getString(Env.sp.user_password, "Password"));

                        map.put("username", preferences.getString(Env.sp.user_mobile, ""));
                        map.put("password", preferences.getString(Env.sp.user_password, "123456"));

                        map.put("math_score", String.valueOf(preferences.getInt(Env.sp.game_score_math, 0)));
                        map.put("en_error", String.valueOf(preferences.getInt(Env.sp.game_error_en, 0)));
                        map.put("en_score", String.valueOf(preferences.getInt(Env.sp.game_score_en, 0)));
                        map.put("math_error", String.valueOf(preferences.getInt(Env.sp.game_error_math, 0)));
                        return map;
                    }
                };

        Singleton.getInstance(context).addToRequestque(jsonObjectRequestsForQuestions);
    }


}
