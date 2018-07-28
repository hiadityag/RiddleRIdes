package com.example.aditya.rideapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllRidesActivity extends AppCompatActivity {

    Button cr;
    Intent intent;
    RecyclerView recyclerView;
    private DatabaseReference mDatabse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rides);
        cr= (Button) findViewById(R.id.crb);
        recyclerView= (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabse= FirebaseDatabase.getInstance().getReference().child("Rides");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<AllRidesModel,userViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AllRidesModel, userViewHolder>(
                AllRidesModel.class,
                R.layout.single_rider,
                userViewHolder.class,
                mDatabse

        ) {
            @Override
            protected void populateViewHolder(userViewHolder viewHolder, AllRidesModel model, int position) {
                viewHolder.setRiderName(model.getRiderName());
                viewHolder.setTakerName(model.getTakerName());
                viewHolder.setDestText(model.getDestText());
                viewHolder.setSourceText(model.getSourceText());
                viewHolder.setDate(model.getDate());
                viewHolder.setStatus(model.getStatus());
                viewHolder.mView.findViewById(R.id.cancelride).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                viewHolder.mView.findViewById(R.id.ridefulldetail).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent=new Intent(AllRidesActivity.this,SingleRideDetailActivity.class);
                        intent.putExtra("RideId","576d4fdf8f4f5hhg64");
                        startActivity(intent);

                    }
                });


            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class userViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public userViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setRiderName(String riderName) {
         TextView ridername=mView.findViewById(R.id.rider);
            ridername.setText(riderName);
        }

        public void setTakerName(String takerName) {
            TextView takername=mView.findViewById(R.id.taker);
            takername.setText(takerName);
        }

        public void setStatus(String status) {
            TextView st=mView.findViewById(R.id.rider);
            st.setText(status);
        }

        public void setDestText(String destText) {
            TextView dst=mView.findViewById(R.id.dst);
            dst.setText(destText);
        }

        public void setSourceText(String sourceText) {
            TextView sor=mView.findViewById(R.id.src);
            sor.setText(sourceText);
        }

        public void setDate(String date) {
            TextView dateride=mView.findViewById(R.id.date);
            dateride.setText(date);
        }
    }
}
