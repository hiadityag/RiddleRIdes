package com.example.aditya.rideapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetResetActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emaile,OTP,password;
    Button emailB,otpB,npB;
    LinearLayout eb,otpb,pb;
    ProgressDialog svc;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_reset);
        init();
        mAuth=FirebaseAuth.getInstance();
    }

    private void init() {
        emaile= (EditText) findViewById(R.id.emailEditforget);
        OTP= (EditText) findViewById(R.id.otp);
        password= (EditText) findViewById(R.id.npedit);
        emailB= (Button) findViewById(R.id.emailButton);
        otpB= (Button) findViewById(R.id.otpButton);
        npB= (Button) findViewById(R.id.cpButton);

        eb= (LinearLayout) findViewById(R.id.emailBlock);
        otpb= (LinearLayout) findViewById(R.id.otpBlock);
        pb= (LinearLayout) findViewById(R.id.passwordBlockforget);

        emailB.setOnClickListener(this);
        otpB.setOnClickListener(this);
        npB.setOnClickListener(this);

        svc=new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.emailButton:
                /**/
                sendmail();
                break;
            case R.id.otpButton:
                varifyOTP();
                break;
            case R.id.passwordBlock:
                changePass();
                break;
            default:
                break;
        }
    }

    private void changePass() {
        String pas=password.getText().toString();
        if (TextUtils.isEmpty(pas))
        {
            Toast.makeText(getApplicationContext(),"Password Should Contain 8 Letter.",Toast.LENGTH_LONG).show();
        }
        else
        {

            svc.setMessage("Updating Password");
            svc.setCanceledOnTouchOutside(false);
            svc.show();
            mAuth.getCurrentUser().updatePassword(pas).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                       svc.dismiss();
                        Toast.makeText(getApplicationContext(),"Pasword resetSuccessfully",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(ForgetResetActivity.this,LogInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        svc.hide();
                        Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_LONG).show();

                    }
                }
            });
            Intent in=new Intent(ForgetResetActivity.this,MainActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
            finish();
        }
    }

    private void varifyOTP() {
        String otpCode=OTP.getText().toString();
        mAuth.verifyPasswordResetCode(otpCode).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful())
                {
                    eb.setVisibility(View.GONE);
                    otpb.setVisibility(View.GONE);
                    pb.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void sendmail() {
        String email=emaile.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(ForgetResetActivity.this,"Please Enter Email First",Toast.LENGTH_LONG).show();
        }
        else
        {
            svc.setTitle("Sending Code");
            svc.setMessage("Please Wait A Moment...");
            svc.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            svc.setCanceledOnTouchOutside(false);
            svc.show();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        svc.dismiss();
                        Toast.makeText(getApplicationContext(),"Please Check Your Mail",Toast.LENGTH_LONG).show();
                        eb.setVisibility(View.GONE);
                        otpb.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Try Again Later.",Toast.LENGTH_LONG).show();
                    }
                }
            });
            /**/
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
