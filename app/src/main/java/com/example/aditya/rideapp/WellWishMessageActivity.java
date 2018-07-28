package com.example.aditya.rideapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WellWishMessageActivity extends AppCompatActivity implements View.OnClickListener {

    Button home,rate,dispatch;
    ImageView oepic;
    TextView oeName;
    Intent intent;
    AlertDialog.Builder blog;
    ImageButton imgb;
    LinearLayout ll;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String rideId,offerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_wish_message);
        mAuth=FirebaseAuth.getInstance();
        init();

    }

    private void init() {
        home= (Button) findViewById(R.id.homeButtonww);
        rate = (Button) findViewById(R.id.rateusButtonww);
        rate = (Button) findViewById(R.id.dispatch);
        oeName = (TextView) findViewById(R.id.offerearnedName);
        ll= (LinearLayout) findViewById(R.id.olayout);
        oepic = (ImageView) findViewById(R.id.oep);
        imgb= (ImageButton) findViewById(R.id.bfod);
        imgb.setOnClickListener(this);
        home.setOnClickListener(this);
        rate.setOnClickListener(this);
        dispatch.setOnClickListener(this);
     }

    @Override
    protected void onStart() {
        super.onStart();
        rideId=getIntent().getStringExtra("RideId");
        offerId=getIntent().getStringExtra("OfferId");
        if (!rideId.isEmpty())
        {
         setValueByRide(rideId) ;
        }
        else if(!offerId.isEmpty())
        {
            setValueByOffer(offerId);
        }

        if (isOfferdisPatched(offerId))
        {
            dispatch.setText("Already Dispatched");
            dispatch.setClickable(false);
            dispatch.setAlpha(0.8f);
        }
    }

    private boolean isOfferdisPatched(String offerId) {
        return true;
    }

    private void setValueByOffer(String offerId) {
    }

    private void setValueByRide(String rideId) {
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Rides").child(rideId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.child("RiderId").getValue().toString().equals(mAuth.getCurrentUser().getUid()))
                {
                    offerId=dataSnapshot.child("ROID").getValue().toString();
                }
                else
                {
                    offerId=dataSnapshot.child("TOID").getValue().toString();
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bfod:
                moveTaskToBack(true);
                break;
            case R.id.dispatch:
                dispatchFun();
                break;
            case R.id.homeButtonww:
                intent=new Intent(WellWishMessageActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.rateusButtonww:
                intent=new Intent(WellWishMessageActivity.this,RatingActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void dispatchFun() {

        blog=new AlertDialog.Builder(getApplicationContext());
        blog.setTitle("Do You Want To Dipatch It?");
        blog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"You Will BE get Notification shortly",Toast.LENGTH_SHORT).show();

                dialogInterface.dismiss();
            }
        });

        blog.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"It Will Be Stored. You can Use It Later.",Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        });
        blog.show();

        ll.setVisibility(View.GONE);
    }
}