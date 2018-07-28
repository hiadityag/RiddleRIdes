package com.example.aditya.rideapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ShowImageActivity extends AppCompatActivity {

    ImageButton imageButton;
    ImageView imageView;
    TextView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        imageView= (ImageView) findViewById(R.id.anyImage);
        img= (TextView) findViewById(R.id.imgName);
        imageButton= (ImageButton) findViewById(R.id.bB);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String imgUri=getIntent().getStringExtra("ImgUri");
        img.setText(imgUri);
        Picasso.with(ShowImageActivity.this).load(Uri.parse(imgUri)).placeholder(R.drawable.logo).into(imageView);
    }
}
