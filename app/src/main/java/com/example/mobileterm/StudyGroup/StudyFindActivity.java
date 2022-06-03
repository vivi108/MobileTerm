package com.example.mobileterm.StudyGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.FindStudyAdapter;
import com.example.mobileterm.StudyGroup.popup.closedPopup;
import com.example.mobileterm.StudyGroup.popup.openedPopup;
import com.example.mobileterm.StudyGroup.vo.FindStudyVo;
import com.example.mobileterm.StudyGroup.popup.openedPopup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class StudyFindActivity extends AppCompatActivity {
    private openedPopup open;
    private closedPopup close;

    FrameLayout popup_container;
    ImageButton btn_studyfind_back;
    EditText et_search_study;
    Button btn_tag0, btn_tag1, btn_tag2, btn_tag3, btn_tag4, btn_tag5, btn_tag6, btn_tag7;

    ListView lv_study_find;
    private ArrayList<FindStudyVo> studies;
    private ArrayList<FindStudyVo> findStudies;
    private ArrayList<FindStudyVo> tagStudies;
    private FindStudyAdapter adapter;

    private FirebaseFirestore db;
    FirebaseUser user;
    String uid;
    String myNickName = "jiyeonleeee";
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_find);
        studies = new ArrayList<>();
        findStudies = new ArrayList<>();
        tagStudies = new ArrayList<>();
        open = new openedPopup();
        close = new closedPopup();

        activity = this;
        intent = getIntent();
        db = FirebaseFirestore.getInstance();

        String joined = intent.getStringExtra("Joined");
        // db에서 데이터 받아와서 저장후 리스트로 넘기기
        //String studyName, String studyCapacity, String[] members, String tags, String description, String isOpened, String isLiked
        String[] member1 = {"jiyeonleeee", "abc", "j", "i", "k"};
        String[] member2 = {"abc", "j", "i", "k"};
        String[] tag1 = {"#어학", "#토익", "#아침", "#서울"};
        String description1 = "서울에서 진행하는 토익 아침 스터디입니다.";
//        String mergeTag1 = String.join(" ", tag1);
        String[] tag2 = {"#전공", "#모프", "#오후", "#성남"};
        String description2 = "성남에서 진행하는 모바일프로그래밍 오후 스터디입니다.";
//        String mergeTag2 = String.join(" ", tag2);
//        studies.add(new FindStudyVo("스터디1", "10", member1, mergeTag2, description2, "open", "true", null));
//        studies.add(new FindStudyVo("스터디2", "5", member2, mergeTag1, description1, "open", "true", null));
//        studies.add(new FindStudyVo("스터디3", "8", member2, mergeTag2, description2, "close", "true", "1234"));
//        studies.add(new FindStudyVo("스터디4", "12", member2, mergeTag1, description1, "close", "false", "5678"));
//        studies.add(new FindStudyVo("스터디5", "15", member2, mergeTag2, description2, "open", "false", null));
        //Log.d(this.getClass().getName(), "데이터 삽입 완료");
        for (int i = 0; i < studies.size(); i++){
            String[] getMembers = studies.get(i).getMembers();
            if(!Arrays.asList(getMembers).contains(myNickName)){
                findStudies.add(studies.get(i));
            }
        }

        popup_container = findViewById(R.id.popup_container);
        btn_studyfind_back = findViewById(R.id.btn_studyfind_back);
        et_search_study = findViewById(R.id.et_search_study);
        btn_tag0 = findViewById(R.id.btn_tag0);
        btn_tag1 = findViewById(R.id.btn_tag1);
        btn_tag2 = findViewById(R.id.btn_tag2);
        btn_tag3 = findViewById(R.id.btn_tag3);
        btn_tag4 = findViewById(R.id.btn_tag4);
        btn_tag5 = findViewById(R.id.btn_tag5);
        btn_tag6 = findViewById(R.id.btn_tag6);
        btn_tag7 = findViewById(R.id.btn_tag7);
        lv_study_find = findViewById(R.id.lv_study_find);

//        adapter = new FindStudyAdapter(this, findStudies);
        lv_study_find.setAdapter(adapter);

        btn_studyfind_back.setOnClickListener(Listener_back);
        btn_tag0.setOnClickListener(Listener_get_study);
        btn_tag1.setOnClickListener(Listener_get_tag_study);
        btn_tag2.setOnClickListener(Listener_get_tag_study);
        btn_tag3.setOnClickListener(Listener_get_tag_study);
        btn_tag4.setOnClickListener(Listener_get_tag_study);
        btn_tag5.setOnClickListener(Listener_get_tag_study);
        btn_tag6.setOnClickListener(Listener_get_tag_study);
        btn_tag7.setOnClickListener(Listener_get_tag_study);
        lv_study_find.setOnItemClickListener(Listener_join_study);
    }

    private View.OnClickListener Listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener Listener_get_study = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tagStudies.clear();
            for (int i = 0; i < studies.size(); i++) {
                String[] getMembers = studies.get(i).getMembers();
                if (!Arrays.asList(getMembers).contains(myNickName)) {
                    tagStudies.add(studies.get(i));
                }
            }
//            adapter = new FindStudyAdapter(view.getContext(), tagStudies);
            lv_study_find.setAdapter(adapter);
        }
    };

    private Button.OnClickListener Listener_get_tag_study = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            tagStudies.clear();
            Button btn = findViewById(v.getId());
            String tag = "#" + btn.getText().toString();
            //tag 이용 -> firebase에 데이터 전달 및 수신
            int size = studies.size();
            for (int i = 0; i < size; i++){
                String getTags = studies.get(i).getTags();
                tags = getTags.split(" ");
                String[] members = studies.get(i).getMembers();
                if(!Arrays.asList(members).contains(myNickName)){
                    if(Arrays.asList(tags).contains(tag)){
                        tagStudies.add(studies.get(i));
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
            if(getID.contains("open")){
                getSupportFragmentManager().beginTransaction().replace(R.id.popup_container, open).addToBackStack(null).commit();
                Bundle bundle = new Bundle();
                bundle.putString("StudyName", StudyName);
                open.setArguments(bundle);
            }
            else if(getID.contains("close")){
                getSupportFragmentManager().beginTransaction().replace(R.id.popup_container, close).addToBackStack(null).commit();
                Bundle bundle = new Bundle();
                bundle.putString("PW", pw);
                bundle.putString("StudyName", StudyName);
                close.setArguments(bundle);
            }
        }
    };

    // 가입시 /로 nickname 구분
}
