package com.example.aditya.rideapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView imageView,vehiclePic;
    Button logout,delete,offer,ALR;
    TextView name,uname,adharNo,contactNo,email,vehicleNo,vehicleType,pname;
    Intent intent;
    ImageButton back,edit;
    FloatingActionButton fabChangePic;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    String img,vTypeS,vp;
    int vType;
    private ProgressDialog pd;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        pd=new ProgressDialog(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setValues();
    }

    private void setValues() {
        pd.setMessage("Retreiving");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Name").getValue().toString());
                pname.setText(dataSnapshot.child("Name").getValue().toString());
                uname.setText(dataSnapshot.child("Username").getValue().toString());
                adharNo.setText(dataSnapshot.child("Aadhar").getValue().toString());
                contactNo.setText(dataSnapshot.child("MobileNo").getValue().toString());
                email.setText(dataSnapshot.child("Email").getValue().toString());
                vehicleNo.setText(dataSnapshot.child("VNumber").getValue().toString());
                vType= Integer.parseInt(dataSnapshot.child("VType").getValue().toString());
                switch (vType)
                {
                    case 1:
                        vTypeS="BIKE";
                        break;
                    case 2:
                        vTypeS="SCOOTY";
                        break;
                    case 3:
                        vTypeS="CAR";
                        break;
                    default:
                        vTypeS="None";
                        break;
                }
                vehicleType.setText(vTypeS);

                img=dataSnapshot.child("Profile").getValue().toString();
                if (img.equals("Default"))
                {
                    Picasso.with(ProfileActivity.this).load(R.drawable.logo).into(imageView);
                }
                else
                {
                    Picasso.with(ProfileActivity.this).load(Uri.parse(img)).placeholder(R.drawable.logo).into(imageView);
                }
                vp=dataSnapshot.child("VPic").getValue().toString();
                if (vp.equals("Default"))
                {
                    Picasso.with(ProfileActivity.this).load(R.drawable.logo).into(imageView);
                }
                else
                {
                    Picasso.with(ProfileActivity.this).load(Uri.parse(vp)).placeholder(R.drawable.dp).into(vehiclePic);

                }

                ALR.setText(dataSnapshot.child("AllRides").getValue().toString());
                pd.dismiss();

            }
            @Override
            public void onCancelled(DatabaseError databaseError)  {
                Toast.makeText(ProfileActivity.this,databaseError.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {

        imageView= (ImageView) findViewById(R.id.profileImage);
        name= (TextView) findViewById(R.id.name);
        pname= (TextView) findViewById(R.id.personName);
        uname= (TextView) findViewById(R.id.username);
        adharNo= (TextView) findViewById(R.id.aadharNo);
        contactNo= (TextView) findViewById(R.id.contactNO);
        email= (TextView) findViewById(R.id.email);
        vehicleNo= (TextView) findViewById(R.id.vehicleNumber);
        vehicleType= (TextView) findViewById(R.id.vehicleCategeory);
        vehiclePic= (ImageView) findViewById(R.id.vehiclepic);
        logout= (Button) findViewById(R.id.logoutButton);
        delete= (Button) findViewById(R.id.deleteButton);
        offer= (Button) findViewById(R.id.offer);
        ALR= (Button) findViewById(R.id.allRide);

        back= (ImageButton) findViewById(R.id.backButton);
        edit= (ImageButton) findViewById(R.id.editProfile);

        fabChangePic= (FloatingActionButton) findViewById(R.id.changeProfile);

        fabChangePic.setOnClickListener(this);
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        logout.setOnClickListener(this);
        delete.setOnClickListener(this);
        offer.setOnClickListener(this);
        ALR.setOnClickListener(this);
        imageView.setOnClickListener(this);
        vehiclePic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.deleteButton:
                deleteUser();
                break;

            case R.id.logoutButton:
                signOut();
                break;

            case R.id.changeProfile:
                changePic();
                break;

            case R.id.offer:
                intent =new Intent(ProfileActivity.this,OAEActivity.class);
                startActivity(intent);
                break;

            case R.id.allRide:
                intent = new Intent(ProfileActivity.this, AllRidesActivity.class);
                startActivity(intent);
                break;

            case R.id.backButton:
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.editProfile:
                intent = new Intent(ProfileActivity.this, EditProfile.class);
                startActivity(intent);
                break;
            case R.id.profileImage:
                showImg();
                break;
            default:
                break;
        }

    }

    private void showImg() {
        if(!img.equals("Default"))
        {
            Intent intent = new Intent(ProfileActivity.this,ShowImageActivity.class);
            intent.putExtra("ImgUri",img);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please First Set Your Profile Image.",Toast.LENGTH_LONG).show();
        }

    }

    private void changePic() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(ProfileActivity.this);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        intent = new Intent(ProfileActivity.this, IndexActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void deleteUser() {
        pd=new ProgressDialog(this);
        pd.setTitle("Deleting Account");
        pd.setMessage("Please Wait we Are Clear your Data...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),"Delete Successfully.",Toast.LENGTH_SHORT).show();
                    intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK)
            {
                pd=new ProgressDialog(this);
                pd.setCanceledOnTouchOutside(false);
                pd.setTitle("Uploading");
                pd.setMessage("Please wait a Moment");
                pd.show();

                final String picUri=result.getUri().toString();

                mStorage= FirebaseStorage.getInstance().getReference().child("profile_image").child(mAuth.getCurrentUser().getUid()+"profile_img.jpg");
                mStorage.putFile(Uri.parse(picUri)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            final String profileUrl=task.getResult().getDownloadUrl().toString();
                            mDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                            Map up=new HashMap();
                            up.put("Profile",profileUrl);
                            mDatabase.updateChildren(up).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful())
                                    {
                                        Picasso.with(ProfileActivity.this).load(Uri.parse(profileUrl)).placeholder(R.drawable.dp).into(imageView);
                                        pd.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Successfully Changed.", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(ProfileActivity.this, "Try Again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

            }
            else if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Toast.makeText(getApplicationContext(),"Sorry! ,Try Again.",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
