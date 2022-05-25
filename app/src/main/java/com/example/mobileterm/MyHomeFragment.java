package com.example.mobileterm;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mobileterm.ProfileSettingFragment;
import com.example.mobileterm.R;
import com.example.mobileterm.SettingFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MyHomeFragment extends Fragment {
    Uri imageUri;
    ImageView setting;
    ImageView profile;
    TextView name, beforetodo,aftertodo,token;
    ListView listview;
    String[] data ={"관심스터디","관심게시글"};
    String uid;
    BarChart barChart;
    ArrayList<Integer> jsonList ; // ArrayList 선언
    ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언

    //Firebase로 로그인한 사용자 정보 알기 위해
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //프로필 uri이용해 bitmap으로
    Bitmap bitmap;

    public MyHomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_home, container, false);
        setting = (ImageView) rootView.findViewById(R.id.my_home_setting_iv);
        listview = (ListView) rootView.findViewById(R.id.my_home_listview);
        profile = (ImageView) rootView.findViewById(R.id.my_home_profile_iv);
        name = (TextView) rootView.findViewById(R.id.my_home_profile_name_tv);
        beforetodo = (TextView) rootView.findViewById(R.id.my_home_make_todo_tv);
        aftertodo = (TextView) rootView.findViewById(R.id.my_home_todo_tv);
        token = (TextView) rootView.findViewById(R.id.my_home_profile_token_num_tv);
        barChart = (BarChart)rootView.findViewById(R.id.my_home_bar_chart);

        graphInitSetting(); //그래프 기본 세팅
        todo_tv(); // 오늘 할일 N개 남았어요 - 여기를 눌러서 오늘의 할일을 지정해주세요 문구변경
        //Firebase 로그인한 사용자 정보
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();
            //boolean emailVerified = user.isEmailVerified();
            uid= user.getUid();
        } else {
            // No user is signed in
        }
        loadImage(uid);
        Getname(user);
        GetToken(user);
        get_todo(user);
        //리스트뷰
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, data);
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
        //여기를 눌러서 오늘의 할일을 지정해주세요
        beforetodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return rootView;
    }
    private void todo_tv(){
        //오늘치 정보 없으면 todobefore
        //오늘치 정보 있으면 todoafter + 문구 업데이트
        aftertodo.setVisibility(View.GONE);


    }
    private String changeDateFormat(Calendar cal){
        String result="";
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int date = cal.get(Calendar.DATE);
        result = year + "-" +month + "-" + date;
        return result;
    }
    private void get_todo(FirebaseUser firebaseUser){
        jsonList=new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int[] cnt={0,0,0,0,0,0,0}; //전체 할 일 개수
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        CollectionReference collectionReference =fb.collection("Users").document(firebaseUser.getUid()).collection("iSchedule");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //document.getData() or document.getId() 등등 여러 방법으로
                        //데이터를 가져올 수 있다.
                        Calendar date = (Calendar) document.getData().get("date");
                        String isDone = (String) document.getData().get("isDone");
                        if (changeDateFormat(cal).equals(changeDateFormat(date))) {//오늘
                                  cnt[6]++;
                             }
//                        else if (changeDateFormat(cal.add(Calendar.DATE, -1)).equals(changeDateFormat(date.add(Calendar.DATE, -1)))){//오늘-1
//
//                        }
                    }
                }
            }
        });

//        DocumentReference documentReference = fb.collection("Users").document(firebaseUser.getUid()).collection("iSchedule").document();
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    List list =(List) document.getData();
//                    if (document != null) {
//                        if (document.exists()) {
//                            for(int i=0; i<list.size(); i++){
//                                Log.i("MyHomeFragment", "data["+i+"] > " + list.get(i).toString());
////                                CalendarDay date = (CalendarDay) document.getData().get("date");
////                                String isDone = (String) document.getData().get("isDone");
////                                if (date //선택 날짜와 파베 날짜가 동일할 경우
////                                        .equals(String.valueOf(date.getYear()) + "-" + String.valueOf(date.getMonth() + 1) + "-" + String.valueOf(date.getDay()))) {
////                                    int todaypercent=  ;
////                                    jsonList.add(todaypercent);
//                                }
//                            }
//
//                        }
//                    }
//                }
//
//        });
    }
    private void graphInitSetting() {
        labelList.add("일");
        labelList.add("월");
        labelList.add("화");
        labelList.add("수");
        labelList.add("목");
        labelList.add("금");
        labelList.add("토");

        //서버에서 정보 받아와야함.
        jsonList.add(10);
        jsonList.add(20);
        jsonList.add(30);
        jsonList.add(40);
        jsonList.add(50);
        jsonList.add(60);
        jsonList.add(70);

        BarChartGraph(labelList, jsonList);
        barChart.setTouchEnabled(false); //확대하지못하게 막아버림
        //barChart.setRendererLeftYAxis();
//        barChart.setMaxVisibleValueCount(50);
//        barChart.setTop(50);
//        barChart.setBottom(0);
//        barChart.setAutoScaleMinMaxEnabled(true);
        barChart.getAxisRight().setAxisMaxValue(80);
        barChart.getAxisLeft().setAxisMaxValue(80);
    }

    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < valList.size(); i++) {
            entries.add(new BarEntry((Integer) valList.get(i), i));
        }

        BarDataSet depenses = new BarDataSet(entries, "일일 완성도"); // 변수로 받아서 넣어줘도 됨
        depenses.setAxisDependency(YAxis.AxisDependency.LEFT);
        barChart.setDescription(" ");

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < labelList.size(); i++) {
            labels.add((String) labelList.get(i));
        }

        BarData data = new BarData(labels, depenses); // 라이브러리 v3.x 사용하면 에러 발생함
        depenses.setColors(ColorTemplate.LIBERTY_COLORS); //

        barChart.setData(data);
        barChart.animateXY(1000, 1000);
        barChart.invalidate();
    }


    public void Getname(FirebaseUser firebaseUser) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fb.collection("Users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            String a = (String) document.getData().get("nickname");
                            name.setText(a);
                        }
                    }
                }
            }
        });
    }
    public void GetToken(FirebaseUser firebaseUser) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fb.collection("Users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            String a = (String) document.getData().get("token");
                            token.setText(a);
                        }
                    }
                }
            }
        });
    }
    //서버에서 기본 이미지 로드하기
    private void loadbasicImage() {

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://mptermproject-75489.appspot.com"); //firebase storate 경로
        StorageReference storageRef = storage.getReference();
        if (storageRef.child("profile_image/ic_profile_gray.jpg") != null) {
            storageRef.child("profile_image/ic_profile_gray.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //이미지 로드 성공시


                    Glide.with(getActivity())
                            .load(uri)
                            .into(profile);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //이미지 로드 실패시
                    Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    //서버에서 사용자 지정 프로필 이미지 로드하기
    private void loadImage(String uid){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://mptermproject-75489.appspot.com"); //firebase storate 경로
        StorageReference storageRef = storage.getReference();

        //파일 명 만들기
        String filename = "profile" +uid +".jpg"; //ex) profile1.jpg 로그인 하는 사람의 식별값에 맞는 사진 가져오기
        if(storageRef.child("profile_image/"+filename)!=null) {
            storageRef.child("profile_image/" + filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //이미지 로드 성공시

                    Glide.with(getActivity())
                            .load(uri)
                            .into(profile);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //이미지 로드 실패시
                    //Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    loadbasicImage();
                }
            });
        }
    }
}
