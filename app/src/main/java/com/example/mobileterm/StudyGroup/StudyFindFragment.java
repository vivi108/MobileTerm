package com.example.mobileterm.StudyGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.FindStudyAdapter;
import com.example.mobileterm.StudyGroup.vo.FindStudyVo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StudyFindFragment extends Fragment {
    ImageButton btn_studyfind_back;
    EditText et_search_study;
    Button btn_tag0, btn_tag1, btn_tag2, btn_tag3, btn_tag4, btn_tag5, btn_tag6, btn_tag7;

    ListView lv_study_find;
    private ArrayList<FindStudyVo> studies = new ArrayList<FindStudyVo>();
    private ArrayList<FindStudyVo> showStudies;
    private HashMap<Integer, String> buttonMap;
    private FindStudyAdapter adapter;

    private FirebaseFirestore db;
    FirebaseUser user;
    String uid;
    String myNickName;
    private String studyID;
    private ArrayList<String> members;
    private String[] tags;
    private String leader;
    private String studyCap;
    private String studyName;
    private String TAG = "FindStudyGroup";

    Activity activity;
    Intent intent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_study_find, container, false);
        showStudies = new ArrayList<>();
        buttonMap = new HashMap<Integer, String>();


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
        buttonMap.put(Integer.valueOf(R.id.btn_tag2), btn_tag1.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag3), btn_tag1.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag4), btn_tag1.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag5), btn_tag1.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag6), btn_tag1.getText().toString());
        buttonMap.put(Integer.valueOf(R.id.btn_tag7), btn_tag1.getText().toString());


        lv_study_find.setOnItemClickListener(Listener_join_study);


        db.collection("Study").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot document : querySnapshot){
                        if (document.exists()) {
                            StudyInfo tempStudy = document.toObject(StudyInfo.class);
                            studies.add(tempStudy);
                        }
                    }

                }
            }
        });
        return rootView;
    }

    private View.OnClickListener Listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private View.OnClickListener Listener_get_study = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            tagStudies.clear();
            for (int i = 0; i < studies.size(); i++) {
                String[] getMembers = studies.get(i).getMembers();
                if (!Arrays.asList(getMembers).contains(myNickName)) {
//                    tagStudies.add(studies.get(i));
                }
            }
//            adapter = new FindStudyAdapter(view.getContext(), tagStudies);
            lv_study_find.setAdapter(adapter);
        }
    };

    private Button.OnClickListener Listener_get_tag_study = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
//            tagStudies.clear();

            String tag = "#" + buttonMap.get(v.getId());
            //tag 이용 -> firebase에 데이터 전달 및 수신
            int size = studies.size();
            for (int i = 0; i < size; i++){
                String getTags = studies.get(i).getTags();
                tags = getTags.split(" ");
                String[] members = studies.get(i).getMembers();
                if(!Arrays.asList(members).contains(myNickName)){
                    if(Arrays.asList(tags).contains(tag)){
//                        tagStudies.add(studies.get(i));
                    }
                }
            }
//            adapter = new FindStudyAdapter(v.getContext(), tagStudies);
            lv_study_find.setAdapter(adapter);
        }
    };

    private AdapterView.OnItemClickListener Listener_join_study = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String getID = (String) view.findViewById(R.id.tv_find_study_name).getTag().toString();
            FindStudyVo member = (FindStudyVo) adapter.getItem(i);
            String pw = member.getPw();
            String StudyName = member.getStudyName();
            //Toast.makeText(view.getContext(), "Clicked" + getID, Toast.LENGTH_SHORT).show();

        }
    };
}
