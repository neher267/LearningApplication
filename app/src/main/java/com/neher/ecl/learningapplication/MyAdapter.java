package com.neher.ecl.learningapplication;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<Gifts> mGifts;
    private static final String TAG = MyAdapter.class.getSimpleName();


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView enMarks;
        public TextView mathMarks;
        public TextView giftName;
        public ImageView giftImage;

        public ViewHolder(View itemView) {
            super(itemView);
            enMarks = itemView.findViewById(R.id.en_mark);
            mathMarks = itemView.findViewById(R.id.math_mark);
            giftName = itemView.findViewById(R.id.gift_name);
            giftImage = itemView.findViewById(R.id.gift_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder() calling");

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);

        return vh;
    }

    public MyAdapter(ArrayList<Gifts> gifts){
        mGifts = gifts;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() calling");

        Gifts gift = mGifts.get(position);
        holder.giftName.setText("Name: Test");
        holder.enMarks.setText("English Marks: "+String.valueOf(gift.getEn_marks()));
        holder.mathMarks.setText("Math Marks: "+String.valueOf(gift.getMath_marks()));

        String url = "http://139.162.60.218/game/public/images/1.jpg";

        Picasso.get()
                .load(url)
                .error(R.drawable.gift)
                .into(holder.giftImage);


        //Picasso.get().load("http://cdn.blcalligraphy.com/wp-content/uploads/2014/11/pinterest_badge_red-300x300.png").into(logoView);
    }

    @Override
    public int getItemCount() {
        return mGifts.size();
    }


}
