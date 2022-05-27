package com.example.mobileterm.Calendar;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    public String readDay = null;
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
    ArrayList<iCalendarItem> scheduleList = new ArrayList<iCalendarItem>();

    private ListView listview;
    private iCalendarAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup main_frame_layout, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_i_calendar, main_frame_layout, false);

        calendarView = rootView.findViewById(R.id.calendarView);
        diaryTextView = rootView.findViewById(R.id.diaryTextView); //중간에 몇월 몇일 보여주는
        save_Btn = rootView.findViewById(R.id.save_Btn);
        del_Btn = rootView.findViewById(R.id.del_Btn);
        add_Btn = rootView.findViewById(R.id.add_Btn);
        textView2 = rootView.findViewById(R.id.textView2); //일정 추가된 날짜 클릭 시 어떤 일정있나 보여주는 칸
        //textView3 = rootView.findViewById(R.id.textView3); //맨 위 달력이라 표시
        contextEditText = rootView.findViewById(R.id.contextEditText); //선택 날짜 일정 수정하는 칸
        ctype = rootView.findViewById(R.id.ctype);
        radIndividual = rootView.findViewById(R.id.radIndividual);
        radGroup = rootView.findViewById(R.id.radGroup);
        isDone = rootView.findViewById(R.id.isDone);
        listview = rootView.findViewById(R.id.list);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();

        //현재 로그인한 유저 users의 iSchedule 컬렉션
        CollectionReference docref = db.collection("Users").document(curUser.getUid()).collection("iSchedule");

        //복사하면 사용할 어레이리스트


//        adapter = new iCalendarAdapter();



        radGroup.setOnClickListener(new View.OnClickListener() { //그룹버튼 누르면 그룹캘린더로 전환
            @Override
            public void onClick(View view) {
                int radioId = ctype.getCheckedRadioButtonId();
                if (radGroup.getId() == radioId) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.onFragmentChanged(1);
                }
            }
        });


        // 날짜 클릭할 때 일어날 일들
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);

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
                }catch (Exception e){
                    dateTable.put(calendarDate, new ArrayList<iCalendarItem>());
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
//                                                adapter.addItem(schedule);
//                                                adapter.notifyDataSetChanged();
                                                iCalendarItem data = new iCalendarItem(schedule, date, isDone); //이 3개를 쌍으로 data에 넣음
                                                newArrayList.add(0, data);
                                            }
                                        }
                                    }
                                    try {
                                        if(!dateTable.get(calendarDate).equals(newArrayList)){
                                            dateTable.put(calendarDate, newArrayList);
                                        }
                                    }catch (Exception e){
                                        dateTable.put(calendarDate, newArrayList);
                                    }
                                    adapter = new iCalendarAdapter(dateTable.get(calendarDate));
                                    listview.setAdapter(adapter);

                                }
                            }
                        });

                listview.setVisibility(View.VISIBLE);
                add_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);

                //중간 날짜 어케 보여줄지 + 일정 추가되는 칸 초기화
                diaryTextView.setText(String.format("%d / %d / %d", date.getYear(), date.getMonth() + 1, date.getDay()));
                contextEditText.setText("");


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
                        del_Btn.setVisibility(View.INVISIBLE);
                    }
                });


                //삭제 버튼 눌리면 -> 아직 노구현
                del_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //1
                        //이건 나중에 리스트 형태로 바꿨을때 구현하기로 - 어떤 항목 삭제할건지 2번 화면 띄우기
                        textView2.setVisibility(View.VISIBLE);
                        contextEditText.setText("");
                        contextEditText.setVisibility(View.VISIBLE);
                        //isDone.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.INVISIBLE);
                        save_Btn.setVisibility(View.VISIBLE);
                        add_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                    }
                });


                //저장 버튼 클릭 시 -> 에딧텍스트에 일정 넣고 저장하면 그 날에 일정에 당연히 추가되고 리스트뷰로 그 날에 있는 모든 일정 보여주기
                save_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        str = contextEditText.getText().toString(); //일정을 쓰면 그 내용이 str로 할당

                        //컬렉션에 넣을 준비
                        String Date = calendarDate; //일정 쓴 날짜겠지
                        String IsDone = "false"; //방금 추가한거니까 false
                        String Context = str;

                        //에딧텍스트 일정을 db 컬렉션에 넣음 - user 안 iSchedule 컬렉션에
                        docref
                                .document()
                                .set(new iCalendarItem(Context, Date, IsDone)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "successfully added");
                                }
                            }
                        });

                        //이제 파베에서 불러옴
                        //불러와서 해당날짜 일정을 리스트뷰에 넣어놓음
                        docref
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if (document.exists()) {
                                                    if (String.valueOf(document.getData().get("date")).equals(calendarDate))
                                                    {
                                                        String str = ((String) document.getData().get("schedule")); //그 일정을 가져오겠다
                                                        String isDone = String.valueOf(document.getData().get("isDone"));
                                                        String date = ((String) document.getData().get("date"));
                                                        adapter.addItem(str); //리스트에서는 스케줄만 보여줄거니까?
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                            listview.setAdapter(adapter);
                                        }
                                    }
                                });
                        //저장 버튼을 클릭 한 후 - 저장버튼과 edittext 안보이고 / 수정, 삭제 버튼, 일정 보여주는 거 보이게함
                        save_Btn.setVisibility(View.INVISIBLE);
                        add_Btn.setVisibility(View.VISIBLE);
                        del_Btn.setVisibility(View.VISIBLE);
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

//                                                iCalendarItem data = new iCalendarItem(schedule, date, isDone); //이 3개를 쌍으로 data에 넣음
//                                                newArrayList.add(0, data);

//if (!newArrayList.equals(scheduleList)) {
//        scheduleList = newArrayList;
//        }
//        Log.e(TAG, scheduleList.toString());
//        //adapter = new iCalendarAdapter(rootView.getContext(), scheduleList);
//        listview.setAdapter(adapter);