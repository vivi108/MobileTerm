package com.example.mobileterm.StudyGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WritePostActivity extends AppCompatActivity {
    Activity activity;
    Intent intent = null;
    ImageButton btn_studypost_back, btn_finish_writing, btn_study_fileupload;
    TextView tv_study_file_name;
    EditText et_study_post_title, et_study_post_tag, et_study_post_body;
    FirebaseFirestore db;
    String writer;
    String studyName;
    String TAG = "WritePost";
    MainActivity mainActivity = new MainActivity();
    StudyMakeActivity studyMakeActivity = new StudyMakeActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_write_post);

        writer = mainActivity.sendUserNickname();
        studyName = studyMakeActivity.sendStudyID();
//        btn_studypost_back = findViewById(R.id.btn_studypost_back);
        btn_finish_writing = findViewById(R.id.btn_finish_writing);
//        btn_study_fileupload = findViewById(R.id.btn_study_fileupload);
//        tv_study_file_name = findViewById(R.id.tv_study_file_name);
        et_study_post_title = findViewById(R.id.et_study_post_title);
        et_study_post_tag = findViewById(R.id.et_study_post_tag);
        et_study_post_body = findViewById(R.id.et_study_post_body);

        btn_studypost_back.setOnClickListener(Listener_back);
        btn_finish_writing.setOnClickListener(Listener_finish_writing);
        btn_study_fileupload.setOnClickListener(Listener_file_upload);
    }

    private View.OnClickListener Listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener Listener_finish_writing = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //작성 내용 및 파일 파이어베이스에 업로드 후 그룹으로 복귀
            String fileName = tv_study_file_name.getText().toString();
            String postTitle = et_study_post_title.getText().toString();
            String postTag = et_study_post_tag.getText().toString();
            String postBody = et_study_post_body.getText().toString();
            String ID = writer + postTitle;
            String StudyID = studyMakeActivity.sendStudyID();

            Map<String, Object> post = new HashMap<>();
            post.put("fileName", fileName);
            post.put("postTitle", postTitle);
            post.put("postTag", postTag);
            post.put("postBody", postBody);

            db.collection("Study").document(StudyID)
                    .collection("Posts").document(ID)
                    .set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "Write Post Success!");
                    Toast.makeText(activity, "Write post success!", Toast.LENGTH_SHORT);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });

            // 파일 업로드 추가

            finish();
        }
    };

    private View.OnClickListener Listener_file_upload = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 사용자 핸드폰에 저장된 파일을 불러와서 업로드
        }
    };

}
