package com.example.mobileterm;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mobileterm.Calendar.CalendarFragment;
import com.example.mobileterm.Calendar.iCalendarFragment;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MyHomeFragment extends Fragment {
    ImageView setting;
    ImageView profile;
    TextView name, beforetodo, aftertodo, token;
    ExpandableListView listview;
    ArrayList<myGroup> DataList;
    ExpandAdapter adapter;
    String[][] childids = new String[999][999];
    String uid;
    BarChart barChart;

    int[] cnt = new int[7]; //전체 할 일 개수
    int[] isDone_cnt = new int[7];
    //Firebase로 로그인한 사용자 정보 알기 위해
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public MyHomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_home, container, false);
        setting = (ImageView) rootView.findViewById(R.id.my_home_setting_iv);
        listview = (ExpandableListView) rootView.findViewById(R.id.my_home_listview);
        profile = (ImageView) rootView.findViewById(R.id.my_home_profile_iv);
        name = (TextView) rootView.findViewById(R.id.my_home_profile_name_tv);
        beforetodo = (TextView) rootView.findViewById(R.id.my_home_make_todo_tv);
        aftertodo = (TextView) rootView.findViewById(R.id.my_home_todo_tv);
        token = (TextView) rootView.findViewById(R.id.my_home_profile_token_num_tv);
        barChart = (BarChart) rootView.findViewById(R.id.my_home_bar_chart);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            uid = user.getUid();
        } else {
            // No user is signed in
        }
        //함수 순서 바뀌면 안됨
        loadImage(uid);
        Getname(user);
        GetToken(user);
        get_todo(user);
        todo_tv(); // 오늘 할일 N개 남았어요 - 여기를 눌러서 오늘의 할일을 지정해주세요 문구변경
        cnt[0] = 0;
        cnt[1] = 0;
        cnt[2] = 0;
        cnt[3] = 0;
        cnt[4] = 0;
        cnt[5] = 0;
        cnt[6] = 0;

        isDone_cnt[0] = 0;
        isDone_cnt[1] = 0;
        isDone_cnt[2] = 0;
        isDone_cnt[3] = 0;
        isDone_cnt[4] = 0;
        isDone_cnt[5] = 0;
        isDone_cnt[6] = 0;
        //화장 리스트뷰
        Display newDisplay = requireActivity().getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        DataList = new ArrayList<myGroup>();
        myGroup temp = new myGroup("관심스터디");
        temp.child.add("여");
        temp.child.add("기");
        temp.child.add("도");
        DataList.add(temp);
        temp = new myGroup("관심게시글");

        addChildListView_Board(temp, user);
        adapter = new ExpandAdapter(requireActivity(), R.layout.expandable_liistview_parent, R.layout.expandable_listview_child, DataList);
        listview.setIndicatorBounds(width - 50, width); //이 코드를 지우면 화살표 위치가 바뀐다.
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
        //여기를 눌러서 오늘의 할일을 지정해주세요 -> 캘린더 fragment로 이동
        beforetodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomNavigationView botNavView = requireActivity().findViewById(R.id.main_bnv);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_layout, new iCalendarFragment())
                        .addToBackStack(null)
                        .commit();
                botNavView.setSelectedItemId(R.id.nav_menu_calendar);

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
        //이름을 눌렀을 때 -->완성
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_layout, new ProfileSettingFragment())
                        .commit();
            }
        });
        //확장 리스트의 childlist를 눌렀을 때.
        //아이디랑 비교해서 게시글 보여주어야함
        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                MainActivity activity = (MainActivity) getActivity();
                switch (groupPosition) {
                    case 0://관심스터디

                        break;
                    case 1:
                        Log.e("boardItemClicked", "by setOnItemCLick from LikedBoardItem");
                        String getchildid = childids[1][childPosition];
                        Log.d("getchildid", getchildid + "가 선택됨");
                        GetLikedBoardItem(user, getchildid);
                        break;
                }

                return false;
            }
        });
        return rootView;
    }


    private void GetLikedBoardItem(FirebaseUser firebaseUser, String did) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fb.collection("BulletinBoard").document(did);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            MainActivity activity = (MainActivity) getActivity();
                            String title = (String) document.getData().get("title");
                            String content = (String) document.getData().get("content");
                            String uName = (String) document.getData().get("name");
                            String wTime = (String) document.getData().get("writtenTime");
                            Log.d("getchildid", title);
                            Log.d("getchildid", content);
                            Log.d("getchildid", uName);
                            Log.d("getchildid", wTime);
                            activity.onFragmentChanged(title, content, uName, wTime);

                        }
                    }
                }
            }
        });

    }

    //확장 리스트뷰 차일드 정보 가져오기 by likedBoardItem-->완성
    private void addChildListView_Board(myGroup temp, FirebaseUser firebaseUser) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fb.collection("Users").document(firebaseUser.getUid()).collection("likedBoardItem");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //document.getData() or document.getId() 등등 여러 방법으로
                        //데이터를 가져올 수 있다.

                        String childTitle = (String) document.getData().get("title");
                        String childid = (String) document.getData().get("did");
                        // temp.childId.add(childid);
                        temp.child.add(childTitle);
                        childids[1][i] = childid;
                        i++;

                    }
                }
            }
        });

        DataList.add(temp);
    }

    private void todo_tv() {
        //오늘치 정보 없으면 todobefore
        //오늘치 정보 있으면 todoafter + 문구 업데이트

        beforetodo.setVisibility(View.GONE);
        aftertodo.setVisibility(View.VISIBLE);
        if (cnt[6] == 0) {
            beforetodo.setVisibility(View.VISIBLE);
            aftertodo.setVisibility(View.GONE);
        }
    }

    String datePattern = "yyyyMMdd";

    private void get_todo(FirebaseUser firebaseUser) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        String date7 = sdf.format(cal.getTime());
        cal.add(cal.DATE, -1);
        String date6 = sdf.format(cal.getTime());
        cal.add(cal.DATE, -1);
        String date5 = sdf.format(cal.getTime());
        cal.add(cal.DATE, -1);
        String date4 = sdf.format(cal.getTime());
        cal.add(cal.DATE, -1);
        String date3 = sdf.format(cal.getTime());
        cal.add(cal.DATE, -1);
        String date2 = sdf.format(cal.getTime());
        cal.add(cal.DATE, -1);
        String date1 = sdf.format(cal.getTime());

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fb.collection("Users").document(firebaseUser.getUid()).collection("iSchedule");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //document.getData() or document.getId() 등등 여러 방법으로
                        //데이터를 가져올 수 있다.
                        String date = (String) document.getData().get("date");
                        // Date getdate =changeDateFormat(date);
                        Log.d("가져온 date", date);
                        Log.d("MyFragment date", date7);
                        String isDone = (String) document.getData().get("isDone");

                        if (date7.equals(date)) {//오늘
                            cnt[6] = cnt[6] + 1;

                            if (isDone.equals("true")) {
                                isDone_cnt[6]++;
                            }
                            // Log.d("MyhomeFragment", "cnt 6 :"+cnt[6]);
                        } else if (date6.equals(date)) {
                            //cnt[5]++;
                            cnt[5] = cnt[5] + 1;
                            if (isDone.equals("true")) {
                                isDone_cnt[5]++;
                            }
                            // Log.d("MyhomeFragment", "cnt 5 :"+cnt[5]);
                        } else if (date5.equals(date)) {
                            cnt[4] = cnt[4] + 1;
                            if (isDone.equals("true")) {
                                isDone_cnt[4]++;
                            }
                            //Log.d("MyhomeFragment", "cnt 4 :"+cnt[4]);
                        } else if (date4.equals(date)) {
                            cnt[3] = cnt[3] + 1;
                            if (isDone.equals("true")) {
                                isDone_cnt[3]++;
                            }
                            //Log.d("MyhomeFragment", "cnt 3 :"+cnt[3]);
                        } else if (date3.equals(date)) {
                            cnt[2] = cnt[2] + 1;
                            if (isDone.equals("true")) {
                                isDone_cnt[2]++;
                            }
                            //Log.d("MyhomeFragment", "cnt 2 :"+cnt[2]);
                        } else if (date2.equals(date)) {
                            cnt[1] = cnt[1] + 1;
                            if (isDone.equals("true")) {
                                isDone_cnt[1]++;
                            }
                            //Log.d("MyhomeFragment", "cnt 1 :"+cnt[1]);
                        } else if (date1.equals(date)) {
                            //cnt[0]++;
                            cnt[0] = cnt[0] + 1;
                            if (isDone.equals("true")) {
                                isDone_cnt[0]++;
                            }

                        }
                        ;
                    }graphInitSetting();
                }
            }
        });
        for (int i = 0; i < 7; i++) {
            Log.d("MyhomeFragment", "cnt(함수전)  " + i + " ; " + cnt[i]);
        }

    }

    private void graphInitSetting() {
        ArrayList<Float> jsonList = new ArrayList<>(); // ArrayList 선언
        ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언
        float[] percentage = new float[]{0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 7; i++) {
            Log.d("MyhomeFragment", "cnt  " + i + " ; " + cnt[i]);
            if (cnt[i] == 0) {
                percentage[i] = 0;
                Log.d("MyhomeFragment", "percentage  " + i + " ; " + percentage[i]);
            } else {
                Log.d("MyhomeFragment", "isDone  " + i + " ; " + isDone_cnt[i]);

                percentage[i] = (((float) isDone_cnt[i] / (float) cnt[i])) * 100;
                Log.d("MyhomeFragment", "divide  " + i + " ; " + ((float) isDone_cnt[i] / (float) cnt[i]));
                Log.d("MyhomeFragment", "percentage  " + i + " ; " + percentage[i]);
            }

        }
        jsonList.clear();
        //서버에서 정보 받아와야함.
        jsonList.add(percentage[0]);
        jsonList.add(percentage[1]);
        jsonList.add(percentage[2]);
        jsonList.add(percentage[3]);
        jsonList.add(percentage[4]);
        jsonList.add(percentage[5]);
        jsonList.add(percentage[6]);


        labelList.clear();
        labelList.add(" ");
        labelList.add(" ");
        labelList.add(" ");
        labelList.add(" ");
        labelList.add(" ");
        labelList.add(" ");
        labelList.add("오늘");

        BarChartGraph(labelList, jsonList);

        barChart.setTouchEnabled(false); //확대하지못하게 막아버림
//        barChart.setRendererLeftYAxis();
        barChart.setMaxVisibleValueCount(50);
        barChart.setTop(50);
        barChart.setBottom(0);
        barChart.setAutoScaleMinMaxEnabled(true);
        barChart.getAxisRight().setAxisMaxValue(100);
        barChart.getAxisLeft().setAxisMaxValue(100);

    }

    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Float> valList) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < valList.size(); i++) {
            entries.add(new BarEntry((float) valList.get(i), i));
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
                            token.setText(" "+a);
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
                            .circleCrop()
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
    private void loadImage(String uid) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://mptermproject-75489.appspot.com"); //firebase storate 경로
        StorageReference storageRef = storage.getReference();

        //파일 명 만들기
        String filename = "profile" + uid + ".jpg"; //ex) profile1.jpg 로그인 하는 사람의 식별값에 맞는 사진 가져오기
        if (storageRef.child("profile_image/" + filename) != null) {
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
                    loadbasicImage();
                }
            });
        }
    }
}

