package com.example.aditya.rideapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

public class TakeRidesActivity extends AppCompatActivity {

    Button nor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_rides);
        nor= (Button) findViewById(R.id.crt);
    }
}
