package com.example.mobileterm.StudyGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.ChattingActivity.ChattingActivity;
import com.example.mobileterm.R;

public class StudySettingActivity extends AppCompatActivity {
    Activity activity;
    Intent intent = null;
    ImageButton btn_study_setting_back;
    Button btn_study_setting_groupCalender, btn_study_chatting;
    Switch sw_online_offline_study;
    TextView tv_study_setting_leader;
    LinearLayout ll_study_setting_onlineORnot;
    String studyName = null;
    String myNickName = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_setting);
        init();
        setting();
        addListener();
    }

    private void init(){
        activity = this;
//        btn_study_setting_back = findViewById(R.id.btn_study_setting_back);
        btn_study_setting_groupCalender = findViewById(R.id.btn_study_setting_groupCalender);
        btn_study_chatting = findViewById(R.id.btn_study_chatting);
        sw_online_offline_study = findViewById(R.id.sw_online_offline_study);
        tv_study_setting_leader = findViewById(R.id.tv_study_setting_leader);
        ll_study_setting_onlineORnot = findViewById(R.id.ll_study_setting_onlineORnot);
    }

    private void setting(){
        // 파이어베이스에서 스터디그룹 생성자 여부 확인
        // 스터디그룹 생성자이면 ll_study_setting_onlineORnot 보이기
        //온라인 -> switch on, 오프라인 -> switch off
        // 파이어베이스에서 받아와서 설정
        ll_study_setting_onlineORnot.setVisibility(View.GONE);
    }

    private void addListener(){
        btn_study_setting_back.setOnClickListener(Listener_back);
        btn_study_setting_groupCalender.setOnClickListener(Listener_groupCalender);
        btn_study_chatting.setOnClickListener(Listener_chatting);
    }

    private View.OnClickListener Listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener Listener_groupCalender = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // goto Main + groupCalender fragment View
        }
    };

    private View.OnClickListener Listener_chatting = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // goto Chatting room
            intent = new Intent(activity, ChattingActivity.class);
            intent.putExtra("StudyName", studyName);
            intent.putExtra("myNickName", myNickName);

        }
    };
}
