package com.neher.ecl.learningapplication;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.slideup.SlideUp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<Gifts> mGifts;
    private static final String TAG = "MyAdapter"; //type logt



    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView enMarks;
        public TextView mathMarks;
        public TextView giftName;
        public ImageView giftImage;
        public ImageButton button;

        LinearLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);
            enMarks = itemView.findViewById(R.id.en_mark);
            mathMarks = itemView.findViewById(R.id.math_mark);
            giftName = itemView.findViewById(R.id.gift_name);
            giftImage = itemView.findViewById(R.id.gift_image);
            button = itemView.findViewById(R.id.card_option);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Log.d(TAG, "onCreateViewHolder() calling");

        Log.d(TAG, "onCreateViewHolder: calling"); //type logd

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);

        return vh;
    }

    public MyAdapter(ArrayList<Gifts> gifts){
        mGifts = gifts;
        Log.d(TAG, "MyAdapter() calling" + String.valueOf(mGifts.get(0).getName()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder() calling");

        Gifts gift = mGifts.get(position);
        holder.giftName.setText("Name: "+gift.getName());
        holder.enMarks.setText("English Marks: "+gift.getEn_marks());
        holder.mathMarks.setText("Math Marks: "+gift.getMath_marks());

        String url = "http://139.162.60.218/game/public/"+gift.getImage();

        Picasso.get()
                .load(url)
                .error(R.drawable.gift)
                .into(holder.giftImage);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Name = "+mGifts.get(position).getName());
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Name = "+mGifts.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {

        return mGifts.size();
    }
}
