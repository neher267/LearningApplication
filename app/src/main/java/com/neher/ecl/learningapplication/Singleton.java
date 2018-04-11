package com.neher.ecl.learningapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 4/10/2018.
 */

public class Singleton {

    private static Singleton singleton;
    private RequestQueue requestQueue;
    private static Context context;


    private Singleton(Context context)
    {
        this.context = context;
        requestQueue = getRequestQueue();
    }


    private RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static synchronized Singleton getInstance(Context context)
    {
        if (singleton == null)
        {
            singleton = new Singleton(context);
        }
        return singleton;
    }

    public<T> void addToRequestque(Request<T> request)
    {
        requestQueue.add(request);
    }
    
}
