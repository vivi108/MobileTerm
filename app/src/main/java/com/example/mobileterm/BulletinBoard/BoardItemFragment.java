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
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.lang.ref.Reference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BoardItemFragment extends Fragment {
    private String did;
//    private BoardInfo selectedBoardItem;
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
    EditText commentEditText;
    TextView titleTextViewBoardItem;
    TextView timeTextView ;
    TextView nameTextViewBoardItem;
    TextView contentTextViewBoardItem;
    TextView tagTextViewBoardItem;
    long curLike;
    long updateLike;
    boolean notLiked;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_item, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
//        selectedBoardItem = mainActivity.sendBoardItem();
        did = mainActivity.sendDid();
        Log.d(TAG,did);
        commentListView = rootView.findViewById(R.id.commentView);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();


        timeTextView = rootView.findViewById(R.id.timeTextView);
        nameTextViewBoardItem = rootView.findViewById(R.id.nameTextViewBoardItem);
        contentTextViewBoardItem = rootView.findViewById(R.id.contentTextViewBoardItem);
        titleTextViewBoardItem = rootView.findViewById(R.id.titleTextViewBoardItem);
        tagTextViewBoardItem = rootView.findViewById(R.id.tagTextViewBoardItem);
        Button likeButton = rootView.findViewById(R.id.likeButton);
        commentEditText = rootView.findViewById(R.id.commentEditText);
        ImageButton addCommentButtonBoardItem = rootView.findViewById(R.id.addCommentButtonBoardItem);

        likeButton.setOnClickListener(onClickListener);
        addCommentButtonBoardItem.setOnClickListener(onClickListener);
        db.collection("BulletinBoard").document(did).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        titleTextViewBoardItem.setText((String) document.getData().get("title"));
                        contentTextViewBoardItem.setText((String) document.getData().get("content"));
                        timeTextView.setText((String) document.getData().get("writtenTime"));
                        nameTextViewBoardItem.setText((String) document.getData().get("name"));

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
                                            String writtenTime = (String) document.getData().get("writtenTime");
                                            CommentInfo data = new CommentInfo(content, name, writtenTime);
                                            newArrayList.add(0,data);
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

                    }
                }
            }
        });







        return rootView;
    }

    private void DBinsertion(String content, String userName, String writtenTime) {
        CommentInfo newComment = new CommentInfo(content, userName, writtenTime);
        commentId = getTime()+userName;
        db.collection("BulletinBoard/"+did+"/Comments").document(commentId).set(newComment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "댓글 등록 성공");
                }
            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.addCommentButtonBoardItem:

                    addComment();

                    break;
                case R.id.likeButton:
                    notLiked = true;
                    db.collection("Users").document(curUser.getUid()).collection("likedBoardItem").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot documentSnapshot = task.getResult();
                                for (DocumentSnapshot document : documentSnapshot) {
                                    if (document.exists()) {
                                        Log.d(TAG,document.getId());
                                        if (did.equals(document.getId())){
                                            notLiked = false;
                                            break;
                                        }
                                    }
                                }
                                if (notLiked){
                                    String title = titleTextViewBoardItem.getText().toString();
                                    addToLikedItem(title);
                                }else{
                                    Toast.makeText(getActivity().getApplicationContext(),"이미 좋아요 눌린 게시글입니다.",Toast.LENGTH_LONG).show();
                                    Log.d(TAG,"like already pressed");
                                }
                            }
                        }
                    });

                    break;

            }
        }
    };

    private void addComment(){
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
                    String curTime = getTime();
                    CommentInfo newComment = new CommentInfo(commentEditText.getText().toString(), userName, curTime);
//                            arrayList.add(newComment);
//                            commentListViewAdapter = new CommentListViewAdapter(rootView.getContext(), arrayList);
//                            commentListView.setAdapter(commentListViewAdapter);
                    commentListViewAdapter.addComment(newComment);
                    DBinsertion(commentEditText.getText().toString(), userName, curTime);
//                            commentListView.
                    commentEditText.setText("");
                }
            }
        });
    }

    public void addToLikedItem(String title){
         db.collection("Users").document(curUser.getUid()).collection("likedBoardItem").document(did).set(new LikedBoardItem(title, did)).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "successfully added");
                    db.collection("BulletinBoard").document(did).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()){
                                    curLike = (Long) documentSnapshot.getData().get("likedCount");
                                    updateLike = curLike+1;
                                    db.collection("BulletinBoard").document(did).update("likedCount", updateLike).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG,"liked done");
                                            Toast.makeText(getActivity().getApplicationContext(), "관심 게시글에 추가되었습니다.",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
             }
         });
    }

    private String getTime() {
        mnow = System.currentTimeMillis();
        mDate = new Date(mnow);
        return mFormat.format(mDate);
    }
}
