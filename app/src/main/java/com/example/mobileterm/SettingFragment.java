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

        //??????, ??????, ?????? ??????

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


        //????????? ??????
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
                alert.setTitle("???????????? ????????????");
                alert.setMessage("???????????? ????????????????????????????");
//                alert.setView(container);
                alert.setPositiveButton("???????????? ??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        String value = input.getText().toString();
//                        value.toString();
                        //???????????? ???????????? ?????????
                        //????????? ???????????? verified
                        auth =FirebaseAuth.getInstance();
                        user = auth.getCurrentUser();
                        auth.useAppLanguage(); //??????????????? ?????? ??????
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");//?????? ???????????? ??????????????? ??????
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
                                                            Toast.makeText(getActivity().getApplicationContext(), "?????????????????????.",Toast.LENGTH_LONG).show();
                                                            regionchange.setText("true");
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });

                                } else {                                             //?????? ????????? ??????
                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                    Toast.makeText(requireActivity(),
                                            "Failed to send verification email.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                alert.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //canceled
                    }
                });
                alert.show();
            }

        });

        //????????????
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
                alert.setTitle("???????????? ??????");
                alert.setMessage("?????? ????????? ??????????????????");
                alert.setView(container);
                alert.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = input.getText().toString();
                        value.toString();

                        //edittext??? ?????? ?????? ???????????? settext & ????????? ??????<-??????

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
                alert.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
