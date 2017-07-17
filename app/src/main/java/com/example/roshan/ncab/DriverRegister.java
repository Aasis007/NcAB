package com.example.roshan.ncab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class DriverRegister extends AppCompatActivity implements View.OnClickListener{

    private Button btn_register;
    private TextView txt_alreadyuser,txt_forgetpsw;
    private EditText editTextemail,editTextpassword,editTextContact,editTextUsername,editTextLicence;
    private ProgressDialog progressdialog;
    private FirebaseAuth firebaseauth;
    private Firebase mref;
    private RadioButton Rb_user,Rb_driver;
    DatabaseReference databaseReference;
    DatabaseReference userdatabase;
    private RadioGroup radioGroup;
    double lat;
    double lon;
    SharedPreferences sharedPreferences;
    String id;
    String licence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        progressdialog = new ProgressDialog(this);
        firebaseauth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("driver");
        userdatabase= FirebaseDatabase.getInstance().getReference("user");
        Firebase.setAndroidContext(this);
        mref =new Firebase("https://ncab-80692.firebaseio.com/");

        if(firebaseauth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        btn_register=(Button)findViewById(R.id.btn_register);
        txt_alreadyuser=(TextView)findViewById(R.id.txt_alreadyuser);
        editTextemail=(EditText)findViewById(R.id.email);
        editTextpassword=(EditText)findViewById(R.id.password);
        editTextContact=(EditText)findViewById(R.id.phn_number);
        editTextLicence=(EditText)findViewById(R.id.licence);
        editTextUsername=(EditText)findViewById(R.id.user_name);
        txt_forgetpsw=(TextView)findViewById(R.id.txt_forgetpassword);
        radioGroup=(RadioGroup)findViewById(R.id.radio_group) ;
        Rb_driver=(RadioButton)findViewById(R.id.rb_driver);
        Rb_user=(RadioButton)findViewById(R.id.rb_user);
        txt_forgetpsw.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        txt_alreadyuser.setOnClickListener(this);


        Rb_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextLicence.setVisibility(View.GONE);
                licence="1";
            }
        });

        Rb_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextLicence.setVisibility(View.VISIBLE);
            }
        });


        DriverRegister.this.setTitle("Register");
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
        final String email=editTextemail.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();
        final long contact= Long.parseLong(editTextContact.getText().toString());
        if (Rb_user.isChecked()){
            licence="1";
        }
        if (Rb_driver.isChecked()){
            licence=editTextLicence.getText().toString();
        }

        final String username=editTextUsername.getText().toString();


        if (TextUtils.isEmpty(email)) {

            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(licence)){
            Toast.makeText(this, "Enter Your Licence", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Enter Your Username", Toast.LENGTH_SHORT).show();
        }

        progressdialog.setMessage("Registering User...");
        progressdialog.show();



        firebaseauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressdialog.dismiss();

                        if (task.isSuccessful()){
                            finish();
                            if (Rb_driver.isChecked()){

                                 id=databaseReference.push().getKey();
                            //    mref.child(username).setValue(new Post(email,0.0,0.0,contact,licence,username));
                                Post post=new Post(id,email,0.0,0.0,contact,licence,username);
                                databaseReference.child(id).setValue(post);
                            }
                            if (Rb_user.isChecked()){
                                //mref.child(username).setValue(new Post(email,0.0,0.0,contact,null,username));
                                id=userdatabase.push().getKey();
                                //    mref.child(username).setValue(new Post(email,0.0,0.0,contact,licence,username));
                                Post post=new Post(id,email,0.0,0.0,contact,licence,username);
                                userdatabase.child(id).setValue(post);
                            }

                            sharedPreferences = getSharedPreferences("LONGITUDE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong("contact",contact);
                          //  editor.putLong("contact", Double.doubleToLongBits(contact));
                            editor.putString("licence",licence);
                            editor.putString("username",username);
                            editor.putString("id", id);
                            editor.putString("current_email",firebaseauth.getCurrentUser().getEmail());
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(DriverRegister.this, "Register Success", Toast.LENGTH_SHORT).show();



                            SharedPreferences sharedpreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
                            String token = sharedpreferences.getString("token", null);
                            String current_email=sharedPreferences.getString("current_email",null);
                            System.out.println("Current User :"+current_email);
                            System.out.println("Token :"+token);
                            new Background(DriverRegister.this).execute("insert",token,current_email);
                        }else {
                            Toast.makeText(DriverRegister.this, "Couldn't register email may exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
