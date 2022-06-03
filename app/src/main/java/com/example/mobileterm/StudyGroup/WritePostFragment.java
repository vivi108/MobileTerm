package com.example.mobileterm.StudyGroup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WritePostFragment extends Fragment {

    ImageButton btn_studypost_back, btn_finish_writing, btn_study_fileupload;
    TextView tv_study_file_name;
    EditText et_study_post_title, et_study_post_tag, et_study_post_body;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    MainActivity activity;
    FirebaseFirestore db;
    String myNickname;
    String studyName;
    long mnow;
    Date mDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_study_write_post, container, false);
        activity = (MainActivity) getActivity();
        db = FirebaseFirestore.getInstance();
        myNickname = activity.sendUserNickname();
        studyName = activity.sendStudyTitle();
//        btn_studypost_back = rootView.findViewById(R.id.btn_studypost_back);
        btn_finish_writing = rootView.findViewById(R.id.btn_finish_writing);
        btn_study_fileupload = rootView.findViewById(R.id.btn_study_fileupload);
        tv_study_file_name = rootView.findViewById(R.id.tv_study_file_name);
        et_study_post_title = rootView.findViewById(R.id.et_study_post_title);
        et_study_post_tag = rootView.findViewById(R.id.et_study_post_tag);
        et_study_post_body = rootView.findViewById(R.id.et_study_post_body);
        String wTime = getTime();
        btn_finish_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String did = wTime+" "+et_study_post_title.getText().toString();
                StudyPostInfo newPost = new StudyPostInfo(et_study_post_title.getText().toString(), et_study_post_body.getText().toString(), myNickname, did, wTime, (long) 0, (long) 0, (float)0.0, et_study_post_tag.getText().toString());
                db.collection(studyName).document(did).set(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("WritePostFrag","sucess");
                        activity.onFragmentChanged(studyName,101);
                    }
                });
            }
        });
        return rootView;
    }

    private String getTime() {
        mnow = System.currentTimeMillis();
        mDate = new Date(mnow);
        return mFormat.format(mDate);
    }
}
