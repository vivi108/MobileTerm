package com.example.mobileterm.StudyGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudyMakeActivity extends AppCompatActivity {
    ImageButton imgbtn_make_study_cancel, btn_make_study_show_pw;
    EditText et_make_study_name, et_make_study_set_member, pt_entrance_pw, et_make_study_tag, et_make_study_description;
    RadioGroup rdg_make_study_open;
    Button btn_make_study_make, btn_make_study_cancel;
    LinearLayout ll_make_study_closed_pw;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    String uid;
    String TAG = "MakeStudy";

    int i;

    Activity activity;
    Intent intent = null;
    String myNickName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_new_study);
        init();
        setting();
        addListener();
    }

    private void init(){
        activity = this;

        imgbtn_make_study_cancel = findViewById(R.id.imgbtn_make_study_cancel);
        btn_make_study_show_pw = findViewById(R.id.btn_make_study_show_pw);
        et_make_study_name = findViewById(R.id.et_make_study_name);
        et_make_study_set_member = findViewById(R.id.et_make_study_set_member);
        pt_entrance_pw = findViewById(R.id.pt_entrance_pw);
        et_make_study_tag = findViewById(R.id.et_make_study_tag);
        et_make_study_description = findViewById(R.id.et_make_study_description);
        rdg_make_study_open = findViewById(R.id.rdg_make_study_open);
        btn_make_study_make = findViewById(R.id.btn_make_study_make);
        btn_make_study_cancel = findViewById(R.id.btn_make_study_cancel);
        ll_make_study_closed_pw = findViewById(R.id.ll_make_study_closed_pw);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setting(){
        ll_make_study_closed_pw.setVisibility(View.GONE);
        btn_make_study_show_pw.setImageResource(R.drawable.ic_eye_opened);
        i = 0;
        getName(user);
        if(user != null){
            uid = user.getUid();
        }
    }

    private void addListener(){
        imgbtn_make_study_cancel.setOnClickListener(Listener_cancel_make_study);
        btn_make_study_cancel.setOnClickListener(Listener_cancel_make_study);
        btn_make_study_show_pw.setOnClickListener(Listener_pw_show);
        rdg_make_study_open.setOnCheckedChangeListener(Listener_study_openedORnot);
        btn_make_study_make.setOnClickListener(Listener_make_study);
    }

    private View.OnClickListener Listener_cancel_make_study = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener Listener_pw_show = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(i == 0){
                btn_make_study_show_pw.setImageResource(R.drawable.ic_eye_opened);
                pt_entrance_pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                i = 1;
            }
            else {
                btn_make_study_show_pw.setImageResource(R.drawable.ic_eye_crossed);
                pt_entrance_pw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                i = 0;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener Listener_study_openedORnot = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rdb_open_study:
                    ll_make_study_closed_pw.setVisibility(View.GONE);
                    break;
                case R.id.rdb_closed_study:
                    ll_make_study_closed_pw.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private View.OnClickListener Listener_make_study = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String openORnot = null;
            String password = null;
            String getStudyName = et_make_study_name.getText().toString();
            String getMembers = et_make_study_set_member.getText().toString();
            if (ll_make_study_closed_pw.getVisibility() == View.GONE){
                openORnot = "open";
            }
            else if(ll_make_study_closed_pw.getVisibility() == View.VISIBLE){
                openORnot = "close";
                password = pt_entrance_pw.getText().toString();
            }
            String tags = et_make_study_tag.getText().toString();
            String description = et_make_study_description.getText().toString();

            String ID = openORnot + " " + getStudyName;

            Map<String, Object> study = new HashMap<>();
            study.put("studyName", getStudyName);
            study.put("maxNumPeople", getMembers);
            study.put("siOpened", openORnot);
            study.put("password", password);
            study.put("description", description);
            study.put("leader", myNickName);
            study.put("memberList", myNickName + "/");
            // study.put("studyID", ID);
            study.put("tags", tags);
            db.collection("Study").document(ID)
                    .set(study).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "Make Study Success!");
                    Toast.makeText(activity, "Make study success!", Toast.LENGTH_SHORT);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });

            String[] tag = tags.split(" ");
            // 파이어베이스에서 태그 컬렉션 읽어오기 -> 태그와 일치하는 문서 필드에 스터디 이름 저장(구분자 : /)
            for(int i = 0; i < tag.length; i++){
                switch (tag[i]){
                    case "#공무원":
                    case "#자격증":
                    case "#토익":
                    case "#전공":
                    case "#대외활동":
                    case "#취업/면접":
                    case "#어학":
                        db.collection("StudyTags").document(tag[i])
                                .set(study).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                        break;
                }
            }
            finish(); // 화면 재실행 가능하도록 구현
        }
    };

    private void getName(FirebaseUser firebaseUser) {
        DocumentReference documentReference = db.collection("Users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            String a = (String) document.getData().get("nickname");
                            myNickName = a;
                        }
                    }
                }
            }
        });
    }

    public String sendStudyID(){
        String openORnot = null;
        if (ll_make_study_closed_pw.getVisibility() == View.GONE){
            openORnot = "open";
        }
        else if(ll_make_study_closed_pw.getVisibility() == View.VISIBLE){
            openORnot = "close";
        }
        String getStudyName = et_make_study_name.getText().toString();
        String ID = openORnot + " " + getStudyName;

        return ID;
    }
}
