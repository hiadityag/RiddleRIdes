package com.example.aditya.rideapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NevigationActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton cancel;
    Button home,allrides,offer,refer,rating,about,help,contact,exit;
    Intent intent;
    TextView allR;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevigation);
        init();
        mAuth=FirebaseAuth.getInstance();
        mDatabse= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
    }

    private void init() {
        cancel= (ImageButton) findViewById(R.id.cancel);
        home= (Button) findViewById(R.id.homeButton);
        allrides= (Button) findViewById(R.id.allRide);
        offer= (Button) findViewById(R.id.offerearned);
        refer= (Button) findViewById(R.id.refer);
        about= (Button) findViewById(R.id.about);
        help= (Button) findViewById(R.id.help);
        contact= (Button) findViewById(R.id.contact);
        rating= (Button) findViewById(R.id.rateus);
        exit= (Button) findViewById(R.id.exit);

        allR= (TextView) findViewById(R.id.ar);

        cancel.setOnClickListener(this);
        home.setOnClickListener(this);
        allrides.setOnClickListener(this);
        offer.setOnClickListener(this);
        refer.setOnClickListener(this);
        about.setOnClickListener(this);
        help.setOnClickListener(this);
        contact.setOnClickListener(this);
        rating.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               allR.setText(dataSnapshot.child("AllRide").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NevigationActivity.this,"ryAgain",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cancel:
                intent=new Intent(NevigationActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.homeButton:
                intent=new Intent(NevigationActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.allRide:
                intent=new Intent(NevigationActivity.this,AllRidesActivity.class);
                startActivity(intent);
                break;
            case R.id.offerearned:
                intent=new Intent(NevigationActivity.this,OfferEarnedActivity.class);
                startActivity(intent);
                break;
            case R.id.refer:
                intent=new Intent(NevigationActivity.this,ReferActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent=new Intent(NevigationActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.help:
                intent=new Intent(NevigationActivity.this,HowToUseActivity.class);
                startActivity(intent);
                break;
            case R.id.contact:
                intent=new Intent(NevigationActivity.this,EmergncyContactActivity.class);
                startActivity(intent);
                break;
            case R.id.rateus:
                intent=new Intent(NevigationActivity.this,RatingActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                onDestroy();
                break;
            default:
                break;
        }

    }
}
