package com.example.mobileterm.Init;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginSelectionActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private static final int RC_SIGN_IN = 9001;
    SignInButton googleButton;
    private String TAG = "LoginSelectionActivity";
    boolean autoLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        findViewById(R.id.emailLoginButton).setOnClickListener(onClickListener);
        findViewById(R.id.googleButton).setOnClickListener(onClickListener);
        mAuth = FirebaseAuth.getInstance();
        googleButton = findViewById(R.id.googleButton);
        db = FirebaseFirestore.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.emailLoginButton:
                    StartActivity(LoginActivity.class);
                    break;
                case R.id.googleButton:
                    user = mAuth.getCurrentUser();
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
                                            Bundle data = new Bundle();
                                            data.putString("uid",user.getUid());
                                            data.putString("name",user.getDisplayName());
                                            data.putString("email",user.getEmail());
//                                            signIn();
                                            StartActivity(MainActivity.class, data);
                                        }else{
                                            Log.d(TAG, "should start login");
                                            signIn();
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
                        Log.d(TAG,"user null");
                        signIn();
                    }
                    break;
            }
        }
    };

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }catch (ApiException e){
                Log.d(TAG,e.toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // sign in success
                    Log.d(TAG,"sign in success");
                    FirebaseUser user = mAuth.getCurrentUser();
//                    db.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                Log.d(TAG,"여기 오면안된다 firebaseAuthWithGoogle");
//                                Bundle data = new Bundle();
//                                data.putString("uid",user.getUid());
//                                data.putString("name",user.getDisplayName());
//                                data.putString("email",user.getEmail());
//                                data.putString("phone",user.getPhoneNumber());
//                                StartActivity(MainActivity.class, data);
//                            }else{
//                                Log.d(TAG,"이게 실행되야하는데..");
//                                updateUI(user);
//                            }
//                        }
//                    });
                    db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                QuerySnapshot querySnapshot = task.getResult();
                                for (DocumentSnapshot document :querySnapshot){
                                    if (document.getId().equals(user.getUid())){
                                        Log.d(TAG,"여기 오면안된다 firebaseAuthWithGoogle");
                                       autoLogin = true;
                                       break;
                                    }
                                }
                                if (autoLogin){
                                    Bundle data = new Bundle();
                                    data.putString("uid",user.getUid());
                                    data.putString("name",user.getDisplayName());
                                    data.putString("email",user.getEmail());
                                    data.putString("phone",user.getPhoneNumber());
                                    StartActivity(MainActivity.class, data);
                                }else{
                                    updateUI(user);
                                }


                            }
                        }
                    });
//                    updateUI(user);
                }else{
                    // fail
                    Log.d(TAG,"sign in fail");
                    updateUI(null);
                }

            }
        });
    }

    private void updateUI(FirebaseUser user){
        if (user != null){
            Intent intent = new Intent(this, GoogleLoginSetupActivity.class);
            Bundle data = new Bundle();
            data.putString("uid",user.getUid());
            data.putString("name",user.getDisplayName());
            data.putString("email",user.getEmail());
            data.putString("phone",user.getPhoneNumber());
            intent.putExtras(data);
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "login failed");
        }
    }

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void StartActivity(Class c){
        Intent intent = new Intent(this, c);
        // 동일한 창이 여러번 뜨게 하지 않고 기존에 켜져있던 창을 앞으로 끌어와주는 기능
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void StartActivity(Class c, Bundle data){
        Intent intent = new Intent(this, c);
        intent.putExtras(data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}