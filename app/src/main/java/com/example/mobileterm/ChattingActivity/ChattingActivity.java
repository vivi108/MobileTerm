package com.example.mobileterm.ChattingActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {
    private ArrayList<DataItem> dataList;
    private String uid;

    private String send_msg;

    private String CHAT_NAME = "testchatting"; // 스터디 그룹 이름
    private String USER_NAME ; // 사용자 이름

    private RecyclerView chat_view; //채팅내용
    private EditText chat_edit; //보낼내용쓰기
    private ImageButton chat_send; //보내기버튼

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        chat_view = findViewById(R.id.chatting_recyvlerv);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (ImageButton) findViewById(R.id.chat_send_btn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        //GetInfo(user);//닉네임 받아옴, 발화자와 같으면  Code.ViewType.RIGHT_CONTENT
        Intent intent = getIntent();

        USER_NAME = intent.getStringExtra("nickname");
        CHAT_NAME = intent.getStringExtra("studyName");
        Log.d("CHATTING", "USER_NAME " +USER_NAME+" CHAT_NAME"+CHAT_NAME);

        initData();
        openChat(CHAT_NAME);
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_msg = chat_edit.getText().toString();
                if (send_msg != null) {
                    chatDTO chat = new chatDTO(USER_NAME, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                    databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
                    chat_edit.setText("");//입력창 초기화

                    dataList.add(new DataItem(send_msg, USER_NAME, Code.ViewType.RIGHT_CONTENT)); //레이아웃에 보여주는 리스트
                } else return;
            }
        });


    }

    private void initData() {
        dataList = new ArrayList<>();
        dataList.add(new DataItem(USER_NAME + " 님이 입장하셨습니다", USER_NAME, Code.ViewType.CENTER_CONTENT));
    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        chatDTO chatDTO = dataSnapshot.getValue(chatDTO.class);
        adapter.add(chatDTO.getUserName() + " : " + chatDTO.getMessage());
        if(!chatDTO.getUserName().equals(USER_NAME))       {
            dataList.add(new DataItem(chatDTO.getMessage(), chatDTO.getUserName() ,Code.ViewType.LEFT_CONTENT));
        }
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        chatDTO chatDTO = dataSnapshot.getValue(chatDTO.class);
        adapter.remove(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//        chat_view.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        chat_view.setLayoutManager(manager);
        chat_view.setAdapter(new MyAdapter(dataList));

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                // Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//        public void GetInfo(FirebaseUser firebaseUser) {
//        FirebaseFirestore fb = FirebaseFirestore.getInstance();
//        DocumentReference documentReference = fb.collection("Users").document(firebaseUser.getUid());
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document != null) {
//                        if (document.exists()) {
//                            nickname = (String) document.getData().get("nickname");
//
//                        }
//                    }
//                }
//            }
//        });
//    }
}