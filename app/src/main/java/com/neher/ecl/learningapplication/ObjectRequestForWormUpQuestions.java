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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 4/12/2018.
 */

public class ObjectRequestForWormUpQuestions {

    private Context context;
    private JSONObject jsonResponse;
    private static final String TAG = "ObjectRequestForWorm";

    public ObjectRequestForWormUpQuestions(Context context)
    {
        this.context = context;
    }

    public void getResponse(String url)
    {
        Log.d(TAG, url);
        Log.d(TAG, "method calling");
        JsonObjectRequest jsonObjectRequestsForWormUpQuestions =  new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, String.valueOf(response));
                        try {
                            QuestionDB questionDB = new QuestionDB(context);
                            questionDB.getWritableDatabase();

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

                                Question question = new Question(qus, sub, option_1, option_2, option_3,option_4, ans, weight, Env.WORMUP_UNREAD_QUESTION);
                                questionDB.store(question);
                                Log.d(TAG, question.getQuestion());

                            }

                            SharedPreferences sharedPref = context.getSharedPreferences(Env.USER_INFO_SHARD_PRE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("worm_up_qsn_download", "yes");
                            editor.commit();


                            context.startActivity(new Intent(context, WormUpQuestionActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "error!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, String.valueOf(error));
                        Log.d(TAG, "There is an error for downloading worm up questions");
                    }
                });

        Singleton.getInstance(context).addToRequestque(jsonObjectRequestsForWormUpQuestions);
    }

}
