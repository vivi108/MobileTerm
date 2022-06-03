package com.example.mobileterm.StudyGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.FindStudyAdapter;
import com.example.mobileterm.StudyGroup.adapter.PostAdapter;
import com.example.mobileterm.StudyGroup.vo.FindStudyVo;
import com.example.mobileterm.StudyGroup.vo.JoinedStudyVo;
import com.example.mobileterm.StudyGroup.vo.PostVo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;


public class StudyGroupActivity extends AppCompatActivity {
    Activity activity;
    Intent intent;
    ImageButton btn_studygroup_back, btn_studygroup_setting, btn_studygroup_write_post;
    TextView tv_studygroup_title;
    String study_name;

    ListView lv_studygroup_posts;
    private ArrayList<PostVo> posts;
    private ArrayList<PostVo> myPosts;
    private PostAdapter adapter;

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
    private String TAG = "StudyGroup";
    private String writer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_studygroup);

        posts = new ArrayList<>();
        myPosts = new ArrayList<>();

        activity = this;
        intent = getIntent();

        // db에서 데이터 받아와서 저장후 리스트로 넘기기
        String[] user = {"jiyeonleeee", "k", "i", "j"};
        String[] upload = {"2022.05.29", "2022.05.20", "2022.05.28"};
        String[] postName = {"Title1", "Title2", "Title3", "Title4"};
        String[] content = {"테스트1", "테스트2", "Test3", "Test4"};
        String tag = "#스터디 #모프 #전공";
        String fileName = "mobileProgramming.txt";
        String commentSize = "5";
        String likeNum = "20";
        String downLoad = "10";
        float rating = (float) 3.8;
        String starRate = "3.8";

        posts.add(new PostVo(user[0], upload[1], postName[1], content[1], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[2], upload[2], postName[2], content[2], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[1], upload[0], postName[0], content[0], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[3], upload[1], postName[3], content[3], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[3], upload[2], postName[3], content[3], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[2], upload[0], postName[1], content[1], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[1], upload[1], postName[2], content[2], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[3], upload[2], postName[0], content[0], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[0], upload[1], postName[1], content[1], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[1], upload[2], postName[2], content[2], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));
        posts.add(new PostVo(user[2], upload[1], postName[3], content[3], tag, fileName, commentSize, likeNum, downLoad, rating, starRate));

        btn_studygroup_back = findViewById(R.id.btn_studygroup_back);
        btn_studygroup_setting = findViewById(R.id.btn_studygroup_setting);
        btn_studygroup_write_post = findViewById(R.id.btn_studygroup_write_post);
        tv_studygroup_title = findViewById(R.id.tv_studygroup_title);
        lv_studygroup_posts = findViewById(R.id.lv_studygroup_posts);

        adapter = new PostAdapter(this, posts);
        lv_studygroup_posts.setAdapter(adapter);

        btn_studygroup_write_post.setFocusable(false);
        study_name = intent.getStringExtra("JoinedStudy");

        //firebase에서 스터디 이름 수신

        //스터디 이름 출력
        tv_studygroup_title.setText(study_name);

        btn_studygroup_back.setOnClickListener(Listener_back);
        btn_studygroup_setting.setOnClickListener(Listener_study_setting);
        btn_studygroup_write_post.setOnClickListener(Listener_write_post);
        lv_studygroup_posts.setOnItemClickListener(Listener_post);
    }

    private View.OnClickListener Listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener Listener_write_post = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(v.getContext(), "Click Write Study Button", Toast.LENGTH_SHORT).show();
            intent = new Intent(activity, WritePostActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener Listener_study_setting = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(activity, StudySettingActivity.class);
            startActivity(intent);
        }
    };

    private AdapterView.OnItemClickListener Listener_post = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            PostVo member = (PostVo) adapter.getItem(i);
            String writer = member.getWriter();
            intent = new Intent(view.getContext(), StudyPostActivity.class);
            intent.putExtra("user", member.getWriter());
            intent.putExtra("upload", member.getUploadDate());
            intent.putExtra("postName", member.getPostName());
            intent.putExtra("content", member.getContent());
            intent.putExtra("tag", member.getTag());
            intent.putExtra("fileName", member.getFileName());
            intent.putExtra("commentSize", member.getCommentSize());
            intent.putExtra("likeNum", member.getLikeNum());
            intent.putExtra("download", member.getDownLoad());
            intent.putExtra("rating", member.getRating());
            intent.putExtra("starRate", member.getStarRate());

            if(writer.equals(myNickName)){
                intent.putExtra("myPost", "true");
            }
            else {
                intent.putExtra("myPost", "false");
            }
            startActivity(intent);
        }
    };
}
