package com.example.mobileterm.StudyGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.PostAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudyGroupFragment extends Fragment {
    ImageButton btn_studygroup_back, btn_studygroup_setting, btn_studygroup_write_post;
    TextView tv_studygroup_title;
    ListView lv_studygroup_posts;
    ArrayList<StudyPostInfo> arrayList;
    private PostAdapter adapter;
    String title;
    MainActivity activity;
    FirebaseFirestore db ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_study_studygroup, container, false);
        activity = (MainActivity) getActivity();
        title = activity.sendStudyTitle();
        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<StudyPostInfo>();

//        btn_studygroup_back = rootView.findViewById(R.id.btn_studygroup_back);
        btn_studygroup_setting = rootView.findViewById(R.id.btn_studygroup_setting);
        btn_studygroup_write_post = rootView.findViewById(R.id.btn_studygroup_write_post);

        tv_studygroup_title = rootView.findViewById(R.id.tv_studygroup_title);
        lv_studygroup_posts = rootView.findViewById(R.id.lv_studygroup_posts);

        tv_studygroup_title.setText(title);

        db.collection(title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot document : querySnapshot) {
                        if (document.exists()) {
                            StudyPostInfo temp = document.toObject(StudyPostInfo.class);
                            arrayList.add(temp);
                        }
                    }
                    adapter = new PostAdapter(rootView.getContext(), arrayList);
                    lv_studygroup_posts.setAdapter(adapter);
                }
            }
        });

        btn_studygroup_write_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onFragmentChanged(400);
            }
        });

        return rootView;
    }
}
