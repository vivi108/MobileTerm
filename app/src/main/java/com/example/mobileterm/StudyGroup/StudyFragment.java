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
import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.JoinedStudyAdapter;
import com.example.mobileterm.StudyGroup.vo.JoinedStudyVo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudyFragment extends Fragment implements View.OnClickListener {
    private ViewGroup rootView;
    private Button btn_find_study, btn_make_study_make;
    private Intent intent;

    ListView lv_study_joined;
    ArrayList<JoinedStudyVo> studies;
    ArrayList<JoinedStudyVo> joinedStudies;
    private JoinedStudyAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid;
    String myNickName;
    private String TAG = "MyStudyGroup";
    String studyName;
    String maxNumPeople;
    String memberList;
    String[] members;
    String tags;
    String description;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_study_main, container, false);
        mainActivity = (MainActivity)getActivity();
        studies = new ArrayList<>();
        joinedStudies = new ArrayList<>();
        myNickName = mainActivity.sendUserNickname();




        Log.d(TAG, "myNickName : " + myNickName); // 닉네임 로그 출력시 에러 닉네임에 저장은 되어 있으나 출력은 안되는 것으로 확인됨

        CollectionReference collectionReference = db.collection("Study").document("Study")
                .collection("StudyName");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //document.getData() or document.getId() 등등 여러 방법으로
                        //데이터를 가져올 수 있다.

                        studyName = (String) document.getData().get("studyName");
                        maxNumPeople = (String) document.getData().get("maxNumPeople");
                        memberList = (String) document.getData().get("memberList");
                        members = (String[]) memberList.split("/");
                        tags = (String) document.getData().get("tags");
                        description = (String) document.getData().get("description");
                        Log.d(TAG, studyName + " " + maxNumPeople + " " + members[0] + " " + tags);

                        studies.add(new JoinedStudyVo(studyName, maxNumPeople, members, tags, description));
                    }
                    Log.d(TAG, "study size : " + studies.size());
                    for (int i = 0; i < studies.size(); i++){
                        String[] getMembers = studies.get(i).getMembers();
                        if(Arrays.asList(getMembers).contains(myNickName)){
                            joinedStudies.add(studies.get(i));
                        }
                    }
                    adapter = new JoinedStudyAdapter(getContext(), joinedStudies);
                    lv_study_joined.setAdapter(adapter);
                }
            }
        });

        lv_study_joined = (ListView) rootView.findViewById(R.id.lv_study_joined);

        btn_find_study = rootView.findViewById(R.id.btn_find_study);
        btn_make_study_make = rootView.findViewById(R.id.btn_make_study_make);

        btn_find_study.setOnClickListener (this);
        btn_make_study_make.setOnClickListener(this);

        lv_study_joined.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String studyName = (String) view.findViewById(R.id.tv_joined_study_name).getTag().toString();
                Bundle bundle = new Bundle();
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
                mainActivity.onFragmentChanged(301);
                break;
            case R.id.btn_make_study_make:
//                intent = new Intent(getActivity(), StudyMakeActivity.class);
//                startActivity(intent); // 생성 버튼 클릭 -> 자동 반영을 위한 코드 작성 필요
                mainActivity.onFragmentChanged(301);
                break;
        }

    }

    public String sendStudyName(){
        return studyName;
    }

}
