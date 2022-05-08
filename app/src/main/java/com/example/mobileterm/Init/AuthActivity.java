package com.example.mobileterm.Init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthActivity extends AppCompatActivity {
    // 로그인 되어있으면 메인 액티비티 실행하고 아니면 로그인 액티비티를 실행하는 액티비티
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private static final String TAG = "AuthActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        startLoading();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user != null){
            Log.d(TAG, "user not null");
            FirebaseFirestore fb = FirebaseFirestore.getInstance();
            DocumentReference documentReference = fb.collection("Users").document(user.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG, "task success");
                        DocumentSnapshot document = task.getResult();
                        if (document != null){
                            if(document.exists()){
                                Log.d(TAG,"Should start main");
                                StartActivity(MainActivity.class);
                            }else{
                                Log.d(TAG, "should start login");
                                FirebaseAuth.getInstance().signOut();
                                StartActivity(LoginActivity.class);
                            }
                        }else{
                            Log.d(TAG,"document null");
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"Failure: null value");
                }
            });
        }else{
            Log.d(TAG, "user null");
            StartActivity(LoginActivity.class);
        }
    }

    private void StartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }
}