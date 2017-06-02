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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private Button btn_register;
    private TextView txt_alreadyuser,txt_forgetpsw;
    private EditText editTextemail,editTextpassword,editTextaddress,editid;
    private ProgressDialog progressdialog;
    private FirebaseAuth firebaseauth;
    private Firebase mref;

    private int id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        progressdialog = new ProgressDialog(this);
        firebaseauth= FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        mref =new Firebase("https://fir-auth-f3cc8.firebaseio.com/");
        if(firebaseauth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        btn_register=(Button)findViewById(R.id.btn_register);
        txt_alreadyuser=(TextView)findViewById(R.id.txt_alreadyuser);
        editTextemail=(EditText)findViewById(R.id.email);
        editTextpassword=(EditText)findViewById(R.id.password);
        editTextaddress=(EditText)findViewById(R.id.address);
        editid =(EditText)findViewById(R.id.edit_id);
        txt_forgetpsw=(TextView)findViewById(R.id.txt_forgetpassword);
        txt_forgetpsw.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        txt_alreadyuser.setOnClickListener(this);
        Register.this.setTitle("Register");
    }

    @Override
    public void onClick(View view) {
        if(view==btn_register){
            registerUser();
        }
        if (view==txt_alreadyuser){

            startActivity(new Intent(getApplicationContext(),LoginActivity.class));

        }
        if (view==txt_forgetpsw){
            startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
        }

    }
    private void registerUser(){
        String email=editTextemail.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();
        final String address=editTextaddress.getText().toString();
        String id_text=editid.getText().toString();
        if (TextUtils.isEmpty(email)) {

            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
    return;
        }
        if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
     return;
        }
        if (TextUtils.isEmpty(address)){
            Toast.makeText(this, "Enter Your Address", Toast.LENGTH_SHORT).show();
        }

//        mref.push().setValue(id);
//        id++;
//        Firebase child=mref.child("Address");
//        Firebase child2=mref.child("Email");
//        Firebase child3=mref.child("Password");
//        child3.setValue(password);
//        child2.setValue(email);
//        child.setValue(address);
//        mref.child("Driver"+id).child("Address").setValue(address);
//        mref.child("Driver"+id).child("Email").setValue(email);
//        id++;
       // mref.child("Email").setValue(email);
        progressdialog.setMessage("Registering User...");
        progressdialog.show();
//
        firebaseauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressdialog.dismiss();

                        if (task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Register.this, "Couldn't register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
