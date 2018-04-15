package com.neher.ecl.learningapplication;

import android.content.Context;
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

public class MyJsonObjectRequest {

    private Context context;
    private JSONObject jsonResponse;
    private static final String TAG = "MyJsonObjectRequest";

    public MyJsonObjectRequest(Context context)
    {
        this.context = context;
    }

    public void getResponse(String url)
    {
        Log.d(TAG, url);
        Log.d(TAG, "method calling");
        JsonObjectRequest jsonObjectRequests =  new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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

                                Question question = new Question(qus, sub, option_1, option_2, option_3,option_4, ans,weight, false);
                                questionDB.store(question);
                                Log.d(TAG, question.getQuestion());

                            }
                            SharedPreferences sharedPref = context.getSharedPreferences(Env.USER_INFO_SHARD_PRE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("worm_up_qsn_download", "yes");
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*JSONObject obj = new JSONObject();

                        Log.d(TAG, "error");

                        try {
                            obj.put("status", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonResponse = obj;*/
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        SharedPreferences preferences = context.getSharedPreferences(Env.USER_INFO_SHARD_PRE, Context.MODE_PRIVATE);
                        Map<String, String> map = new HashMap<>();
                        map.put("Content-Type","application/json");
                        map.put("Accept","application/json");
                        map.put("Authorization", preferences.getString("access_token",""));
                        return map;
                    }
                };

        Singleton.getInstance(context).addToRequestque(jsonObjectRequests);
    }

}
