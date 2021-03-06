package com.example.mobileterm.Init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class GoogleLoginSetupActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    TextView userEmail;
    TextView userName;

    EditText memberInfoPhone;
    EditText memberInfoNickname;
    EditText memberInfoDate;
    EditText memberInfoAddress;

    String uid;
    String name;
    String email;
    String phone;
    String date;
    String nickname;
    String address;
    ArrayList<String> nicknames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login_setup);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        Bundle data = intent.getExtras();

        uid = data.getString("uid");
        name = data.getString("name");
        email = data.getString("email");
        phone = data.getString("phone");

        userEmail = findViewById(R.id.UserEmail);
        userName = findViewById(R.id.UserName);


        userEmail.setText(email);
        userName.setText(name);

        memberInfoDate = (EditText) findViewById(R.id.MemberInfoDateGoogle);
        memberInfoNickname = (EditText) findViewById(R.id.MemberInfoNickNameGoogle);
        memberInfoPhone = (EditText) findViewById(R.id.MemberInfoPhoneGoogle);
        memberInfoAddress = (EditText) findViewById(R.id.MemberInfoAddressGoogle);

        CalendarDay calendarDay = CalendarDay.today();
        String year = Integer.toString(calendarDay.getYear());
        String month = Integer.toString(calendarDay.getMonth()+1);
        String day = Integer.toString(calendarDay.getDay());
        String regDate = year+month+day;

        Button registerButton = findViewById(R.id.RegisterButtonGoogle);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("??? ??????????",memberInfoDate.getText().toString());
                date = memberInfoDate.getText().toString();
                nickname = memberInfoNickname.getText().toString();
                phone = memberInfoPhone.getText().toString();
                address = memberInfoAddress.getText().toString();
                dbInsertion(name, date, phone, email, nickname, regDate, address);
            }
        });

        nicknames = new ArrayList<>();
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document: task.getResult()) {
                        if (document.exists()) {
                            nicknames.add((String) document.getData().get("nickname"));
                        }
                    }
                }
            }
        });

    }

    private void dbInsertion(String name, String date, String phone, String email, String nickname, String regDate, String address){
        Log.e("temp",date);
        if (name.length() > 0 && date.length() == 6 && phone.length() >= 8 && email.contains("@") && nickname.length() < 20 && !nicknames.contains(nickname) && address.contains("???") && address.contains("???")){
            UserInfoClass userInfo = new UserInfoClass(name, date, phone, email, nickname, regDate, address);

            db.collection("Users").document(user.getUid()).set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    StartToast("??????????????? ?????????????????????.");
                    Bundle data = new Bundle();
                    data.putString("uid",user.getUid());
                    data.putString("nickname",nickname);
                    data.putString("email",user.getEmail());
                    data.putString("phone",user.getPhoneNumber());
                    StartActivity(MainActivity.class, data);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    StartToast("????????? ?????????????????? ??????????????? ?????????????????????.");
                    user.delete();
                }
            });
        }else{
            CheckSignUpMemberInfoCondition(name, date, phone, email, nickname, address);
        }
    }

    private void CheckSignUpMemberInfoCondition(String name, String date, String phone, String email, String nickname, String address) {
        // ???????????? ???????????? ???????????? ??????????????? ???????????? ????????????.
        // ????????? ????????? ????????? ???????????? ?????? ????????? ?????????????????? ??????.
        Log.e("temp", "CheckSignUpMemberInfoCondition: " + user.getEmail());
        user.delete();

        if(name.length() <= 0) {
            StartToast("?????? ????????? ????????? ?????????????????? : 1??? ??????");
        }
        else if(date.length() < 6) {
            StartToast("??????????????? ????????? ?????????????????? : 6??? ??????");
        }
        else if(phone.length() < 8) {
            StartToast("??????????????? ????????? ?????????????????? : 8??? ??????");
        }
        else if(email.contains("@") == false){
            StartToast("????????? ????????? ??????????????????");
        }else if (nicknames.contains(nickname)){
            StartToast("???????????? ?????????????????????.");
        } else if (nickname.length() < 20) {
            StartToast("????????? ????????? ??????????????????.");
        }else{
            StartToast("????????? ??????????????????.");
        }
    }

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    private void StartActivity(Class c, Bundle data){
        Intent intent = new Intent(this, c);
        intent.putExtras(data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}