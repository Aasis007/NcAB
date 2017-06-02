package com.example.roshan.ncab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {
    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail, btnRemoveUser,
            changeEmail, changePassword, sendEmail,  signOut;
    private ProgressDialog progressdialog;
    private EditText oldEmail, newEmail, password;
    //private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressdialog = new ProgressDialog(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(getString(R.string.app_name));
//        setSupportActionBar(toolbar);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(Profile.this, LoginActivity.class));
                    finish();
                }
            }
        };

        btnChangeEmail = (Button) findViewById(R.id.btn_changeemail);
        btnChangePassword = (Button) findViewById(R.id.btn_changepassword);
        btnSendResetEmail = (Button) findViewById(R.id.btn_sendpasswordresetemail);
        btnRemoveUser = (Button) findViewById(R.id.btn_removeuser);
        changeEmail = (Button) findViewById(R.id.changeemail);
        changePassword = (Button) findViewById(R.id.changepassword);
        sendEmail = (Button) findViewById(R.id.sendemail);
        signOut = (Button) findViewById(R.id.btn_signout);

        oldEmail = (EditText) findViewById(R.id.old_email);
        newEmail = (EditText) findViewById(R.id.new_email);
        password = (EditText) findViewById(R.id.newPassword);
        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                changeEmail.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.GONE);

            }
        });


        changeEmail.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if (user != null && !newEmail.getText().toString().trim().equals("")) {
                    user.updateEmail(newEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Profile.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                        signOut();
                                    } else {
                                        Toast.makeText(Profile.this, "Failed to update email!", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                } else if (newEmail.getText().toString().trim().equals("")) {
                    newEmail.setError("Enter email");

                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.VISIBLE);
                sendEmail.setVisibility(View.GONE);

            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null && !password.getText().toString().trim().equals("")) {
                    if (password.getText().toString().trim().length() < 6) {
                       password.setError("Password too short, enter minimum 6 characters");
                        //progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(password.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Profile.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                            signOut();
                                          //  progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(Profile.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                            //progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Enter password");
//                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnSendResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.VISIBLE);

            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // progressBar.setVisibility(View.VISIBLE);
                if (!oldEmail.getText().toString().trim().equals("")) {
                    auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Profile.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                        signOut();
                                        //progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(Profile.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                      //  progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else {
                    oldEmail.setError("Enter email");
                   // progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Profile.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Profile.this, MainActivity.class));
                                        finish();
                                       // progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(Profile.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        //progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

}
