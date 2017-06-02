package com.example.roshan.ncab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Email;
    private Button btnReset, btnBack;
    private ProgressDialog progressdialog;
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        progressdialog = new ProgressDialog(this);
        firebaseauth= FirebaseAuth.getInstance();
        Email=(EditText)findViewById(R.id.editTextemail);
        btnBack=(Button)findViewById(R.id.btn_back);
        btnReset=(Button)findViewById(R.id.btn_reset_password);
        btnReset.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        ResetPasswordActivity.this.setTitle("Password Reset");
    }

    @Override
    public void onClick(View view) {
        if (view==btnBack){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));

        }
        if (view==btnReset){
            resetpassword();
        }

    }

private void resetpassword(){
    String email = Email.getText().toString().trim();
  

    if (TextUtils.isEmpty(email)) {

        Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
        return;
    }
   
    progressdialog.setMessage("Resetting Password...");
    progressdialog.show();


    firebaseauth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override

                public void onComplete(@NonNull Task<Void> task) {
                    progressdialog.dismiss();
                    if (task.isSuccessful()){
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                         Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
}
}