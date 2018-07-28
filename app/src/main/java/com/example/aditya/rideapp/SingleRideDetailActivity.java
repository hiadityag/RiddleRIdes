package com.example.aditya.rideapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleRideDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView src,dst,date,dist,tn,rn,tus,ru,tc,rc,vn;
    ImageView tp,rp;
    private DatabaseReference mDatabase;
    Button str,som;
    String rideId,TID,RID;
    double slat,slong,dlat,dlong;
    ImageButton im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_ride_detail);
        init();
        rideId=getIntent().getStringExtra("RideId");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Rides").child(rideId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setValue();
    }

    private void init() {
        src= (TextView) findViewById(R.id.source);
        dst= (TextView) findViewById(R.id.dest);
        som= (Button) findViewById(R.id.som);
        date= (TextView) findViewById(R.id.date);
        dist= (TextView) findViewById(R.id.dist);
        tn= (TextView) findViewById(R.id.takername);
        rn= (TextView) findViewById(R.id.ridername);
        tus= (TextView) findViewById(R.id.takeruser);
        ru= (TextView) findViewById(R.id.rideruser);
        tc= (TextView) findViewById(R.id.takercontact);
        rc= (TextView) findViewById(R.id.ridercontact);
        vn= (TextView) findViewById(R.id.ridervehicleNO);
        rp= (ImageView) findViewById(R.id.riderpic);
        tp= (ImageView) findViewById(R.id.takerpic);

        str= (Button) findViewById(R.id.ctstr);
        im= (ImageButton) findViewById(R.id.bfsrt);
        im.setOnClickListener(this);
        str.setOnClickListener(this);
        som.setOnClickListener(this);
    }

    private void setValue() {
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Rides").child(rideId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                src.setText(dataSnapshot.child("Source").getValue().toString());
                dst.setText(dataSnapshot.child("Dest").getValue().toString());
                date.setText(dataSnapshot.child("Date").getValue().toString());
                dist.setText(dataSnapshot.child("Dist").getValue().toString());

                TID=dataSnapshot.child("TakerId").getValue().toString();
                DatabaseReference takerd=FirebaseDatabase.getInstance().getReference().child("Users").child(TID);
                takerd.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tn.setText(dataSnapshot.child("Name").getValue().toString());
                        tus.setText(dataSnapshot.child("Username").getValue().toString());
                        tc.setText(dataSnapshot.child("MobileNo").getValue().toString());
                        Picasso.with(SingleRideDetailActivity.this).load(Uri.parse(dataSnapshot.child("Profile").getValue().toString())).placeholder(R.drawable.logo).into(tp);

                        DatabaseReference riderd=FirebaseDatabase.getInstance().getReference().child("Users").child(RID);
                        riderd.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                rn.setText(dataSnapshot.child("Name").getValue().toString());
                                ru.setText(dataSnapshot.child("Username").getValue().toString());
                                rc.setText(dataSnapshot.child("MobileNo").getValue().toString());
                                vn.setText(dataSnapshot.child("VNumber").getValue().toString());
                                Picasso.with(SingleRideDetailActivity.this).load(Uri.parse(dataSnapshot.child("Profile").getValue().toString())).placeholder(R.drawable.logo).into(rp);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.ctstr)
        {
            Intent intent=new Intent(SingleRideDetailActivity.this,MainActivity.class);
            startActivity(intent);
        }
        else if (view.getId()==R.id.som)
        {
            openMap();
        }
        else if (view.getId()==R.id.bfsrt)
        {
            moveTaskToBack(true);
        }
    }

    private void openMap() {
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Rides").child(rideId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                slat= Double.parseDouble(dataSnapshot.child("SourceLat").getValue().toString());
                slong= Double.parseDouble(dataSnapshot.child("SourceLong").getValue().toString());
                dlat= Double.parseDouble(dataSnapshot.child("DestLat").getValue().toString());
                dlong= Double.parseDouble(dataSnapshot.child("DestLong").getValue().toString());

                Intent intent=new Intent(SingleRideDetailActivity.this,OpenMapActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_SHORT).show();
            }
        });
    }
}