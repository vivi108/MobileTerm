package com.example.mobileterm.Init;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mobileterm.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken()
    }
}