package com.example.shubhampratap.rideapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText name,user,contact,email,vno;
    Button save;
    ImageButton vp;
    Spinner vts;
    ArrayAdapter mAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    ProgressDialog progress;
    String rUriO,rUriN,resultUri;
    int vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        progress=new ProgressDialog(this);
        vts.setOnItemSelectedListener(this);
    }

    private void init() {

        name= (EditText) findViewById(R.id.nameprofileedit);
        user= (EditText) findViewById(R.id.usernameprofileedit);
        contact= (EditText) findViewById(R.id.contactnoprofileedit);
        email= (EditText) findViewById(R.id.emailprofileedit);
        vno= (EditText) findViewById(R.id.vehiclenoedit);
        vts= (Spinner) findViewById(R.id.vehicletypespinneredit);
        mAdapter=ArrayAdapter.createFromResource(this,R.array.vt,R.layout.single_spiner_layout);
        vts.setAdapter(mAdapter);
        save= (Button) findViewById(R.id.saveBUtton);
        vp= (ImageButton) findViewById(R.id.vehiclepicedit);
        
        save.setOnClickListener(this);
        vp.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setValue();
    }

    private void setValue() {
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Name").getValue().toString());
                user.setText(dataSnapshot.child("Username").getValue().toString());
                email.setText(dataSnapshot.child("Email").getValue().toString());
                contact.setText(dataSnapshot.child("MobileNo").getValue().toString());
                vno.setText(dataSnapshot.child("VNumber").getValue().toString());
                rUriO=dataSnapshot.child("VPic").getValue().toString();
                if (!rUriO.equals("Default"))
                {
                    Picasso.with(EditProfile.this).load(Uri.parse(rUriO)).into(vp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.saveBUtton:
                progress.setTitle("Updating");
                progress.setMessage("Please Wait While  We Update Your Data...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                updateData();
                break;
            case R.id.vehiclepic:
                choosePic();
                break;
            default:
                break;
        }
    }

    private void updateData() {

        Map map=new HashMap();
        map.put("Name",name.getText().toString());
        map.put("Username",user.getText().toString());
        map.put("Email",email.getText().toString());
        map.put("MobileNo",contact.getText().toString());
        map.put("VNumber",vno.getText().toString());
        map.put("VType",vt);
        map.put("VPic",rUriN);

        mDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Update Successfully Done.",Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(EditProfile.this,ProfileActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                }
                else
                {
                    progress.hide();
                    Toast.makeText(getApplicationContext(),"Data Update Failed!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void choosePic() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setAutoZoomEnabled(true)
                .setAllowRotation(true)
                .setAllowFlipping(true)
                .start(EditProfile.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult reusult=CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK)
            {
                progress.setTitle("Uploading");
                progress.setMessage("Please Wait While  We Update.");
                progress.setCanceledOnTouchOutside(false);
                progress.show();

                resultUri=reusult.getUri().toString();
                mStorage= FirebaseStorage.getInstance().getReference().child("vehicle_image");
                StorageReference filepath=mStorage.child(mAuth.getCurrentUser().getUid()+"vehicle_img.jpg");
                filepath.putFile(Uri.parse(resultUri)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            rUriN=task.getResult().getDownloadUrl().toString();
                            Picasso.with(EditProfile.this).load(Uri.parse(rUriN)).placeholder(R.drawable.logo).into(vp);
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(),"Image Upload Successful",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            rUriN=rUriO;
                            progress.hide();
                            Toast.makeText(getApplicationContext(),"Image Upload Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        vt=i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        vt=0;
    }
}
