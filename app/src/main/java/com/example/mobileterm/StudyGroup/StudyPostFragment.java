package com.example.mobileterm.StudyGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class StudyPostFragment extends Fragment {
    String studyname;
    String pid;
    MainActivity activity;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_study_post, container, false);
        activity = (MainActivity)getActivity();
        studyname = activity.sendStudyTitle();
        pid = activity.sendPid();
        db = FirebaseFirestore.getInstance();
        TextView tv_posted_user_name = rootView.findViewById(R.id.tv_posted_user_name);
        TextView tv_posted_wdate = rootView.findViewById(R.id.tv_post_upload_date);
        TextView tv_post_title = rootView.findViewById(R.id.tv_post_title);
        TextView tv_post_description = rootView.findViewById(R.id.tv_post_description);
        TextView tv_post_tags = rootView.findViewById(R.id.tv_post_tags);

        ListView postCommentView = rootView.findViewById(R.id.postCommentView);

        db.collection(studyname).document(pid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    tv_posted_user_name.setText((String) document.getData().get("name"));
                    tv_posted_wdate.setText((String) document.getData().get("writtenTime"));
                    tv_post_title.setText((String) document.getData().get("title"));
                    tv_post_description.setText((String) document.getData().get("content"));
                    tv_post_tags.setText((String) document.getData().get("tags"));
                }
            }
        });
        return rootView;
    }
}
