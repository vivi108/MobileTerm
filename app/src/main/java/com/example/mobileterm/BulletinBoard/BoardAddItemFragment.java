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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class BoardAddItemFragment extends Fragment {
    EditText titleEditText;
    EditText tagEditText;
    EditText bodyEditText;

    ImageButton endWriteButton;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;

    String did;
    String nickname;
    String content;
    String title;
    String tags;

    String TAG = "BoardAddItem";
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
                db.collection("User").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                            String[] tagIter = tags.split(" ");
                            BulletinBoardCollection newItem = new BulletinBoardCollection(content, nickname, "default",title);
                            db.collection("BulletinBoard").add(newItem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        DocumentReference document = task.getResult();
                                        Log.e(TAG, "did:"+document.getId());
                                        did = document.getId();
                                        for (String tag: tagIter){
                                            BoardTags newTag = new BoardTags(tag.substring(1));
                                            db.collection("BulletinBoard").document(did).collection("BoardTags").add(newTag).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()){
                                                        Log.e(TAG,"tag add success");
                                                    }
                                                }
                                            });
                                        }
                                        BoardFragment boardFragment = new BoardFragment();
                                        BoardInfo newBoardItem = new BoardInfo(title, content, nickname, did);
                                        boardFragment.addNewItem(newBoardItem);
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardFragment).addToBackStack(null).commit();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        return rootView;

    }
}
