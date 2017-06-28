package com.example.roshan.ncab;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_lgoin;
    private TextView txt_signup,txt_forgetpsw;
    private EditText editTextemail,editTextpassword;
    private ProgressDialog progressdialog;
    private FirebaseAuth firebaseauth;
    DatabaseReference mdatabase;
    DatabaseReference userdatabase;
    private Firebase mref;
    double lat;
    double lon;
    String latitude;
    LatLng latLng;
    LatLng longitude;
    SharedPreferences sharedpreferences;
    long contact;
    String licence;
    String username;
    String id;
    String emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        progressdialog = new ProgressDialog(this);
        firebaseauth= FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        mref =new Firebase("https://ncab-80692.firebaseio.com/");
     //   FirebaseMessaging.getInstance().subscribeToTopic("news");
        if(firebaseauth.getCurrentUser() != null){
           finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }


        btn_lgoin=(Button)findViewById(R.id.btn_signin);
        txt_signup=(TextView)findViewById(R.id.txt_signup);
        editTextemail=(EditText)findViewById(R.id.email);
        editTextpassword=(EditText)findViewById(R.id.password);
        txt_forgetpsw=(TextView)findViewById(R.id.txt_forgetpassword);
        txt_forgetpsw.setOnClickListener(this);
        btn_lgoin.setOnClickListener(this);
        txt_signup.setOnClickListener(this);
        LoginActivity.this.setTitle("Login");
    }

    @Override
    public void onClick(View view) {
        if(view==btn_lgoin){
            loginUser();
        }
        if (view==txt_signup){
            finish();
            startActivity(new Intent(getApplicationContext(),DriverRegister.class));

        }
        if (view==txt_forgetpsw){
            startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
        }

    }
    private void loginUser(){
        final String email=editTextemail.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressdialog.setMessage("Login User...");
        progressdialog.show();
//

        firebaseauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressdialog.dismiss();
                        if (task.isSuccessful()){
                            finish();
                            mdatabase = FirebaseDatabase.getInstance().getReference("driver");
                            mdatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            Post post = dataSnapshot1.getValue(Post.class);
                                            username = post.getUsername();
                                            licence = post.getLicence();
                                            contact = post.getContact();
                                            emailId=post.getEmail();
                                            id = post.getId();
                                         if (emailId.equals(firebaseauth.getCurrentUser().getEmail())) {
                                            sharedpreferences = getSharedPreferences("LONGITUDE", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putLong("Latitude", Double.doubleToLongBits(post.getLatitude()));
                                            editor.putLong("Longitude", Double.doubleToLongBits(post.getLongitude()));
                                             editor.putLong("contact", contact);
                                            editor.putString("licence", licence);
                                            editor.putString("username", username);
                                            editor.putString("id", id);
                                            editor.apply();
                                            final double lat1 = Double.longBitsToDouble(sharedpreferences.getLong("Latitude", 0));
                                            final double lng1 = Double.longBitsToDouble(sharedpreferences.getLong("Longitude", 0));
                                            contact =sharedpreferences.getLong("contact", 0);
                                            licence = sharedpreferences.getString("licence", null);
                                            username = sharedpreferences.getString("username", null);
                                            id = sharedpreferences.getString("id", null);
                                            System.out.println("ID :" + id);
                                            System.out.println("Username: " + username);
                                            longitude = new LatLng(lat1, lng1);
                                            System.out.println("Longitude :" + longitude);
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "Couldn't Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart(){
        super.onStart();


    }

}
