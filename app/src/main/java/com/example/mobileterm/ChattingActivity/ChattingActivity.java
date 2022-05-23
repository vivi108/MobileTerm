package com.example.mobileterm.ChattingActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mobileterm.R;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {
    private ArrayList<DataItem> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        initData();
        RecyclerView recyclerView = findViewById(R.id.chatting_recyvlerv);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new MyAdapter(dataList));
    }
    private void initData(){
        dataList = new ArrayList<>();
        dataList.add(new DataItem("사용자1님 입장", null, Code.ViewType.CENTER_CONTENT));
        dataList.add(new DataItem("사용자2님 입장", null, Code.ViewType.CENTER_CONTENT));
        dataList.add(new DataItem("안녕하세요1", "사용자1",Code.ViewType.LEFT_CONTENT));
        dataList.add(new DataItem("안녕하세요2", "사용자2",Code.ViewType.RIGHT_CONTENT));
    }
}