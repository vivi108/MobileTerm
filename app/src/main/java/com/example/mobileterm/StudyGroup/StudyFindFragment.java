package com.example.mobileterm.StudyGroup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.FindStudyAdapter;
import com.example.mobileterm.StudyGroup.vo.FindStudyVo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StudyFindFragment extends Fragment {
    ImageButton btn_studyfind_back, studySearchButton;
    EditText et_search_study;
    Button btn_tag0, btn_tag1, btn_tag2, btn_tag3, btn_tag4, btn_tag5, btn_tag6, btn_tag7;

    ListView lv_study_find;
    private ArrayList<StudyInfo> studies = new ArrayList<StudyInfo>();
    private ArrayList<StudyInfo> showStudies;
    private ArrayList<StudyInfo> notSameDis;
    private ArrayList<String> likedStudies;
    private HashMap<Integer, String> buttonMap;
    private FindStudyAdapter adapter;

    private FirebaseFirestore db;
    FirebaseUser user;
    String uid;
    private String studyID;
    private ArrayList<String> members;
    private String[] tags;
    private String leader;
    private String studyCap;
    private String studyName;
    private String TAG = "FindStudyGroup";

    Dialog joinStudy;
    MainActivity activity;
    FirebaseAuth mAuth;
    String myNickName;
    String uAddress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_study_find, container, false);
        activity = (MainActivity) getActivity();
        joinStudy = new Dialog(activity);
        showStudies = new ArrayList<StudyInfo>();
        likedStudies = new ArrayList<String>();
        notSameDis = new ArrayList<StudyInfo>();
        buttonMap = new HashMap<Integer, String>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        myNickName = activity.sendUserNickname();
        uAddress = activity.sendUserAddress();
        studySearchButton = rootView.findViewById(R.id.studySearchButton);
        btn_studyfind_back = rootView.findViewById(R.id.btn_studyfind_back);
        et_search_study = rootView.findViewById(R.id.et_search_study);
        btn_tag0 = rootView.findViewById(R.id.btn_tag0);
        btn_tag1 = rootView.findViewById(R.id.btn_tag1);
        btn_tag2 = rootView.findViewById(R.id.btn_tag2);
        btn_tag3 = rootView.findViewById(R.id.btn_tag3);
        btn_tag4 = rootView.findViewById(R.id.btn_tag4);
        btn_tag5 = rootView.findViewById(R.id.btn_tag5);
        btn_tag6 = rootView.findViewById(R.id.btn_tag6);
        btn_tag7 =rootView. findViewById(R.id.btn_tag7);
        lv_study_find = rootView.findViewById(R.id.lv_study_find);

        btn_studyfind_back.setOnClickListener(Listener_back);
        btn_tag0.setOnClickListener(Listener_get_study);
        btn_tag1.setOnClickListener(Listener_get_tag_study);
        btn_tag2.setOnClickListener(Listener_get_tag_study);
        btn_tag3.setOnClickListener(Listener_get_tag_study);
        btn_tag4.setOnClickListener(Listener_get_tag_study);
        btn_tag5.setOnClickListener(Listener_get_tag_study);
        btn_tag6.setOnClickListener(Listener_get_tag_study);
        btn_tag7.setOnClickListener(Listener_get_tag_study);

        buttonMap.put(Integer.valueOf(R.id.btn_tag1), btn_tag1.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag2), btn_tag2.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag3), btn_tag3.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag4), btn_tag4.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag5), btn_tag5.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag6), btn_tag6.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag7), btn_tag7.getText().toString());


        lv_study_find.setOnItemClickListener(Listener_join_study);
        studySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.filter((String) et_search_study.getText().toString(), 1);
            }
        });


        db.collection("Study").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot document : querySnapshot){
                        if (document.exists() ) {
                            Log.d(TAG,"스터디 읽기"+((String) document.getData().get("studyName")));
                            StudyInfo tempStudy = document.toObject(StudyInfo.class);
                            if (tempStudy.getAddress().equals(uAddress)){
                                showStudies.add(tempStudy);
                            }else{
                                notSameDis.add(tempStudy);
                            }

                        }
                    }
                    showStudies.addAll(notSameDis);
                    db.collection("Users").document(user.getUid()).collection("likedStudy").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot1 = task.getResult();
                                for (DocumentSnapshot document1 : querySnapshot1){
                                    likedStudies.add((String) document1.getData().get("name"));
                                }
                                if (!studies.equals(showStudies)){
                                    studies = showStudies;
                                }
                                adapter = new FindStudyAdapter(rootView.getContext(), studies, likedStudies);
                                lv_study_find.setAdapter(adapter);
                            }
                        }
                    });
                }
            }
        });
        return rootView;
    }

    private View.OnClickListener Listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.onFragmentChanged(300);
        }
    };

    private View.OnClickListener Listener_get_study = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 전체 스터디 보여주깅
            adapter.renew();
        }
    };

    private Button.OnClickListener Listener_get_tag_study = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
//            tagStudies.clear();
            //해당 태그 스터디 보여주기
            adapter.filter(buttonMap.get(v.getId()));
        }
    };

    private AdapterView.OnItemClickListener Listener_join_study = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String getID = (String) view.findViewById(R.id.tv_find_study_name).getTag().toString();
            StudyInfo member = (StudyInfo) adapter.getItem(i);
            if (member.isOpened()){
                joinStudy.setContentView(R.layout.dialog_join_open_study);
                joinStudy.show();
                Button joinButton = joinStudy.findViewById(R.id.openJoinButton);
                Button cancelButton = joinStudy.findViewById(R.id.openCancelButton);
                ArrayList<String> newMem = member.getMemberList();
                for (String itr : newMem){
                    Log.d(TAG,itr);
                }
                joinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ((member.getMaxNumPeople() > newMem.size()) && !newMem.contains(myNickName)){
                            newMem.add(myNickName);
                            db.collection("Study").document(getID).update("memberList", newMem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG,"스터디조인성공함");
                                    joinStudy.dismiss();
                                }
                            });
                        }else{
                            Toast.makeText(getContext(), "가득찬 스터디입니다.",Toast.LENGTH_LONG);
                        }

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        joinStudy.dismiss();
                    }
                });

            }else{
                joinStudy.setContentView(R.layout.dialog_join_closed_study);
                joinStudy.show();
                Button joinButton = joinStudy.findViewById(R.id.closeJoinButton);
                Button cancelButton = joinStudy.findViewById(R.id.closeCancelButton);
                EditText pwd = joinStudy.findViewById(R.id.closedPassword);

                ArrayList<String> newMem = member.getMemberList();

                joinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ((member.getMaxNumPeople() > newMem.size()) && !newMem.contains(myNickName) && member.getPassword().equals(pwd.getText().toString())){
                            newMem.add(myNickName);
                            db.collection("Study").document(getID).update("memberList", newMem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG,"스터디조인성공함");
                                    joinStudy.dismiss();
                                }
                            });
                        }else{
                            Toast.makeText(getContext(), "가득찬 스터디입니다.",Toast.LENGTH_LONG);
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        joinStudy.dismiss();
                    }
                });
            }


        }
    };
}
