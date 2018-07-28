package com.example.aditya.rideapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GiveLiftActivity extends AppCompatActivity implements View.OnClickListener {

    Button sc,dc,search;
    EditText source,dest;
    Spinner person,beg;
    ArrayAdapter pAdapter,bAdapter;
    String sourceText,destText;
    int  per,begs;
    double slat,slong,dlat,dlong;
    Intent intent;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_lift);
        init();
        mAuth=FirebaseAuth.getInstance();

        person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                per=i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                per=1;
            }
        });
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
    }

    private void init()  {
        source= (EditText) findViewById(R.id.sourceEdit);
        dest= (EditText) findViewById(R.id.destEdit);
        person= (Spinner) findViewById(R.id.personSpinner);
        beg= (Spinner) findViewById(R.id.begSpinner);
        search= (Button) findViewById(R.id.searchButton);
        sc= (Button) findViewById(R.id.sourceCurrent);
        dc= (Button) findViewById(R.id.destCurrent);

        search.setOnClickListener(this);
        sc.setOnClickListener(this);
        dc.setOnClickListener(this);

        pAdapter=ArrayAdapter.createFromResource(this,R.array.np,R.layout.single_spiner_layout);
        bAdapter=ArrayAdapter.createFromResource(this,R.array.nb,R.layout.single_spiner_layout);

        person.setAdapter(pAdapter);
        beg.setAdapter(bAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.searchButton:
                requesttoride();
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

    private void requesttoride() {
        sourceText=source.getText().toString();
        destText=dest.getText().toString();
        if (TextUtils.isEmpty(sourceText) || TextUtils.isEmpty(destText))
        {
            Toast.makeText(this,"Source and Destination Both Are Required.",Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Proccessing");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please  Wait Your Request Is Sending...");
            progressDialog.show();
            storeRequest(sourceText,destText);
        }

    }

    private void storeRequest(String sourceText, String destText) {

        HashMap<String,String> requestLift=new HashMap<>();
        requestLift.put("RiderId",mAuth.getCurrentUser().getUid());
        requestLift.put("SourceText", sourceText);
        requestLift.put("SourceLat", String.valueOf(slat));
        requestLift.put("SourceLong", String.valueOf(slong));
        requestLift.put("DestText", destText);
        requestLift.put("DestLat", String.valueOf(dlat));
        requestLift.put("DestLong", String.valueOf(dlong));
        requestLift.put("Person", String.valueOf(begs));
        requestLift.put("Begs", String.valueOf(begs));
        requestLift.put("Dist", String.valueOf(2.5));
        requestLift.put("Status", String.valueOf(1));
        requestLift.put("Date","29/04/2017");
        mDatabse= FirebaseDatabase.getInstance().getReference().child("GiveLiftRequests").child("gv48re4v48r4vd545erwe58");
        mDatabse.setValue(requestLift).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"you will be notify Shortly.",Toast.LENGTH_LONG).show();
                    intent=new Intent(GiveLiftActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    progressDialog.hide();
                    Toast.makeText(GiveLiftActivity.this,"Try Again!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openmapfd() {
        intent=new Intent(GiveLiftActivity.this,OpenMapActivity.class);
        startActivityForResult(intent,0);
    }

    private void openmapfs() {
        intent=new Intent(GiveLiftActivity.this,OpenMapActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Fetching");
            progressDialog.setMessage("Please Wait While We Are  Retreiving Your Location...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            if (requestCode==0)
            {
                dlat=27.1546;
                dlong=28.1464;
                convertIntoTextForDest(dlat,dlong);
            }
            else if (requestCode==1)
            {
                slong=31.6455;
                slat=32.0212;
                convertIntoTextForSource(slat,slong);
            }
        }
    }

    private void convertIntoTextForSource(double lat, double log) {
        Geocoder ge=new Geocoder(this);
        try {
            List<Address> adresses= ge.getFromLocation(lat,log,1);
            Address add=adresses.get(0);
            source.setText(add.getLocality()+add.getAdminArea()+add.getCountryName()+add.getPostalCode());
            progressDialog.dismiss();
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
            progressDialog.dismiss();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
