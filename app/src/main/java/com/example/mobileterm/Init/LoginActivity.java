package com.example.mobileterm.Init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private long backKeyPressedTime = 0;
    private Toast terminate_guide_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);

        findViewById(R.id.SignInButton).setOnClickListener(onClickListener);
        findViewById(R.id.CommonSignUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.FindPassWord).setOnClickListener(onClickListener);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);

    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            currentUser.reload();
        }
    }

    private void StartToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void SignIn(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.length() > 0 && password.length() > 0){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        firebaseUser = mAuth.getCurrentUser();
                        CheckUser(firebaseUser);
                    }else{
                        StartToast("????????? ?????? : ????????? ?????? ?????????");
                        Log.d("login","error due to wrong info");
                    }
                }
            });
        }else{
            StartToast("????????? ??????????????????.");

        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.SignInButton:
                    SignIn();
                    break;
                case R.id.CommonSignUpButton:
                    StartActivity(SignUpActivity.class);
                    break;
                case R.id.FindPassWord:
                    StartActivity(PasswordInitActivity.class);
                    break;
            }
        }
    };

    public void CheckUser(FirebaseUser firebaseUser){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fb.collection("Users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document != null){
                        if (document.exists()){
                            Bundle data = new Bundle();
                            data.putString("uid", firebaseUser.getUid());
                            data.putString("nickname",(String) document.getData().get("nickname"));
                            data.putString("email", (String) document.getData().get("email"));
                            data.putString("phone",(String) document.getData().get("phone"));
                            StartActivity(MainActivity.class, data);
                        }
                    }
                }
            }
        });
    }

    private void StartActivity(Class c){
        Intent intent = new Intent(this, c);
        // ????????? ?????? ????????? ?????? ?????? ?????? ????????? ???????????? ?????? ????????? ??????????????? ??????
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void StartActivity(Class c, Bundle data){
        Intent intent = new Intent(this, c);
        intent.putExtras(data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // ????????????????????? ??????????????? ????????? ????????????.
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 1000) {
            backKeyPressedTime = System.currentTimeMillis();
            terminate_guide_msg = Toast.makeText(this, "\'??????\' ????????? ?????? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT);
            terminate_guide_msg.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
            terminate_guide_msg.cancel();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }


}