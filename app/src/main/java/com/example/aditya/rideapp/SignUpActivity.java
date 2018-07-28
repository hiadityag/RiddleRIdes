package com.example.aditya.rideapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText fn,un,email,acn,vn,p,cp,cn;
    Button s2,s3,done,Next,back,Back1,Back2;
    Spinner VTspinner;
    ImageButton vi,bti;
    int hasVehicle;
    LinearLayout pdb,vdb,spb,cdb;
    ProgressDialog pd,pdi;
    String name,user,em,mobile,adhar,vNo,viUri,pass,cpass,picUri,picUriThumb;
    int vTypr;
    Uri resultUri;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        mAuth = FirebaseAuth.getInstance();
        pd=new ProgressDialog(this);
        viUri="Default";
        hasVehicle=0;
        picUri="Default";
        picUriThumb="Default";
        mStorage=FirebaseStorage.getInstance().getReference().child("vehicle_image");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private void init() {

        fn= (EditText) findViewById(R.id.fnEdit);
        un= (EditText) findViewById(R.id.userEdit);
        email= (EditText) findViewById(R.id.emailEditsignup);
        cn= (EditText) findViewById(R.id.mobileEdit);
        acn= (EditText) findViewById(R.id.aadharEdit);
        vn= (EditText) findViewById(R.id.noEdit);
        p= (EditText) findViewById(R.id.passwordEditsignup);
        cp= (EditText) findViewById(R.id.repasswordEdit);

        Next= (Button) findViewById(R.id.next);
        s2= (Button) findViewById(R.id.step2);
        back= (Button) findViewById(R.id.bak);
        s3= (Button) findViewById(R.id.step3Button);
        Back1= (Button) findViewById(R.id.back1);
        Back2= (Button) findViewById(R.id.back2);
        done= (Button) findViewById(R.id.doneButton);

        VTspinner= (Spinner) findViewById(R.id.vtSpinner);

        vi= (ImageButton) findViewById(R.id.vpic);
        bti= (ImageButton) findViewById(R.id.backtoindex);

        pdb= (LinearLayout) findViewById(R.id.personalBlock);
        cdb= (LinearLayout) findViewById(R.id.contactDetail);
        vdb= (LinearLayout) findViewById(R.id.vehicleDetail);
        spb= (LinearLayout) findViewById(R.id.passwordBlock);

        s2.setOnClickListener(this);
        s3.setOnClickListener(this);
        done.setOnClickListener(this);
        Next.setOnClickListener(this);
        back.setOnClickListener(this);
        Back1.setOnClickListener(this);
        Back2.setOnClickListener(this);
        vi.setOnClickListener(this);
        bti.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backtoindex:
                Intent intent=new Intent(SignUpActivity.this,IndexActivity.class);
                startActivity(intent);
                break;
            case R.id.step2:
                clickstep2();
                break;
            case R.id.step3Button:
                clickstep3();
                break;
            case R.id.doneButton:
                donef();
                break;
            case R.id.back1:
                pressback1();
                break;
            case R.id.back2:
                pressback2();
                break;
            case R.id.bak:
                pressback();
                break;
            case R.id.next:
                clicknext();
                break;
            case R.id.vpic:
                choosePic();
            default:
                break;
        }
    }

    private void clickstep3() {
       // vS=0;
        vNo=vn.getText().toString();
        VTspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vTypr=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                vTypr=0;
            }
        });

        if (vTypr!=0  && !TextUtils.isEmpty(vNo))
        {
            hasVehicle=1;

            //vS=1;
        }
        else {
            Toast.makeText(getApplicationContext(),"You Don't Have Any Vehicle.",Toast.LENGTH_LONG).show();
        }
        vdb.setVisibility(View.GONE);
        pdb.setVisibility(View.GONE);
        spb.setVisibility(View.VISIBLE);
        cdb.setVisibility(View.GONE);

    }

    private void clickstep2() {
        em=email.getText().toString();
        mobile=cn.getText().toString();
        if(TextUtils.isEmpty(em) || TextUtils.isEmpty(mobile) ||mobile.length()<10 || em.length()<12)
        {
            Toast.makeText(this,"Please Check!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            vdb.setVisibility(View.VISIBLE);
            pdb.setVisibility(View.GONE);
            spb.setVisibility(View.GONE);
            cdb.setVisibility(View.GONE);
        }
    }

    private void pressback1() {
        vn.setText("");
        hasVehicle=0;
        vdb.setVisibility(View.GONE);
        pdb.setVisibility(View.GONE);
        spb.setVisibility(View.GONE);
        cdb.setVisibility(View.VISIBLE);
    }

    private void pressback2() {
        p.setText("");
        cp.setText("");
        vdb.setVisibility(View.VISIBLE);
        pdb.setVisibility(View.GONE);
        spb.setVisibility(View.GONE);
        cdb.setVisibility(View.GONE);
    }

    private void clicknext() {
        name=fn.getText().toString();
        user=un.getText().toString();
        adhar=acn.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(user) || TextUtils.isEmpty(adhar) ||adhar.length()<12)
        {
            Toast.makeText(this,"Please Fill Your Form Again.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            vdb.setVisibility(View.GONE);
            pdb.setVisibility(View.GONE);
            spb.setVisibility(View.GONE);
            cdb.setVisibility(View.VISIBLE);
        }
    }

    private void pressback() {
        email.setText(" ");
        cn.setText(" ");
        vdb.setVisibility(View.GONE);
        pdb.setVisibility(View.VISIBLE);
        spb.setVisibility(View.GONE);
        cdb.setVisibility(View.GONE);
    }

    private void donef() {
        pass=p.getText().toString();
        cpass=cp.getText().toString();
        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(cpass) )
        {
            Toast.makeText(this,"Password and RePassword should be  Non-Empty",Toast.LENGTH_LONG).show();
        }
        else if (pass.equals(cpass))
        {
            pd.setCanceledOnTouchOutside(false);
            pd.setTitle("Registring");
            pd.setMessage("Please Wait We Are Creating Your Account...");
            pd.show();
            signin(em,cpass);
        }
        else
        {
            Toast.makeText(this,"Password and RePassword should be same.",Toast.LENGTH_LONG).show();
        }


    }

    private void saveInfo() {
        FirebaseUser currentUser=mAuth.getCurrentUser();
        String UID=currentUser.getUid();

        HashMap<String,String> userInfo=new HashMap<>();
        if(hasVehicle==0)
        {
            vTypr=0;
            vNo="XXXX-XXXX";
        }

        int tr=0;
        userInfo.put("Name",name);
        userInfo.put("Username",user);
        userInfo.put("Aadhar",adhar);
        userInfo.put("Email",em);
        userInfo.put("MobileNo",mobile);
        userInfo.put("VType", String.valueOf(vTypr));
        userInfo.put("VNumber",vNo);
        userInfo.put("VPic",viUri);
        userInfo.put("Profile",picUri);
        userInfo.put("Profile_thumb",picUriThumb);
        userInfo.put("Password",cpass);
        userInfo.put("AllRide", String.valueOf(tr));

        mDatabase.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),"Register Succussfully!!",Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(SignUpActivity.this,MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                }
                else
                {
                    mAuth.getCurrentUser().delete();
                    pd.hide();
                    Toast.makeText(SignUpActivity.this,"Try Again.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signin(String eml, String psw) {
        mAuth.createUserWithEmailAndPassword(eml,psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            saveInfo();
                        }
                        else
                        {
                            pd.hide();
                            Toast.makeText(SignUpActivity.this,"Registration Failed!.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void choosePic() {
       /*Intent picIntent=new Intent();
        picIntent.setType("image/*");
        picIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(picIntent,"Pick Vehicle Image via:-"),10);
        */
       CropImage.activity()
               .setAspectRatio(1,1)
               .setAllowCounterRotation(true)
               .setAllowRotation(true)
               .setAutoZoomEnabled(true)
               .setGuidelines(CropImageView.Guidelines.ON)
               .start(SignUpActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK)
            {
                pdi=new ProgressDialog(this);
                pdi.setCanceledOnTouchOutside(false);
                pdi.setMessage("Uploading");
                pdi.show();

                resultUri=result.getUri();

                /*
                Write Code Of Compress Image
                 */
                StorageReference filePath=mStorage.child(mAuth.getCurrentUser().getUid()+"vehicle_img.jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            viUri=task.getResult().getDownloadUrl().toString();
                            Toast.makeText(getApplicationContext(),"Successfully Image Upoaded",Toast.LENGTH_SHORT).show();
                            Picasso.with(SignUpActivity.this).load(viUri).into(vi);
                            pdi.dismiss();


                        }
                        else
                        {
                            pdi.hide();
                            Toast.makeText(getApplicationContext(),"Try Agian",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else if (resultCode== CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
