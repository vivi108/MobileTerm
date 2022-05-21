package com.example.mobileterm.BulletinBoard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class BoardAddItemFragment extends Fragment {
    EditText titleEditText;
    EditText tagEditText;
    EditText bodyEditText;

    ImageButton endWriteButton;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;

//    String did;
    String nickname;
    String content;
    String title;
    String tags;
    String[] tagIter;
    String TAG = "BoardAddItem";
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    long mnow;
    Date mDate;
    String boardId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_add_item, container, false);

        titleEditText = rootView.findViewById(R.id.titleEditText);
        tagEditText = rootView.findViewById(R.id.tagEditText);
        bodyEditText = rootView.findViewById(R.id.bodyEditText);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        endWriteButton = rootView.findViewById(R.id.endWriteButton);


        endWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                nickname = (String) documentSnapshot.getData().get("nickname");
                            }else{
                                nickname = "unidentified user";
                            }
                            title = titleEditText.getText().toString();
                            content = bodyEditText.getText().toString();
                            tags = tagEditText.getText().toString();
                            tagIter = tags.split(" ");


//                            BulletinBoardCollection newItem = new BulletinBoardCollection(content, nickname, "default",title);

                            boardId = getTime()+" "+nickname;
                            BoardInfo newItem = new BoardInfo(title, content, nickname, boardId, getTime(), (long)0);
                            db.collection("BulletinBoard").document(boardId).set(newItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        WriteBatch batch = db.batch();
                                        for (String tag: tagIter) {
                                            DocumentReference tempref = db.collection("BulletinBoard").document(boardId).collection("BoardTags").document(tag);
                                            BoardTags newTag = new BoardTags(tag);
                                            batch.set(tempref, newTag);
                                        }
                                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e(TAG, "tag add sucess");
                                                    titleEditText.setText("");
                                                    tagEditText.setText("");
                                                    bodyEditText.setText("");
                                                    fragmentChange();
                                                }
                                            }
                                        });

                                    }
                                }
                            });

//                            db.collection("BulletinBoard").add(newItem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentReference> task) {
//                                    if (task.isSuccessful()) {
//                                        DocumentReference document = task.getResult();
//                                        Log.e(TAG, "did:"+document.getId());
//                                        did = document.getId();
////                                        newBoardItem = new BoardInfo(title, content, nickname, did);
//                                        WriteBatch batch = db.batch();
//                                        for (String tag: tagIter) {
//                                            DocumentReference tempref = db.collection("BulletinBoard").document(did).collection("BoardTags").document(tag);
//                                            BoardTags newTag = new BoardTags(tag);
//                                            batch.set(tempref, newTag);
//                                        }
//
//                                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Log.e(TAG,"tag add success");
//                                                    fragmentChange();
//                                                }
//                                            }
//                                        });
//
//
//
//
//                                    }
//                                }
//                            });
                        }
                    }
                });
            }
        });

        return rootView;

    }

    public void fragmentChange() {
        BoardFragment boardFragment = new BoardFragment();
//        boardFragment.addNewItem(newBoardItem);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardFragment).addToBackStack(null).commit();
    }

    private String getTime() {
        mnow = System.currentTimeMillis();
        mDate = new Date(mnow);
        return mFormat.format(mDate);
    }
}
