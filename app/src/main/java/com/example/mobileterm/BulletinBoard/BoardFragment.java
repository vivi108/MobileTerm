package com.example.mobileterm.BulletinBoard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {

    ListView listView;
    ListViewAdapter adapter;
    private FirebaseFirestore db;
    private String TAG = "boardFragment";
    EditText searchEditText;
//    QuerySnapshot boardBoardTags;
//    QuerySnapshot boardComments;
    ArrayList<BoardInfo> arrayList = new ArrayList<BoardInfo>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        Log.e(TAG, "onCreate called");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_main, container, false);
        searchEditText = rootView.findViewById(R.id.searchEditText);
        listView = rootView.findViewById(R.id.listView);
        ArrayList<BoardInfo> newArrayList = new ArrayList<BoardInfo>();

        db.collection("BulletinBoard").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            String did = (String) document.getId();
                            String title = (String) document.getData().get("title");
                            String content = (String) document.getData().get("content");
                            String name = (String) document.getData().get("name");
                            Log.e(TAG, did+"title : "+title);
                            BoardInfo data = new BoardInfo(title, content, name, did);
                            newArrayList.add(data);
                        }
                    }

                    if (!newArrayList.equals(arrayList)){
                        arrayList = newArrayList;
                    }
                    Log.e(TAG,arrayList.toString());
                    adapter = new ListViewAdapter(rootView.getContext(), arrayList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            MainActivity activity = (MainActivity) getActivity();
                            Log.d("boardItemClicked","by setOnItemCLick");

                            activity.onFragmentChanged(arrayList.get(i), arrayList.get(i).getDid());
                        }
                    });
                }
            }
        });


//        db.collectionGroup("BoardTags").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//
//                }
//            }
//        });

        rootView.findViewById(R.id.searchButton).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.filterButton).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.startWriteButton).setOnClickListener(onClickListener);

        return rootView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.searchButton:

                    break;
                case R.id.filterButton:

                    break;
                case R.id.startWriteButton:
                    MainActivity activity = (MainActivity) getActivity();
                    activity.onFragmentChanged(201);
                    break;
            }
        }
    };

}
