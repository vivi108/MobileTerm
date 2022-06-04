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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.CommentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    ArrayList<PostComment> arrayList = new ArrayList<PostComment>();
    ArrayList<PostComment> itemList;
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
        itemList = new ArrayList<PostComment>();
        TextView tv_posted_user_name = rootView.findViewById(R.id.tv_posted_user_name);
        TextView tv_posted_wdate = rootView.findViewById(R.id.tv_post_upload_date);
        TextView tv_post_title = rootView.findViewById(R.id.tv_post_title);
        TextView tv_post_description = rootView.findViewById(R.id.tv_post_description);
        TextView tv_post_tags = rootView.findViewById(R.id.tv_post_tags);
        TextView tv_post_comments_count = rootView.findViewById(R.id.tv_post_comments_count);
        ImageButton postAddComment = rootView.findViewById(R.id.postAddComment);
        ImageButton likePostButton = rootView.findViewById(R.id.likePostButton);
        TextView postLikeCount = rootView.findViewById(R.id.postLikeCount);

        likePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection(studyname).document(pid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            ArrayList<String> tempLiked = (ArrayList<String>) document.getData().get("likedUser");
                            if (tempLiked.contains(myNickname)) {
                                Toast.makeText(getActivity(), "이미 좋아요 누른 게시물입니다.", Toast.LENGTH_LONG).show();
                            }else{
                                postLikeCount.setText(Integer.toString(Integer.parseInt(postLikeCount.getText().toString())+1));
                                tempLiked.add(myNickname);
                                db.collection(studyname).document(pid).update("likedUser", tempLiked).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("좋아요 성공","짜스");
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
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
                                tv_post_comments_count.setText(Integer.toString(Integer.parseInt(tv_post_comments_count.getText().toString())+1));
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
                    ArrayList<String> temp = (ArrayList<String>) document.getData().get("likedUser");
                    postLikeCount.setText(Integer.toString(temp.size()));
                    db.collection(studyname).document(pid).collection("PostComments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                for (DocumentSnapshot doc : querySnapshot){
                                    PostComment tempComment = doc.toObject(PostComment.class);
                                    itemList.add(tempComment);
                                }
                                if (!arrayList.equals(itemList)){
                                    arrayList = itemList;
                                }
                                tv_post_comments_count.setText(Integer.toString(itemList.size()));
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
