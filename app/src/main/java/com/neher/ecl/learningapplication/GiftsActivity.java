package com.neher.ecl.learningapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class GiftsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Gifts> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mArrayList = new ArrayList<>();

        Gifts gifts1 = new Gifts(null,null,10,12,null);
        Gifts gifts2 = new Gifts(null,null,20,22,null);
        Gifts gifts3 = new Gifts(null,null,30,32,null);
        Gifts gifts4 = new Gifts(null,null,40,42,null);
        Gifts gifts5 = new Gifts(null,null,50,52,null);
        Gifts gifts6 = new Gifts(null,null,60,62,null);
        Gifts gifts7 = new Gifts(null,null,70,72,null);

        mArrayList.add(gifts1);
        mArrayList.add(gifts2);
        mArrayList.add(gifts3);
        mArrayList.add(gifts4);
        mArrayList.add(gifts5);
        mArrayList.add(gifts6);
        mArrayList.add(gifts7);

        mAdapter = new MyAdapter(mArrayList);

        mRecyclerView.setAdapter(mAdapter);


    }
}
