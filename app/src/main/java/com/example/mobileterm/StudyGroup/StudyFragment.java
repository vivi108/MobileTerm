package com.example.mobileterm.StudyGroup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.mobileterm.BulletinBoard.BoardInfo;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.JoinedStudyAdapter;
import com.example.mobileterm.StudyGroup.vo.JoinedStudyVo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class StudyFragment extends Fragment implements View.OnClickListener {
    private ViewGroup rootView;
    private Button btn_find_study, btn_make_study_make;
    private Intent intent;

    ListView lv_study_joined;
    ArrayList<JoinedStudyVo> studies;
    ArrayList<JoinedStudyVo> joinedStudies;
    private JoinedStudyAdapter adapter;

    private FirebaseFirestore db;
    FirebaseUser user;
    String uid;
    String myNickName = "jiyeonleeee";
    private String studyID;
    private ArrayList<String> members;
    private ArrayList<String> tags;
    private String leader;
    private String studyCap;
    private String studyName;
    private String TAG = "MyStudyGroup";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_study_main, container, false);
        studies = new ArrayList<>();
        joinedStudies = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            uid = user.getUid();
        }
        getName(user);

        // db에서 데이터 받아와서 저장후 리스트로 넘기기
        String[] member1 = {"jiyeonleeee", "abc", "j", "i", "k"};
        String[] member2 = {"abc", "j", "i", "k"};
        String[] tag1 = {"#어학", "#토익", "#아침", "#서울"};
        String mergeTag1 = String.join(" ", tag1);
        String[] tag2 = {"#전공", "#모프", "#오후", "#경기"};
        String mergeTag2 = String.join(" ", tag2);
        studies.add(new JoinedStudyVo("스터디1", "10", member1, mergeTag2));
        studies.add(new JoinedStudyVo("스터디2", "5", member2, mergeTag1));
        studies.add(new JoinedStudyVo("스터디3", "8", member2, mergeTag2));
        studies.add(new JoinedStudyVo("스터디4", "12", member1, mergeTag1));
        studies.add(new JoinedStudyVo("스터디5", "15", member2, mergeTag2));

        for (int i = 0; i < studies.size(); i++){
            String[] getMembers = studies.get(i).getMembers();
            if(Arrays.asList(getMembers).contains(myNickName)){
                joinedStudies.add(studies.get(i));
            }
        }

        lv_study_joined = (ListView) rootView.findViewById(R.id.lv_study_joined);

        btn_find_study = rootView.findViewById(R.id.btn_find_study);
        btn_make_study_make = rootView.findViewById(R.id.btn_make_study_make);

        btn_find_study.setOnClickListener (this);
        btn_make_study_make.setOnClickListener(this);

        adapter = new JoinedStudyAdapter(getContext(), joinedStudies);
        lv_study_joined.setAdapter(adapter);

        lv_study_joined.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String studyName = (String) view.findViewById(R.id.tv_joined_study_name).getTag().toString();
                intent = new Intent(getActivity(), StudyGroupActivity.class);
                intent.putExtra("JoinedStudy", studyName);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_find_study:
                intent = new Intent(getActivity(), StudyFindActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_make_study_make:
                intent = new Intent(getActivity(), StudyMakeActivity.class);
                startActivity(intent);
                break;
        }

    }

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

}
