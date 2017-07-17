package com.example.roshan.ncab;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by roshan on 6/3/17.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";
    private FirebaseAuth firebaseauth;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,refreshedToken);

        firebaseauth= FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        SharedPreferences sharedpreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("token", refreshedToken);
        editor.apply();

    }
}