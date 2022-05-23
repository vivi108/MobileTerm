package com.example.mobileterm.Init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    // 회원가입하는 액티비티, 회원가입이 완료 되면 메인 액티비티가 실행된다
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordCheckedEditText;


    EditText memberInfoName;
    EditText memberInfoDate;
    EditText memberInfoPhone;
    EditText memberInfoNickname;
    EditText memberInfoAddress;
    private static final String TAG = "SignUpActivity";
    ArrayList<String> nicknames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        emailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
        passwordCheckedEditText = findViewById(R.id.PasswordCheckEditText);


        memberInfoName = findViewById(R.id.MemberInfoName);
        memberInfoDate = findViewById(R.id.MemberInfoDate);
        memberInfoPhone = findViewById(R.id.MemberInfoPhone);
        memberInfoNickname = findViewById(R.id.MemberInfoNickname);
        memberInfoAddress = findViewById(R.id.MemberInfoAddress);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.RegisterButton).setOnClickListener(onClickListener);

        nicknames = new ArrayList<>();
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists())
                            nicknames.add((String) document.getData().get("nickname"));
                    }
                    Log.d(TAG,"got all nicknames");
                }
            }
        });
    }

    public void onStart(){
        super.onStart();
        user = mAuth.getCurrentUser();
        if (user != null){
            user.reload();
        }
    }

    private void SignUp(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordCheck = passwordCheckedEditText.getText().toString();
        CalendarDay calendarDay = CalendarDay.today();
        String year = Integer.toString(calendarDay.getYear());
        String month = Integer.toString(calendarDay.getMonth()+1);
        String day = Integer.toString(calendarDay.getDay());
        String regDate = year+month+day;
        String name = memberInfoName.getText().toString();
        String date = memberInfoDate.getText().toString();
        String phone = memberInfoPhone.getText().toString();
        String nickname = memberInfoNickname.getText().toString();
        String address = memberInfoAddress.getText().toString();



        if (email.length() > 0 && password.length() > 0 && !nicknames.contains(nickname) && address.contains("시") && address.contains("구")){
            if (password.equals(passwordCheck)){
                Log.d(TAG, "Password check");
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "create user with email success");
                            user = mAuth.getCurrentUser();
                            dbInsertion(name, date, phone, email, nickname, regDate, address);
                        }else{
                            StartToast("회원가입에 실패하였습니다.");
                        }
                    }
                });
            }else{
                StartToast("비밀번호가 일치하지 않습니다.");
            }
        }else{
            CheckSignUpCondition(email, password, passwordCheck,address);
        }

    }

    private void dbInsertion(String name, String date, String phone, String email, String nickname, String regDate, String address){
        if (name.length() > 0 && date.length() >= 6 && phone.length() >= 8 && email.contains("@") && nickname.length() < 20){
            UserInfoClass userInfo = new UserInfoClass(name, date, phone, email, nickname, regDate, address);

            db.collection("Users").document(user.getUid()).set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    StartToast("회원가입에 성공하였습니다.");
                    Bundle data = new Bundle();
                    data.putString("uid",user.getUid());
                    data.putString("name",user.getDisplayName());
                    data.putString("email",user.getEmail());
//                    data.putString("phone",user.getPhoneNumber());
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
            CheckSignUpMemberInfoCondition(name, date, phone, email, nickname, address);
        }
    }


    private void CheckSignUpMemberInfoCondition(String name, String date, String phone, String email, String nickname, String address) {
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
        }else if (nicknames.contains(nickname)){
            StartToast("닉네임이 중복되었습니다.");
        } else if (nickname.length() < 20) {
            StartToast("닉네임 길이를 확인해주세요.");
        }else{
            StartToast("주소를 확인해주세요.");
        }
    }

    private void CheckSignUpCondition(String email, String password, String passwordCheck, String address) {
        if(email.length() <= 0) {
            StartToast("이메일 길이를 확인해주세요 : 1자 이상");
        }
        else if(password.length() <= 0) {
            StartToast("비밀번호 길이를 확인해주세요 : 1자 이상");
        }
        else if(passwordCheck.length() <= 0) {
            StartToast("비밀번호 확인 문자를 확인해주세요 : 1자 이상");
        }else{
            StartToast("주소를 확인해주세요.");
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.RegisterButton:
                    SignUp();
                    break;
            }
        }
    };

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    private void StartActivity(Class c){
        Intent intent = new Intent(this, c);
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