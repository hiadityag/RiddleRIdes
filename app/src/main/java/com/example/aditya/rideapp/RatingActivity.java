package com.example.aditya.rideapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RatingActivity extends AppCompatActivity implements View.OnClickListener {

    Button rate,submit;
    EditText comment;
    String cmnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        rate= (Button) findViewById(R.id.rateButton);
        submit= (Button) findViewById(R.id.submitButton);
        comment= (EditText) findViewById(R.id.commentEdit);

        rate.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.submitButton:
                updateFb();
                break;
            case R.id.rateButton:
                launchMarket();
                break;
            default:
                break;
        }
    }

    private void launchMarket() {
    }

    private void updateFb()
    {
        cmnt=comment.getText().toString();
    }
}
