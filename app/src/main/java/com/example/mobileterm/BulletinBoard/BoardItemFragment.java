package com.example.mobileterm.BulletinBoard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Comment;

import java.lang.ref.Reference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BoardItemFragment extends Fragment {
    private String did;
    private BoardInfo selectedBoardItem;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser curUser;
    ListView commentListView;
    CommentListViewAdapter commentListViewAdapter;
    ArrayList<CommentInfo> arrayList = new ArrayList<CommentInfo>();
    String TAG = "BoardItemFragment";

    String userName;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    long mnow;
    Date mDate;
    String commentId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_item, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        selectedBoardItem = mainActivity.sendBoardItem();
        did = mainActivity.sendDid();
        commentListView = rootView.findViewById(R.id.commentView);
        db = FirebaseFirestore.getInstance();




        TextView nameTextViewBoardItem = rootView.findViewById(R.id.nameTextViewBoardItem);
        TextView contentTextViewBoardItem = rootView.findViewById(R.id.contentTextViewBoardItem);
        TextView titleTextViewBoardItem = rootView.findViewById(R.id.titleTextViewBoardItem);
        TextView tagTextViewBoardItem = rootView.findViewById(R.id.tagTextViewBoardItem);


        nameTextViewBoardItem.setText(selectedBoardItem.getName());
        contentTextViewBoardItem.setText(selectedBoardItem.getContent());
        titleTextViewBoardItem.setText(selectedBoardItem.getTitle());
        tagTextViewBoardItem.setText("");

        ArrayList<CommentInfo> newArrayList = new ArrayList<CommentInfo>();
        CollectionReference docRef = db.document("BulletinBoard/"+did).collection("Comments");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            String name = (String) document.getData().get("name");
                            String content = (String) document.getData().get("content");

                            CommentInfo data = new CommentInfo(content, name);
                            newArrayList.add(data);
                        }
                    }
                    if (!newArrayList.equals(arrayList)) {
                        arrayList = newArrayList;
                    }

                    commentListViewAdapter = new CommentListViewAdapter(rootView.getContext(), arrayList);
                    commentListView.setAdapter(commentListViewAdapter);

                }
            }
        });

        docRef = db.document("BulletinBoard/"+did).collection("BoardTags");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            String tagName = (String) document.getData().get("name");
                            String temp = tagTextViewBoardItem.getText().toString();
                            tagTextViewBoardItem.setText(temp+"#"+tagName+" ");

                        }
                    }
                }
            }
        });

        EditText commentEditText = rootView.findViewById(R.id.commentEditText);
        ImageButton addCommentButtonBoardItem = rootView.findViewById(R.id.addCommentButtonBoardItem);
        addCommentButtonBoardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                curUser = mAuth.getCurrentUser();
                DocumentReference docref = db.collection("Users").document(curUser.getUid());
                Log.e(TAG, "curUser : "+curUser.getUid());
                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                userName = (String) documentSnapshot.getData().get("nickname");
                            }else{
                                userName = "unidentified user";
                            }
                            CommentInfo newComment = new CommentInfo(commentEditText.getText().toString(), userName);
//                            arrayList.add(newComment);
//                            commentListViewAdapter = new CommentListViewAdapter(rootView.getContext(), arrayList);
//                            commentListView.setAdapter(commentListViewAdapter);
                            commentListViewAdapter.addComment(newComment);
                            DBinsertion(commentEditText.getText().toString(), userName);
//                            commentListView.
                        }
                    }
                });




            }
        });

        return rootView;
    }

    private void DBinsertion(String content, String userName) {
        CommentInfo newComment = new CommentInfo(content, userName);
        commentId = getTime();
        db.collection("BulletinBoard/"+did+"/Comments").document(commentId).set(newComment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "댓글 등록 성공");
                }
            }
        });

//        db.collection("BulletinBoard/"+did+"/Comments").add(newComment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                if (task.isSuccessful()) {
//                    Log.e(TAG, "댓글 등록 성공");
//                }
//            }
//        });

    }

    private String getTime() {
        mnow = System.currentTimeMillis();
        mDate = new Date(mnow);
        return mFormat.format(mDate);
    }
}
