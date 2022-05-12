package com.example.mobileterm.Init;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobileterm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class GoogleLoginSetupActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    TextView userEmail;
    TextView userName;
    TextView userPhone;

    EditText memberInfoNickname;
    EditText memberInfoDate;

    String uid;
    String name;
    String email;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_google_login_setup);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();

        uid = data.getString("uid");
        name = data.getString("name");
        email = data.getString("email");
        phone = data.getString("phone");

    }
}