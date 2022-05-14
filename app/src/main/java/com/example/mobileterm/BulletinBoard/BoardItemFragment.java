package com.example.mobileterm.BulletinBoard;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Comment;

import java.lang.ref.Reference;
import java.util.ArrayList;

public class BoardItemFragment extends Fragment {
    private String did;
    private BoardInfo selectedBoardItem;
    private FirebaseFirestore db;
    ListView commentListView;
    CommentListViewAdapter commentListViewAdapter;
    ArrayList<CommentInfo> arrayList = new ArrayList<CommentInfo>();

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

        return rootView;
    }
}
