package com.example.aditya.rideapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user,password;
    private Button go,ftvr;
    private ProgressDialog pd;
    private Intent intent;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        init();
        mAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(this);
    }

    private void init() {
        user= (EditText) findViewById(R.id.emailEdit);
        password= (EditText) findViewById(R.id.passwordEdit);
        go= (Button) findViewById(R.id.goButton);
        ftvr= (Button) findViewById(R.id.forgetTextviewlogin);
        go.setOnClickListener(this);
        ftvr.setOnClickListener(this);
    }

    private void updateUI(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            pd.dismiss();
                            intent=new Intent(LogInActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            pd.hide();
                            Toast.makeText(LogInActivity.this,"Try again later!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.goButton:
                gopress();
                break;
            case R.id.forgetTextviewlogin:
                fogetpress();
                break;
            default:
                break;
        }
    }

    private void gopress() {
        String User=user.getText().toString();
        String Pass=password.getText().toString();
        if (TextUtils.isEmpty(User) || TextUtils.isEmpty(Pass))
        {
            Toast.makeText(LogInActivity.this,"All Fields Are Required!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            pd.setTitle("Logging In");
            pd.setMessage("Please Wait While We Are Checking Your Credeintial...");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            updateUI(User,Pass);
        }
    }

    private void fogetpress() {
        intent=new Intent(LogInActivity.this,ForgetResetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
