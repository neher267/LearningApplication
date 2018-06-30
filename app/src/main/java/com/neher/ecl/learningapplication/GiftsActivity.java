package com.neher.ecl.learningapplication;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class GiftsActivity extends AppCompatActivity {

    private static final String TAG = GiftsActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ProgressDialog loading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("Loading");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        loading.show();

        JsonObjectRequest jsonObjectRequestsForGifts10 =  new JsonObjectRequest(Request.Method.POST, Env.remote.gifts, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "onResponse is calling");

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            ArrayList<Gifts> mGifts = new ArrayList<>();

                            for (int i = 0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Gifts gift = new Gifts();
                                gift.setName(jsonObject.getString("name"));
                                gift.setDescription(jsonObject.getString("description"));
                                gift.setEn_marks(jsonObject.getString("en_marks"));
                                gift.setMath_marks(jsonObject.getString("math_marks"));
                                gift.setImage(jsonObject.getString("image"));

                                mGifts.add(gift);
                                Log.d(TAG, "gift: "+gift.getName());

                            }
                            setUpRecyclerView(mGifts);

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
                        Log.d(TAG, "Error:"+String.valueOf(error));
                    }
                });

        Singleton.getInstance(this).addToRequestque(jsonObjectRequestsForGifts10);
        Log.d(TAG, "onResponse is not calling");
    }

    public void setUpRecyclerView(ArrayList<Gifts> list)
    {
        Log.d(TAG, "setUpRecyclerView() calling");
        loading.dismiss();

        mAdapter = new MyAdapter(list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
