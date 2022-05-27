package com.example.mobileterm;

import static android.content.ContentValues.TAG;

import static java.lang.System.currentTimeMillis;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingFragment extends Fragment {
    TextView email_tv;
    TextView phone_tv;
    TextView authDate;

    TextView regionauth;
    TextView regionchange;
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

        authDatechange = (TextView) rootView.findViewById(R.id.setting_info_authenticate_change_tv);
        regionauth = (TextView) rootView.findViewById(R.id.setting_info_region_change_tv);
        regionchange =(TextView) rootView.findViewById(R.id.setting_info_region_tv) ;

        sound = (Switch) rootView.findViewById(R.id.setting_alarm_sound_switch);
        viberate = (Switch) rootView.findViewById(R.id.setting_alarm_ring_switch);

        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        GetInfo(user);

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


        //이메일 인증
        authDatechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final EditText input = new EditText(requireActivity());

//                FrameLayout container = new FrameLayout(getContext());
//                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
//                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
//                input.setLayoutParams(params);
//                container.addView(input);
                AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity(), R.style.MyAlertDialogStyle);
                alert.setTitle("이메일로 인증하기");
                alert.setMessage("이메일을 인증받으시겠습니까?");
//                alert.setView(container);
                alert.setPositiveButton("인증메일 받기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        String value = input.getText().toString();
//                        value.toString();
                        //이메일에 인증메일 보내기
                        //인증이 되었으면 verified
                        auth =FirebaseAuth.getInstance();
                        user = auth.getCurrentUser();
                        auth.useAppLanguage(); //해당기기의 언어 설정
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");//해당 이메일에 확인메일을 보냄
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("User").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if (documentSnapshot.exists()){
//                                                    Boolean curLike = (Boolean) documentSnapshot.getData().get("verified");
                                                    Boolean updateLike = true;
                                                    db.collection("User").document(user.getUid()).update("verified", updateLike).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Log.d(TAG,"Verified: true updated");
                                                            Toast.makeText(getActivity().getApplicationContext(), "인증되었습니다.",Toast.LENGTH_LONG).show();
                                                            regionchange.setText("true");
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });

                                } else {                                             //메일 보내기 실패
                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                    Toast.makeText(requireActivity(),
                                            "Failed to send verification email.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //canceled
                    }
                });
                alert.show();
            }

        });

        //지역인증
        regionauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(requireActivity());
//
                FrameLayout container = new FrameLayout(getContext());
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                container.addView(input);
                AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity(), R.style.MyAlertDialogStyle);
                alert.setTitle("지역인증 받기");
                alert.setMessage("현재 위치를 입력해주세요");
                alert.setView(container);
                alert.setPositiveButton("인증하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = input.getText().toString();
                        value.toString();

                        //edittext에 있는 주소 받아와서 settext & 서버에 저장<-완성

                        auth = FirebaseAuth.getInstance();
                        user = auth.getCurrentUser();
                        FirebaseFirestore fb = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = fb.collection("Users").document(user.getUid());
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        if (document.exists()) {
                                            document.getData().put("address", value);
                                            regionchange.setText(value);
                                            //authDate

                                        }
                                    }
                                }
                            }
                        });
                    }


                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //canceled
                    }
                });
                alert.show();
            }

        });
        return rootView;
    }
    public void GetInfo(FirebaseUser firebaseUser) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fb.collection("Users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            String a = (String) document.getData().get("email");
                            String b= (String) document.getData().get("phone");
                            String c = (String) document.getData().get("address");
                            String d = (String) document.getData().get("verified");
                            email_tv.setText(a);
                            phone_tv.setText(b);
                            regionchange.setText(c);
                            authDate.setText(d);
                            //authDate

                        }
                    }
                }
            }
        });
    }



}
