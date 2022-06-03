package com.example.mobileterm.StudyGroup;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.CommentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StudyPostFragment extends Fragment {
    String studyname;
    String pid;
    MainActivity activity;
    FirebaseFirestore db;
    Dialog addComment;
    String myNickname;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    long mnow;
    Date mDate;

    ArrayList<PostComment> arrayList;
    CommentAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_study_post, container, false);
        activity = (MainActivity)getActivity();
        studyname = activity.sendStudyTitle();
        myNickname = activity.sendUserNickname();
        pid = activity.sendPid();
        db = FirebaseFirestore.getInstance();
        addComment = new Dialog(getActivity());
        addComment.setContentView(R.layout.dialog_post_comment);
        arrayList = new ArrayList<PostComment>();
        TextView tv_posted_user_name = rootView.findViewById(R.id.tv_posted_user_name);
        TextView tv_posted_wdate = rootView.findViewById(R.id.tv_post_upload_date);
        TextView tv_post_title = rootView.findViewById(R.id.tv_post_title);
        TextView tv_post_description = rootView.findViewById(R.id.tv_post_description);
        TextView tv_post_tags = rootView.findViewById(R.id.tv_post_tags);
        ImageButton postAddComment = rootView.findViewById(R.id.postAddComment);
        postAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment.show();
                EditText newPostCommentEditText = addComment.findViewById(R.id.newPostCommentEditText);
                ImageButton newPostCommentDone = addComment.findViewById(R.id.newPostCommentDone);
                String curTime = getTime();
                newPostCommentDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PostComment newComment = new PostComment(myNickname, newPostCommentEditText.getText().toString(), curTime);
                        db.collection(studyname).document(pid).collection("PostComments").document(curTime+" "+myNickname).set(newComment).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("스터디 게시판", "댓글 추가함");
                                adapter.addComment(newComment);
                                addComment.dismiss();
                            }
                        });
                    }
                });
            }
        });
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
                    db.collection(studyname).document(pid).collection("PostComments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                for (DocumentSnapshot doc : querySnapshot){
                                    PostComment tempComment = doc.toObject(PostComment.class);
                                    arrayList.add(tempComment);
                                }
                                adapter = new CommentAdapter(rootView.getContext(), arrayList);
                                postCommentView.setAdapter(adapter);
                            }
                        }
                    });
                }
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
