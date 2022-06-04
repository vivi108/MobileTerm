package com.example.mobileterm.Calendar;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobileterm.BulletinBoard.BoardInfo;
import com.example.mobileterm.BulletinBoard.LikedBoardItem;
import com.example.mobileterm.BulletinBoard.ListViewAdapter;
import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class iCalendarFragment extends Fragment {
    public String str = null;
    MaterialCalendarView calendarView;
    public Button add_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;
    public RadioGroup ctype;
    public RadioButton radIndividual, radGroup;
    public CheckBox isDone;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private iCalendarFragment iCalendarFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser curUser;
    private HashMap<String, ArrayList<iCalendarItem>> dateTable = new HashMap<String, ArrayList<iCalendarItem>>();

    private ListView listview;
    private iCalendarAdapter adapter;
    private ScrollView scroll;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup main_frame_layout, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_i_calendar, main_frame_layout, false);

        calendarView = rootView.findViewById(R.id.calendarView);
        diaryTextView = rootView.findViewById(R.id.diaryTextView); //중간에 몇월 몇일 보여주는
        save_Btn = rootView.findViewById(R.id.save_Btn);
        add_Btn = rootView.findViewById(R.id.add_Btn);
        textView2 = rootView.findViewById(R.id.textView2); //일정 추가된 날짜 클릭 시 어떤 일정있나 보여주는 칸
        contextEditText = rootView.findViewById(R.id.contextEditText); //선택 날짜 일정 수정하는 칸
        ctype = rootView.findViewById(R.id.ctype);
        radIndividual = rootView.findViewById(R.id.radIndividual);
        radGroup = rootView.findViewById(R.id.radGroup);
        isDone = rootView.findViewById(R.id.isDone);
        listview = rootView.findViewById(R.id.list);
        scroll = rootView.findViewById(R.id.scroll);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();

//        ctype.check(radIndividual.getId()); //처음 실행 시에 indiv에 체크돼있도록

        //현재 로그인한 유저 users의 iSchedule 컬렉션
        CollectionReference docref = db.collection("Users").document(curUser.getUid()).collection("iSchedule");

//        ctype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                MainActivity activity = (MainActivity) getActivity();
//                activity.onFragmentChanged(1,"default");
//            }
//        });
        radGroup.setOnClickListener(new View.OnClickListener() { //그룹버튼 누르면 그룹캘린더로 전환
            @Override
            public void onClick(View view) {
                ctype.check(radIndividual.getId());
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(1,"default");
                Toast.makeText(view.getContext(), "그룹 캘린더입니다", Toast.LENGTH_SHORT).show();
//                ctype.check(radGroup.getId());
//                int radioId = ctype.getCheckedRadioButtonId();
//                if (radGroup.getId() == radioId) {
//                    MainActivity activity = (MainActivity) getActivity();
//                    activity.onFragmentChanged(1);
//                }
            }
        });

        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        // 날짜 클릭할 때 일어날 일들
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.VISIBLE);
                add_Btn.setVisibility(View.VISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", date.getYear(), date.getMonth() + 1, date.getDay())); //중간 일정 보여주는 텍스트뷰
                ArrayList<iCalendarItem> newArrayList = new ArrayList<iCalendarItem>();

                String month = String.valueOf(date.getMonth() + 1);
                if(date.getMonth()+1 < 10)
                    month = "0".concat(month); //날짜 클릭시 받아오는 월은 month 그대로
                String day = String.valueOf(date.getDay());
                if(date.getDay() < 10)
                    day = "0".concat(day); //날짜 클릭시 받아오는 일은 day 그대로

                String calendarDate = String.valueOf(date.getYear()) + month + day; //캘린더의 날짜 클릭 시 이걸로 받아옴
                System.out.println("클릭날짜 : " + calendarDate);


                try {
                    dateTable.get(calendarDate);
                    System.out.println("136" + dateTable.get(calendarDate));
                }catch (Exception e){
                    dateTable.put(calendarDate, new ArrayList<iCalendarItem>());
                    System.out.println("139" + dateTable);
                }


               //클릭한 날짜의 일정을 파베에서 불러와서 그 날에 해당하는 일정 리스트뷰 어댑터에 넣기
               docref
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            if (String.valueOf(document.getData().get("date")).equals(calendarDate))  //선택 날짜와 파베 날짜가 동일할 경우
                                            {
                                                System.out.println("클릭날짜 : " + calendarDate);
                                                System.out.println("db날짜 : " + String.valueOf(document.getData().get("date")));
                                                System.out.println("일정 : " + ((String) document.getData().get("schedule")));
                                                String schedule = ((String) document.getData().get("schedule")); //그 일정을 가져오겠다
                                                String isDone = String.valueOf(document.getData().get("isDone"));
                                                String date = ((String) document.getData().get("date"));
                                                String docA = ((String) document.getId());
                                                iCalendarItem data = new iCalendarItem(schedule, date, isDone, docA); //이 3개를 쌍으로 data에 넣음
                                                newArrayList.add(0, data);
                                            }
                                        }
                                    }
                                    try
                                    { //키값 : 날짜 + 밸류값 : 어레이리스트(일정, 날짜, isdone)
                                        if(!dateTable.get(calendarDate).equals(newArrayList))
                                        {
                                            dateTable.put(calendarDate, newArrayList); //날짜를 키값으로 밸류에 리스트 하나(newArr) 추가
                                        }
                                    }catch (Exception e){
                                        dateTable.put(calendarDate, newArrayList); //날짜를 키값으로 밸류에 리스트 하나(newArr) 추가
                                    }

                                    for (iCalendarItem itr : dateTable.get(calendarDate)){ //이 날짜에 어떤 어레이리스트 있나 출력
                                        Log.d(TAG,"item : "+itr.getDate()+" "+itr.getSchedule()+ " " + itr.getIsDone());
                                    }

                                    adapter = new iCalendarAdapter(dateTable.get(calendarDate)); //어댑터에 이 날짜 해당 데이터 다 넘겨줌
                                    listview.setAdapter(adapter); //리스트뷰에 보이도록 함
                                    contextEditText.setText("");
                                }
                            }
                        });


                //추가 버튼 -> 저장 버튼 로직으로
                add_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //추가 눌리면 edit과 저장버튼만 보이게 해주는거..
                        //이전에 있던 일정이 edit에선 안보였다가 txtview에선 추가되어 보이도록
                        contextEditText.setVisibility(View.VISIBLE);
                        contextEditText.setText("");
                        diaryTextView.setVisibility(View.VISIBLE);

                        listview.setVisibility(View.INVISIBLE);
                        save_Btn.setVisibility(View.VISIBLE);
                        add_Btn.setVisibility(View.INVISIBLE);
                    }
                });



                //저장 버튼 클릭 시 -> 에딧텍스트에 일정 넣고 저장하면 그 날에 일정에 당연히 추가되고 리스트뷰로 그 날에 있는 모든 일정 보여주기
                save_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        str = contextEditText.getText().toString(); //일정을 쓰면 그 내용이 str로 할당

                        //컬렉션에 넣을 준비

                        String Date = calendarDate; //일정 쓴 날짜겠지
                        String IsDone = "false"; //방금 추가한거니까 false "false" - 지금은 테스트용으로
                        String Context = str;
                        String docA = Date +" "+ Context;
                        //3개 묶음
                        iCalendarItem newItem = new iCalendarItem(Context, Date, IsDone, docA);

                        //에딧텍스트 일정을 db 컬렉션에 넣음 - user 안 iSchedule 컬렉션에
                        docref
                                .document(Date +" "+ Context)
                                .set(newItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "successfully added");
                                }
                            }
                        });

                        dateTable.get(calendarDate).add(newItem);
                        adapter.addItem(newItem);

                        //저장 버튼을 클릭 한 후 - 저장버튼과 edittext 안보이고 / 수정, 삭제 버튼, 일정 보여주는 거 보이게함
                        save_Btn.setVisibility(View.INVISIBLE);
                        add_Btn.setVisibility(View.VISIBLE);
                        contextEditText.setVisibility(View.INVISIBLE);
                        //isDone.setVisibility(View.INVISIBLE);
                        listview.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        return rootView;
    }
}

