package com.neher.ecl.learningapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserInfo {
    private Context context;
    private static final String TAG = UpdateUserInfo.class.getSimpleName();

    public UpdateUserInfo(Context context)
    {
        this.context = context;
    }

    public void getResponse()
    {
        StringRequest jsonObjectRequestsForQuestions = new StringRequest(Request.Method.POST, Env.remote.update_user_info,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("200"))
                        {
                            Log.d(TAG, "User info Updated Successfully");
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
                SharedPreferences preferences = context.getSharedPreferences(Env.sp.sp_name, Context.MODE_PRIVATE);
                Map<String, String> map = new HashMap<>();
                //map.put("game_score", String.valueOf(preferences.getInt(Env.sp.game_score, 0)));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences preferences = context.getSharedPreferences(Env.sp.sp_name, Context.MODE_PRIVATE);
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", preferences.getString(Env.sp.access_token,""));
                return map;
            }
        };

        Singleton.getInstance(context).addToRequestque(jsonObjectRequestsForQuestions);
    }
}
