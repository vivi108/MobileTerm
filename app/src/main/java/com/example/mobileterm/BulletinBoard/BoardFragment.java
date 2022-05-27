package com.example.mobileterm.BulletinBoard;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    ArrayList<String> tagList = new ArrayList<String>();
    ArrayList<String> didList = new ArrayList<String>();
    ArrayList<String> totalDids = new ArrayList<String>();

    Dialog filterDialog;
    EditText tagSearchEditText;
    ImageButton tagSearchButton;
    Button tagEraseButton;
    TextView appliedTagsTextView;
    RadioButton orderRecent, orderLike, orderOld;
    RadioGroup radioGroup;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        Log.e(TAG, "onCreate called");
        filterDialog = new Dialog(getActivity());
        filterDialog.setContentView(R.layout.activity_pop_up_filter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_main, container, false);
        searchEditText = rootView.findViewById(R.id.searchEditText);
        listView = rootView.findViewById(R.id.listView);

        ArrayList<BoardInfo> newArrayList = new ArrayList<BoardInfo>();

        tagSearchEditText = filterDialog.findViewById(R.id.tagSearchEditText);
        tagSearchButton = filterDialog.findViewById(R.id.tagSearchButton);
        tagEraseButton = filterDialog.findViewById(R.id.tagEraseButton);
        appliedTagsTextView = filterDialog.findViewById(R.id.appliedTagsTextView);
        orderRecent = filterDialog.findViewById(R.id.orderRecent);
        orderLike = filterDialog.findViewById(R.id.orderLike);
        orderOld = filterDialog.findViewById(R.id.orderOld);
        radioGroup = filterDialog.findViewById(R.id.radioGroup);

        db.collection("BulletinBoard").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            didList.add(document.getId());
                            totalDids.add(document.getId());
                            String did = (String) document.getId();
                            String title = (String) document.getData().get("title");
                            String content = (String) document.getData().get("content");
                            String name = (String) document.getData().get("name");
                            String writtenTime = (String) document.getData().get("writtenTime");
                            Long likedCount = (Long) document.getData().get("likedCount");
//                               Log.e(TAG, did+"title : "+title);
                            BoardInfo data = new BoardInfo(title, content, name, did, writtenTime, likedCount);
                            newArrayList.add(0,data);
                        }
                    }
                    if (!newArrayList.equals(arrayList)){
                        arrayList = newArrayList;
                    }
//                        Log.e(TAG,arrayList.toString());
                    adapter = new ListViewAdapter(rootView.getContext(), arrayList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            MainActivity activity = (MainActivity) getActivity();
                            Log.e("boardItemClicked","by setOnItemCLick");

//                            activity.onFragmentChanged(arrayList.get(i), arrayList.get(i).getDid());
                            activity.onFragmentChanged(((TextView) view.findViewById(R.id.titleTextView)).getText().toString(), ((TextView) view.findViewById(R.id.contentTextView)).getText().toString(),
                                    ((TextView) view.findViewById(R.id.nameTextView)).getText().toString(), ((TextView) view.findViewById(R.id.writtenTimeTextView)).getText().toString());
                        }
                    });
                    rootView.findViewById(R.id.searchButton).setOnClickListener(onClickListener);
                    rootView.findViewById(R.id.filterButton).setOnClickListener(onClickListener);
                    rootView.findViewById(R.id.startWriteButton).setOnClickListener(onClickListener);

                    if (tagSearchEditText.getText().toString().length() == 0 && searchEditText.getText().toString().length() == 0){
                        // 정렬만 하면된다
                        adapter.reorder(radioGroup.getCheckedRadioButtonId());
                    }else if (tagSearchEditText.getText().toString().length() == 0 && searchEditText.getText().toString().length() > 0){
                        // 검색하고 정렬 확인해서 하면된다,
                        adapter.filter(searchEditText.getText().toString(), radioGroup.getCheckedRadioButtonId());
                    }else if (tagSearchEditText.getText().toString().length() > 0 && searchEditText.getText().toString().length() == 0){
                        // 정렬이랑 태그만
                        tagList = new ArrayList<String>();
                        didList = new ArrayList<String >();
                        for (String tempTag: tagSearchEditText.getText().toString().split("#")){
                            if (tempTag.length() > 0){
                                tagList.add("#"+tempTag.trim());
                            }
                        }
                        appliedTagsTextView.setText("설정된 태그: "+tagSearchEditText.getText().toString());
                        db.collectionGroup("tagDocs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    QuerySnapshot querySnapshot = task.getResult();
                                    for (DocumentSnapshot document : querySnapshot){
                                        if (tagList.contains((String) document.getData().get("tag")) && !didList.contains((String) document.getData().get("did"))){
                                            didList.add((String) document.getData().get("did"));
                                        }
                                    }
                                    adapter.filter(didList,radioGroup.getCheckedRadioButtonId());
//                                    filterDialog.dismiss();
                                }
                            }
                        });
                    }else{
                        // 전부 확인해야함
                        tagList = new ArrayList<String>();
                        didList = new ArrayList<String >();
                        for (String tempTag: tagSearchEditText.getText().toString().split("#")){
                            if (tempTag.length() > 0){
                                tagList.add("#"+tempTag.trim());
                            }
                        }
                        appliedTagsTextView.setText("설정된 태그: "+tagSearchEditText.getText().toString());
                        db.collectionGroup("tagDocs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    QuerySnapshot querySnapshot = task.getResult();
                                    for (DocumentSnapshot document : querySnapshot){
                                        if (tagList.contains((String) document.getData().get("tag")) && !didList.contains((String) document.getData().get("did"))){
                                            didList.add((String) document.getData().get("did"));
                                        }
                                    }
                                    adapter.searchTagReorder(searchEditText.getText().toString(),didList,radioGroup.getCheckedRadioButtonId());
//                                    filterDialog.dismiss();
                                }
                            }
                        });
                    }
                }
            }
        });


        return rootView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.searchButton:
                    String search = searchEditText.getText().toString();
                    adapter.filter(search, radioGroup.getCheckedRadioButtonId());
                    break;
                case R.id.filterButton:
                    showDialog();

                    break;
                case R.id.startWriteButton:
                    MainActivity activity = (MainActivity) getActivity();
                    activity.onFragmentChanged(201);
                    break;
            }
        }
    };


    public void showDialog(){
        filterDialog.show();
        tagSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"tagSearch pressed");
                String raw = tagSearchEditText.getText().toString();
                if (raw.length() == 0){
                    //검색 정렬만
                    adapter.reorder(radioGroup.getCheckedRadioButtonId());
                    filterDialog.dismiss();
                }else{
                    //검색 정렬 태그 모두 다
                    tagList = new ArrayList<String>();
                    didList = new ArrayList<String >();
                    for (String tempTag:raw.split("#")){
                        if (tempTag.length() > 0){
                            tagList.add("#"+tempTag.trim());
                        }
                    }
                    appliedTagsTextView.setText("설정된 태그: "+raw);
                    db.collectionGroup("tagDocs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                QuerySnapshot querySnapshot = task.getResult();
                                for (DocumentSnapshot document : querySnapshot){
                                    if (tagList.contains((String) document.getData().get("tag")) && !didList.contains((String) document.getData().get("did"))){
                                        didList.add((String) document.getData().get("did"));
                                    }
                                }
                                adapter.filter(didList,radioGroup.getCheckedRadioButtonId());
                                filterDialog.dismiss();
                            }
                        }
                    });
                }

            }
        });

        tagEraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 태그를 지우는데 검색이랑 정렬은 적용
                didList = totalDids;
                tagSearchEditText.setText("");
                tagSearchEditText.setHint("태그를 입력하세요.");
                appliedTagsTextView.setText("설정된 태그:");
                adapter.filter(searchEditText.getText().toString(), radioGroup.getCheckedRadioButtonId());
                filterDialog.dismiss();
            }
        });
    }



}
