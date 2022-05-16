package com.example.mobileterm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingFragment extends Fragment {
    TextView email_tv;
    TextView phone_tv;
    TextView authDate;

    TextView emailchange;
    TextView phonechange;
    TextView authDatechange;

    Switch sound;
    Switch viberate;

    FirebaseAuth auth;
    FirebaseUser user;

    // Id of the provider (ex: google.com)
    String providerId ;
    // UID specific to the provider
    String uid ;
    // Name, email address, and profile photo Url
    String name ;
    String email;
    Uri photoUrl ;


    @Nullable
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);
        email_tv = (TextView) rootView.findViewById(R.id.setting_info_email_tv);
        phone_tv = (TextView) rootView.findViewById(R.id.setting_info_phone_tv);
        authDate = (TextView) rootView.findViewById(R.id.setting_info_authenticate_tv);
        emailchange = (TextView) rootView.findViewById(R.id.setting_info_email_change_tv);
        phonechange = (TextView) rootView.findViewById(R.id.setting_info_phone_change_tv);
        authDatechange = (TextView) rootView.findViewById(R.id.setting_info_authenticate_change_tv);

        sound = (Switch) rootView.findViewById(R.id.setting_alarm_sound_switch);
        viberate = (Switch) rootView.findViewById(R.id.setting_alarm_ring_switch);

        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            email_tv.setText(email);
            photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();
        }

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
//                requireActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_frame_layout, new EmailDialog())
//                        .commit();
                openEmailDialog();
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
//    @Override
//    public void onFolderImageClicked(int position, FolderDTO item) {
//        FolderDialogFragment dialog = new FolderDialogFragment();
//        dialog.show(getActivity().getSupportFragmentManager(),"FolderDialogFragment");
//    }
    private void openEmailDialog() {

        EmailDialog emaildialog = new EmailDialog();
        emaildialog.setFragmentInterfacer(new EmailDialog.MyFragmentInterfacer(){
            @Override
            public void onButtonClick(String input){
                email_tv.setText(input);
            }
        });

//        myDialogFragment.setTargetFragment(this, 0);
//        myDialogFragment.show(getFragmentManager(), "Search Filter");

    }
}
