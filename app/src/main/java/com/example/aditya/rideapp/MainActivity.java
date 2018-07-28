package com.example.aditya.rideapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button give,take;
    CircleImageView appSetting,profile;
    Intent intent;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
    }

    private void init() {
        give= (Button) findViewById(R.id.giveButton);
        take= (Button) findViewById(R.id.takeButton);
        appSetting= (CircleImageView) findViewById(R.id.appnev);
        profile= (CircleImageView) findViewById(R.id.profile_image);

        give.setOnClickListener(this);
        take.setOnClickListener(this);
        appSetting.setOnClickListener(this);
        profile.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser =mAuth.getCurrentUser();
        if (currentUser==null)
        {
            intent=new Intent(MainActivity.this,SplashScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


        setValue();
    }

    private void setValue() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String picUri=dataSnapshot.child("Profile").getValue().toString();
                Picasso.with(MainActivity.this).load(Uri.parse(picUri)).placeholder(R.drawable.logo).into(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Picasso.with(MainActivity.this).load(R.drawable.logo).into(profile);
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.with(this).load(R.drawable.logo).into(appSetting);
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.giveButton:
                intent=new Intent(MainActivity.this,GiveLiftActivity.class);
                startActivity(intent);
                break;
            case R.id.takeButton:
                intent=new Intent(MainActivity.this,TakeLiftActivity.class);
                startActivity(intent);
                break;
            case R.id.appnev:
                intent=new Intent(MainActivity.this,NevigationActivity.class);
                startActivity(intent);
                break;
            case R.id.profile_image:
                intent=new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
