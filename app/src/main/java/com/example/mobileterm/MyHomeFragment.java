package com.example.mobileterm;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class MyHomeFragment extends Fragment {
    FirebaseFirestore db;
    ImageView setting;
    ImageView profile;
    TextView name;
    ListView listview;
    String[] data ={"관심스터디","관심게시글"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_home, container, false);
        setting = (ImageView) rootView.findViewById(R.id.my_home_setting_iv);
        listview =(ListView) rootView.findViewById(R.id.my_home_listview);
        profile =(ImageView) rootView.findViewById(R.id.my_home_profile_iv);
        name=(TextView)rootView.findViewById(R.id.my_home_profile_name_tv);

        //DB
        //Cloud Firestore초기화
        db= FirebaseFirestore.getInstance();
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

        //리스트뷰
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1,data);
        listview.setAdapter(adapter);

        //톱니바퀴, 설정 그림 눌렀을 때
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_layout, new SettingFragment())
                        .commit();
            }
        });

        // 프로필 이미지 눌렀을 때.
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_layout, new ProfileSettingFragment())
                        .commit();
            }
        });
        //이름을 눌렀을 때
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_layout, new ProfileSettingFragment())
                        .commit();
            }
        });

        //리스트뷰 메뉴 선택했을 때
        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }
}
