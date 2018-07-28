package com.example.aditya.rideapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class TakeLiftActivity extends AppCompatActivity implements View.OnClickListener {

    EditText source,dest,land;
    Spinner vehicleType,person,beg;
    Button sc,dc,book;
    ArrayAdapter vAdapter,bAdapter,pAdapter;
    ProgressDialog progressDialog;
    Intent  intent;
    String sourceText,destText,landText,vType,UID;
    ArrayList<String> riderIds;
    double sourceLat,sourceLan,destLan,destlat;
    int per,begs,distBtstos;
    private FirebaseAuth mAuth;
    private DatabaseReference  mDatabse,mDatabase2;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_lift);
        init();
        mAuth=FirebaseAuth.getInstance();

        beg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                begs=i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                begs=0;
            }
        });
        person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                per=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                per=1;
            }
        });

    }

    private void init() {

        source= (EditText) findViewById(R.id.sourceEdittake);
        dest= (EditText) findViewById(R.id.destEdittake);
        land= (EditText) findViewById(R.id.landEdit);
        vehicleType= (Spinner) findViewById(R.id.vehicleSpinner);
        person= (Spinner) findViewById(R.id.personSpinnertake);
        beg= (Spinner) findViewById(R.id.begSpinnertake);
        book= (Button) findViewById(R.id.bookButton);
        sc= (Button) findViewById(R.id.sourceCurrenttake);
        dc= (Button) findViewById(R.id.destCurrenttake);

        book.setOnClickListener(this);
        sc.setOnClickListener(this);
        dc.setOnClickListener(this);

        vAdapter=ArrayAdapter.createFromResource(this,R.array.vt,R.layout.single_spiner_layout);
        bAdapter=ArrayAdapter.createFromResource(this,R.array.nb,R.layout.single_spiner_layout);
        pAdapter=ArrayAdapter.createFromResource(this,R.array.np,R.layout.single_spiner_layout);
        vehicleType.setAdapter(vAdapter);
        person.setAdapter(pAdapter);
        beg.setAdapter(bAdapter);
    }

    @Override
    public void onClick(View view) {
       switch(view.getId())
       {
           case R.id.doneButton:
               done();
               break;
           case R.id.sourceCurrent:
               openmapfs();
               break;
           case R.id.destCurrent:
               openmapfd();
               break;
           default:
               break;
       }
    }

    private void done() {
        sourceText=source.getText().toString();
        destText=dest.getText().toString();
        landText=land.getText().toString();
        if (TextUtils.isEmpty(sourceText) || TextUtils.isEmpty(destText))
        {
            Toast.makeText(this,"Souce and dest should Not be Empty.",Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog=new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please Wait We Are Checking  Who are Going To Same Side...");
            progressDialog.setTitle("Checking");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            updateUI(sourceText,destText);
        }

    }

    private void updateUI(String sourceText, String destText) {

        HashMap<String,String> requestLift=new HashMap<>();
        requestLift.put("SourceText", sourceText);
        requestLift.put("SourceLat", String.valueOf(sourceLat));
        requestLift.put("SourceLan", String.valueOf(sourceLan));
        requestLift.put("DestText", destText);
        requestLift.put("DestLat", String.valueOf(destlat));
        requestLift.put("DestLan", String.valueOf(destLan));
        requestLift.put("Landmark", landText);
        requestLift.put("Persons", String.valueOf(begs));
        requestLift.put("Begs", String.valueOf(begs));
        requestLift.put("Date","25:10:35");
        requestLift.put("Status","1");
        requestLift.put("Dist","3.5 KM.");
        UID=mAuth.getCurrentUser().getUid();
        mDatabse= FirebaseDatabase.getInstance().getReference().child("TakeLiftRequests").child(UID);
        mDatabse.setValue(requestLift).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                                intent=new Intent(TakeLiftActivity.this,ListOfRiderActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              intent.putExtra("RequiredId", UID);
                                startActivity(intent);
                                finish();

                        }
                        else
                        {
                            progressDialog.hide();
                            Toast.makeText(TakeLiftActivity.this,"Try Again!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void searchRequest() {
        riderIds= new ArrayList<>();
        mDatabase2=FirebaseDatabase.getInstance().getReference().child("GiveLiftRequests");
        mDatabse.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Iterable i=dataSnapshot.getChildren();
                Iterator i1=i.iterator();
                while (i1.hasNext())
                {
                    distBtstos=dbsts(sourceLat,sourceLan);
                    if (distBtstos<=5.0)
                    {
                        riderIds.add("chk686655dg5v4tr55");
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int dbsts(double sourceLat, double sourceLan) {
        return 1;
    }

    private void openmapfd() {
        intent=new Intent(TakeLiftActivity.this,OpenMapActivity.class);
        startActivityForResult(intent,2);
    }

    private void openmapfs() {
        intent=new Intent(TakeLiftActivity.this,OpenMapActivity.class);
        startActivityForResult(intent,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            if (requestCode==2)
            {
                destlat=27.2365;
                destLan=27.1425;
                convertIntoTextForDest(destlat,destLan);
            }
            else if (requestCode==3)
            {
                sourceLan=27.2654;
                sourceLat=27.4452;
                convertIntoTextForSource(sourceLat,sourceLan);
            }
        }
    }

    private void convertIntoTextForSource(double lat, double log) {
        Geocoder ge=new Geocoder(this);
        try {
            List<Address> adresses= ge.getFromLocation(lat,log,1);
            Address add=adresses.get(0);
            source.setText(add.getLocality()+add.getAdminArea()+add.getCountryName()+add.getPostalCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertIntoTextForDest(double lat, double log) {
        Geocoder ge=new Geocoder(this);
        try {
            List<Address> adresses= ge.getFromLocation(lat,log,1);
            Address add=adresses.get(0);
            dest.setText(add.getLocality()+add.getAdminArea()+add.getCountryName()+add.getPostalCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
