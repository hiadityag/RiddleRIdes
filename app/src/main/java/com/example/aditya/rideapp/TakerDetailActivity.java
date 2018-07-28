package com.example.aditya.rideapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TakerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView name,user,vehicle,dis,contact,vehiclen;
    Button done,seeonmap;
    Intent intent;
    ImageButton call,bf;
    private DatabaseReference mDatabase;
    AlertDialog.Builder dialog;
    private String glri,riderId,VT;
    double slatr,slongr,dlatr,dlongr,slatt,slongt,dlatt,dlongt;
    ProgressDialog progressDialog;
    int vt;
    Map  map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taker_detail);
        init();
        dialog=new AlertDialog.Builder(this);
        glri=getIntent().getStringExtra("GiveLiftRequestId");
        progressDialog=new ProgressDialog(this);
        map=new HashMap();
    }

    private void init() {
        name= (TextView) findViewById(R.id.namerider);
        user= (TextView) findViewById(R.id.usernamerider);
        vehicle= (TextView) findViewById(R.id.vehicleTyperrider);
        vehiclen= (TextView) findViewById(R.id.vehiclenorider);
        dis= (TextView) findViewById(R.id.distancerider);
        contact= (TextView) findViewById(R.id.contactnorider);
        seeonmap= (Button) findViewById(R.id.riderOnMap);
        done= (Button) findViewById(R.id.doneButtonrider);
        call= (ImageButton) findViewById(R.id.calling);
        bf= (ImageButton) findViewById(R.id.bftd);

        done.setOnClickListener(this);
        call.setOnClickListener(this);
        seeonmap.setOnClickListener(this);
        bf.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setValue();
    }

    private void setValue() {
        mDatabase= FirebaseDatabase.getInstance().getReference().child("GiveLiftRequests").child(glri);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                riderId=dataSnapshot.child("RiderId").getValue().toString();
                dis.setText(dataSnapshot.child("Dist").getValue().toString());
                slatt= Double.parseDouble(dataSnapshot.child("SourceLat").getValue().toString());
                slongt= Double.parseDouble(dataSnapshot.child("SourceLong").getValue().toString());
                dlatt= Double.parseDouble(dataSnapshot.child("DestLat").getValue().toString());
                dlongt= Double.parseDouble(dataSnapshot.child("DestLong").getValue().toString());

                //Rider Info
                DatabaseReference riderData=FirebaseDatabase.getInstance().getReference().child("Users").child(riderId);
                riderData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name.setText(dataSnapshot.child("Name").getValue().toString());
                        user.setText(dataSnapshot.child("Username").getValue().toString());
                        vehiclen.setText(dataSnapshot.child("VNumber").getValue().toString());
                        contact.setText(dataSnapshot.child("MobileNo").getValue().toString());

                        vt= Integer.parseInt(dataSnapshot.child("VType").getValue().toString());
                        switch (vt)
                        {
                            case 0:
                                VT="BIKE";
                                break;
                            case 1:
                                VT="SCOOTY";
                                break;
                            case 2:
                                VT="CAR";
                                break;
                            default:
                                break;
                        }
                        vehicle.setText(VT);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        databaseError.getMessage();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Retry",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.doneButtonrider:
                confirm();
                break;
            case R.id.calling:
                callfun();
                break;
            case R.id.riderOnMap:
                openmap();
                break;
            case R.id.bftd:
                moveTaskToBack(true);
                break;
            default:
                break;
        }
    }

    private void confirm() {

        dialog.setTitle("Confirm Ride?");
        dialog.show();
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                progressDialog.setTitle("Proccessing");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Please Wait a Moment");
                progressDialog.show();
                updateRide();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                intent=new Intent(TakerDetailActivity.this,ListOfRiderActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }

    private void updateRide() {

        mDatabase=FirebaseDatabase.getInstance().getReference().child("GiveLiftRequests").child(glri);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Integer.parseInt(dataSnapshot.child("Status").getValue().toString())==0)
                {
                    DatabaseReference mdata=FirebaseDatabase.getInstance().getReference().child("GiveLiftRequests").child(glri);
                    Map map=new HashMap();
                    map.put("Status",1);
                    mdata.updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            sendNotificationToRider();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Soory! He Is Booked Just Now.",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void sendNotificationToRider() {
        Toast.makeText(this,"Your Request Has Been Sent. You Will get notification Shortly.",Toast.LENGTH_LONG).show();
        intent=new Intent(TakerDetailActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openmap() {
        mDatabase=FirebaseDatabase.getInstance().getReference().child("TakeLiftRequests").child(riderId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                slatr= Double.parseDouble(dataSnapshot.child("SourceLat").getValue().toString());
                slongr= Double.parseDouble(dataSnapshot.child("SourceLong").getValue().toString());
                dlatr= Double.parseDouble(dataSnapshot.child("DestLat").getValue().toString());
                dlongr= Double.parseDouble(dataSnapshot.child("DestLong").getValue().toString());

                map.put("slatr",slatr);
                map.put("slongr",slongr);
                map.put("dlatr",dlatr);
                map.put("dlongr",dlongr);
                map.put("slatt",slatt);
                map.put("slongt",slongt);
                map.put("dlatt",dlatt);
                map.put("dlongt",dlongt);

                intent=new Intent(TakerDetailActivity.this,OpenMapActivity.class);
                startActivityForResult(intent,4);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void callfun() {
        intent=new Intent(Intent.ACTION_CALL);
        intent.addCategory(Intent.CATEGORY_APP_CONTACTS);
        intent.setType("mobile/*");
        intent.setData(Uri.parse("09411278889"));
        startActivityForResult(intent,5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==5)
        {
            if (resultCode==RESULT_OK)
            {
                Toast.makeText(getApplicationContext(),"Call Done Successfully.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
