package com.example.mobileterm.StudyGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileterm.R;

public class WritePostActivity extends AppCompatActivity {
    Activity activity;
    Intent intent = null;
    ImageButton btn_studypost_back, btn_finish_writing, btn_study_fileupload;
    TextView tv_study_file_name;
    EditText et_study_post_title, et_study_post_tag, et_study_post_body;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_write_post);

        btn_studypost_back = findViewById(R.id.btn_studypost_back);
        btn_finish_writing = findViewById(R.id.btn_finish_writing);
        btn_study_fileupload = findViewById(R.id.btn_study_fileupload);
        tv_study_file_name = findViewById(R.id.tv_study_file_name);
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
            intent = new Intent(activity, StudyGroupActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener Listener_file_upload = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 사용자 핸드폰에 저장된 파일을 불러와서 업로드
        }
    };

}
