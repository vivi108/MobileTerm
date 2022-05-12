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
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class GoogleLoginSetupActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    TextView userEmail;
    TextView userName;

    EditText memberInfoPhone;
    EditText memberInfoNickname;
    EditText memberInfoDate;

    String uid;
    String name;
    String email;
    String phone;
    String date;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login_setup);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
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

        memberInfoDate = findViewById(R.id.MemberInfoDateGoogle);
        memberInfoNickname = findViewById(R.id.MemberInfoNickNameGoogle);
        memberInfoPhone = findViewById(R.id.MemberInfoPhoneGoogle);

        CalendarDay calendarDay = CalendarDay.today();
        String year = Integer.toString(calendarDay.getYear());
        String month = Integer.toString(calendarDay.getMonth()+1);
        String day = Integer.toString(calendarDay.getDay());
        String regDate = year+month+day;

        Button registerButton = findViewById(R.id.RegisterButtonGoogle);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = memberInfoDate.getText().toString();
                nickname = memberInfoNickname.getText().toString();
                phone = memberInfoPhone.getText().toString();
                dbInsertion(name, date, phone, email, nickname, regDate);
            }
        });

    }

    private void dbInsertion(String name, String date, String phone, String email, String nickname, String regDate){
        Log.e("temp",date);
        if (name.length() > 0 && date.length() == 6 && phone.length() >= 8 && email.contains("@") && nickname.length() < 10){
            UserInfoClass userInfo = new UserInfoClass(name, date, phone, email, nickname, regDate);
            db = FirebaseFirestore.getInstance();
            db.collection("Users").document(user.getUid()).set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    StartToast("회원가입에 성공하였습니다.");
                    Bundle data = new Bundle();
                    data.putString("uid",user.getUid());
                    data.putString("name",user.getDisplayName());
                    data.putString("email",user.getEmail());
                    data.putString("phone",user.getPhoneNumber());
                    StartActivity(MainActivity.class, data);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    StartToast("조건은 만족하였지만 회원가입에 실패하였습니다.");
                    user.delete();
                }
            });
        }else{
            CheckSignUpMemberInfoCondition(name, date, phone, email, nickname);
        }
    }

    private void CheckSignUpMemberInfoCondition(String name, String date, String phone, String email, String nickname) {
        // 메서드가 호출되는 시점에는 회원가입이 이루어진 상태이다.
        // 따라서 중간에 문제가 생겼다면 해당 계정을 삭제해주어야 한다.
        Log.e("temp", "CheckSignUpMemberInfoCondition: " + user.getEmail());
        user.delete();

        if(name.length() <= 0) {
            StartToast("회원 이름의 길이를 확인해주세요 : 1자 이상");
        }
        else if(date.length() < 6) {
            StartToast("생년월일의 길이를 확인해주세요 : 6자 이상");
        }
        else if(phone.length() < 8) {
            StartToast("전화번호의 길이를 확인해주세요 : 8자 이상");
        }
        else if(email.contains("@") == false){
            StartToast("이메일 형식을 확인해주세요");
        }else if (nickname.length() < 10) {
            StartToast("닉네임 길이를 확인해주세요.");
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