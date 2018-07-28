package com.example.aditya.rideapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RiderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView name,user,souce,dest,dist,person,beg,land;
    CircleImageView pr;
    Button done,som,contact,cancel;
    ImageButton img;
    Intent intent;
    double slat,slong,dlat,dlong;
    HashMap<String,String> hashride;
    private DatabaseReference mTakerDatabase,mUserData;
    private FirebaseAuth  mAuth;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_detail);
        init();
        UID=getIntent().getStringExtra("TakerRequestId");
        setValue();
        mAuth=FirebaseAuth.getInstance();
    }

    private void setValue() {
        mTakerDatabase= FirebaseDatabase.getInstance().getReference().child("TakeLiftRequests").child(UID);
        mTakerDatabase.keepSynced(true);
        mTakerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                souce.setText(dataSnapshot.child("SoucreText").getValue().toString());
                dest.setText(dataSnapshot.child("DestText").getValue().toString());
                dist.setText(dataSnapshot.child("Dist").getValue().toString());
                land.setText(dataSnapshot.child("Landmark").getValue().toString());
                person.setText(dataSnapshot.child("Persons").getValue().toString());
                beg.setText(dataSnapshot.child("Begs").getValue().toString());
                DatabaseReference takerp=FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
                takerp.keepSynced(true);
                takerp.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name.setText(dataSnapshot.child("Name").getValue().toString());
                        user.setText(dataSnapshot.child("Username").getValue().toString());
                        contact.setText(dataSnapshot.child("MobileNo").getValue().toString());
                        Picasso.with(RiderDetailActivity.this).load(Uri.parse(dataSnapshot.child("Profile").getValue().toString())).placeholder(R.drawable.logo).into(pr);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Retry",Toast.LENGTH_SHORT).show();
            }
        });
        mUserData=FirebaseDatabase.getInstance().getReference().child("Users").child("54d4f6564648v5d45cvvf");
        mUserData.keepSynced(true);
        mUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Name").getValue().toString());
                user.setText(dataSnapshot.child("Username").getValue().toString());
                contact.setText(dataSnapshot.child("Mobile").getValue().toString());
                Picasso.with(RiderDetailActivity.this).load(Uri.parse(dataSnapshot.child("ProfileThumb").getValue().toString())).placeholder(R.drawable.logo).into(pr);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Retry",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        pr= (CircleImageView) findViewById(R.id.profile);
        name=(TextView) findViewById(R.id.nametaker);
        user=(TextView) findViewById(R.id.usernametaker);
        contact=(Button) findViewById(R.id.contacttaker);
        souce=(TextView) findViewById(R.id.sourcetaker);
        dest=(TextView) findViewById(R.id.destinationtaker);
        person=(TextView) findViewById(R.id.persontaker);
        dist=(TextView) findViewById(R.id.distancetaker);
        beg=(TextView) findViewById(R.id.begtaker);
        land=(TextView) findViewById(R.id.landtaker);
        som=(Button) findViewById(R.id.seeonmaptaker);
        done= (Button) findViewById(R.id.doneButtontaker);
        cancel= (Button) findViewById(R.id.cancelButtontaker);
        img= (ImageButton) findViewById(R.id.bfrd);

        img.setOnClickListener(this);
        som.setOnClickListener(this);
        done.setOnClickListener(this);
        cancel.setOnClickListener(this);
        contact.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.doneButtontaker:
                saveRide();
                break;
            case R.id.cancelButtontaker:
                cancelRide();
            case R.id.seeonmaptaker:
                openmapf();
                break;
            case R.id.bfrd:
                moveTaskToBack(true);
                break;
            case R.id.contacttaker:
                callFun();
                break;
            default:
                break;
        }
    }

    private void callFun() {
        intent=new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("09411278889"));
        intent.setType("mobile/*");
        startActivity(intent);
    }

    private void cancelRide() {
        mTakerDatabase=FirebaseDatabase.getInstance().getReference().child("GiveLiftRequests").child(UID);
        sendCancelNotification();
        Map map=new HashMap();
        map.put("Status","1");
        mTakerDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Cancel Successful",Toast.LENGTH_LONG).show();
                }
                else
                {
                    cancelRide();
                }
            }
        });

        intent=new Intent(RiderDetailActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendCancelNotification() {
    }

    private void saveRide() {
        mTakerDatabase=FirebaseDatabase.getInstance().getReference().child("GiveLiftRequests").child(UID);
        mTakerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Integer.parseInt(dataSnapshot.child("Status").getValue().toString())==0)
                {
                    DatabaseReference  mtd=FirebaseDatabase.getInstance().getReference().child("GiveLiftRequests").child(UID);
                    Map map=new HashMap<>();
                    map.put("Status","1");
                    mtd.updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            updateRide();
                        }
                    });
                }
                else

                {
                    Toast.makeText(getApplicationContext(),"Taker Found Someone else.",Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        intent =new Intent(RiderDetailActivity.this,SingleRideDetailActivity.class);
        intent.putExtra("RideId","Random");
        startActivity(intent);
        finish();
    }

    private void updateRide() {
        hashride=new HashMap<>();
        //Ride Info
        hashride.put("Source",souce.getText().toString());
        hashride.put("SourceLat", String.valueOf(slat));
        hashride.put("SourceLong", String.valueOf(slong));
        hashride.put("Dest",dest.getText().toString());
        hashride.put("DestLat", String.valueOf(dlat));
        hashride.put("DestLong", String.valueOf(dlong));
        hashride.put("Dist",dist.getText().toString());
        hashride.put("RiderId",mAuth.getCurrentUser().getUid());
        hashride.put("TakerId",UID);
        hashride.put("ROID","5df51v23d4d73d3vsd");
        hashride.put("TOID","77d7875vddf7cd5v42");
        hashride.put("Date","CurrentDate");


        mTakerDatabase=FirebaseDatabase.getInstance().getReference().child("Rides").child("RANDOM");
        mTakerDatabase.setValue(hashride).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                sendPositiveNotification();
            }
        });
    }

    private void sendPositiveNotification() {
    }

    private void openmapf() {
        HashMap<String,Double> hash=new HashMap<>();
        hash.put("SLat",slat);
        hash.put("SLong",slong);
        hash.put("DLat",dlat);
        hash.put("Dlong",dlong);
        intent=new Intent(RiderDetailActivity.this,OpenMapActivity.class);
        intent.putExtra("Hash",hash);
        startActivity(intent);
    }
}
