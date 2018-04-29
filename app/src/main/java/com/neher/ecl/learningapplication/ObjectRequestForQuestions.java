package com.neher.ecl.learningapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public void getResponse(String url, final int last_id)
    {
        Log.d(TAG, url);
        Log.d(TAG, "method calling");

        StringRequest jsonObjectRequestsForQuestions = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            SharedPreferences sharedPref = context.getSharedPreferences(Env.USER_INFO_SHARD_PRE, Context.MODE_PRIVATE);

                            QuestionDB questionDB = new QuestionDB(context);
                            questionDB.getWritableDatabase();

                            int last_id = sharedPref.getInt(Env.LAST_DOWNLOAD_QSN_ID, 0);

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

                                last_id = jsonObject.getInt("id");

                                Question question = new Question(qus, sub, option_1, option_2, option_3, option_4, ans, weight, Env.UNREAD_QUESTION);

                                questionDB.store(question);
                                Log.d(TAG, question.getQuestion());

                            }

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("questions_version", 1);
                            editor.putInt(Env.LAST_DOWNLOAD_QSN_ID, last_id);
                            editor.commit();

                            Log.d(TAG, "Last Id: "+String.valueOf(last_id));

                            context.startActivity(new Intent(context, QuestionActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Question download unsuccessfully!");
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Log.d(TAG, "getParams is calling");
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(last_id));
                        return map;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        SharedPreferences preferences = context.getSharedPreferences(Env.USER_INFO_SHARD_PRE, Context.MODE_PRIVATE);

                        Log.d(TAG, "getHeaders is calling");
                        Log.d(TAG, "access_token: "+preferences.getString("access_token",""));

                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", preferences.getString("access_token",""));
                        return map;
                    }
                };

        Singleton.getInstance(context).addToRequestque(jsonObjectRequestsForQuestions);
    }

}
