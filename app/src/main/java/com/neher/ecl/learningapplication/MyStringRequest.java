package com.neher.ecl.learningapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Administrator on 4/12/2018.
 */

public class MyStringRequest {

    private Context context;
    private String responseString;
    public MyStringRequest(Context context)
    {
        this.context = context;
    }

    public String getResponse(String url)
    {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseString = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseString = "error";
                    }
                });

        Singleton.getInstance(context).addToRequestque(request);

        return responseString;
    }
}
