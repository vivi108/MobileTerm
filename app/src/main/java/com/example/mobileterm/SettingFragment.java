package com.example.mobileterm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingFragment extends Fragment {
    TextView email;
    TextView phone;
    TextView authDate;

    TextView emailchange;
    TextView phonechange;
    TextView authDatechange;

    Switch sound;
    Switch viberate;

    FirebaseAuth auth;
    FirebaseUser user;

    @Nullable
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);
        email = (TextView) rootView.findViewById(R.id.setting_info_email_tv);
        phone = (TextView) rootView.findViewById(R.id.setting_info_phone_tv);
        authDate = (TextView) rootView.findViewById(R.id.setting_info_authenticate_tv);
        emailchange = (TextView) rootView.findViewById(R.id.setting_info_email_change_tv);
        phonechange = (TextView) rootView.findViewById(R.id.setting_info_phone_change_tv);
        authDatechange = (TextView) rootView.findViewById(R.id.setting_info_authenticate_change_tv);

        sound = (Switch) rootView.findViewById(R.id.setting_alarm_sound_switch);
        viberate = (Switch) rootView.findViewById(R.id.setting_alarm_ring_switch);

        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //소리, 무음, 진동 제어

        AudioManager audioManager;
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((MainActivity) getActivity()).noti();
                if (compoundButton.isChecked())
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                else audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
        });
        viberate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((MainActivity) getActivity()).noti();
                if (compoundButton.isChecked())
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                else audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        });

        //이메일 변경 설정
        emailchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //휴대폰 변경 설정정
        phonechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return rootView;
    }

}
