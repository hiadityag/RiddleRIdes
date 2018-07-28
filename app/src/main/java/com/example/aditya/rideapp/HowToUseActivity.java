package com.example.aditya.rideapp;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HowToUseActivity extends  AppCompatActivity {

    RecyclerView r;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        r= (RecyclerView) findViewById(R.id.recycle);
        r.setHasFixedSize(true);
        r.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Help");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<HowToUseModel,viewHolderHTU> rvadapter=new FirebaseRecyclerAdapter<HowToUseModel, viewHolderHTU>(
                HowToUseModel.class,
                R.layout.single_how_to_use,
                viewHolderHTU.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(viewHolderHTU viewHolder, HowToUseModel model, int position) {
                viewHolder.setIndex(model.getIndex());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescImg(model.getDescImg());
                viewHolder.setDescText(model.getDescText());
            }
        };
        r.setAdapter(rvadapter);
    }

    public  class viewHolderHTU extends RecyclerView.ViewHolder
    {
        View mView;
        public viewHolderHTU(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setIndex(String index) {
            TextView i=mView.findViewById(R.id.noTextView);
            i.setText(index);
        }

        public void setTitle(String title) {
            TextView t=mView.findViewById(R.id.titleTextView);
            t.setText(title);
        }

        public void setDescImg(String descImg) {
            ImageButton di=mView.findViewById(R.id.imgView);
            Picasso.with(HowToUseActivity.this).load(Uri.parse(descImg)).placeholder(R.drawable.logo).into(di);
        }

        public void setDescText(String descText) {
            TextView dt=mView.findViewById(R.id.descriptionTextView);
            dt.setText(descText);

        }
    }
}
