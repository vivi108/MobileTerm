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
    ArrayList<iCalendarItem> scheduleList = new ArrayList<iCalendarItem>();
    ListView listview;
    iCalendarAdapter adapter;

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
        CollectionReference docref = db.collection("Users").document(curUser.getUid()).collection("iSchedule");
        ArrayList<iCalendarItem> newArrayList = new ArrayList<iCalendarItem>();


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
                //1
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);


                System.out.println("클릭날짜 : " + String.valueOf(date.getYear()) + "-" + String.valueOf(date.getMonth() + 1) + "-" + String.valueOf(date.getDay()));

                //클릭한 날짜의 일정을 써놓음
                docref
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            if (String.valueOf(document.getData().get("date")) //선택 날짜와 파베 날짜가 동일할 경우
                                                    .equals(String.valueOf(date.getYear()) + "-" + String.valueOf(date.getMonth() + 1) + "-" + String.valueOf(date.getDay()))) {
                                                String schedule = ((String) document.getData().get("str")); //그 일정을 가져오겠다
                                                String isDone = String.valueOf(document.getData().get("isDone"));
                                                String date = ((String) document.getData().get("date"));
                                                Log.e(TAG, "schedule : "+schedule);
                                                iCalendarItem data = new iCalendarItem(schedule, date, isDone); //이 3개를 쌍으로 data에 넣음
                                                newArrayList.add(0,data);
                                            }
                                        }
                                    }
                                    if (!newArrayList.equals(scheduleList)){
                                        scheduleList = newArrayList;
                                    }
                                    Log.e(TAG,scheduleList.toString());
                                    adapter = new iCalendarAdapter(rootView.getContext(), scheduleList);
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


                //추가 버튼으로 짜봄
                add_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //2
                        //추가 눌리면 edit과 저장버튼만 보이게 해주는거..
                        //이전에 있던 일정이 edit에선 안보였다가 txtview에선 추가되어 보이도록
                        contextEditText.setVisibility(View.VISIBLE);
                        isDone.setVisibility(View.VISIBLE);
                        textView2.setVisibility(View.INVISIBLE);
                        contextEditText.setText("");

                        save_Btn.setVisibility(View.VISIBLE);
                        add_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                    }
                });


                //삭제 버튼 눌리면
                del_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //1
                        //이건 나중에 리스트 형태로 바꿨을때 구현하기로 - 어떤 항목 삭제할건지 2번 화면 띄우기
                        textView2.setVisibility(View.VISIBLE);
                        contextEditText.setText("");
                        contextEditText.setVisibility(View.VISIBLE);
                        isDone.setVisibility(View.VISIBLE);
                        save_Btn.setVisibility(View.VISIBLE);
                        add_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                        //removeDiary(readDay);
                    }
                });


                //저장 버튼 클릭 시
                save_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        str = contextEditText.getText().toString(); //일정을 쓰면 그 내용이 str로 할당

                        //컬렉션에 넣을 준비
                        String Date = String.valueOf(date.getYear()) + "-" + String.valueOf(date.getMonth() + 1) + "-" + String.valueOf(date.getDay());
                        String IsDone = String.valueOf(isDone.isChecked());
                        String Context = str;

                        //컬렉션에 넣음 - user 안 iSchedule 컬렉션에
                        db.collection("Users").document(curUser.getUid()).collection("iSchedule").document().set(new iCalendarItem(Context, Date, IsDone)).addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "successfully added");
                                }
                            }
                        });

                        //이제 파베에서 불러옴
                        //불러와서 해당날짜 일정을 txt2에 써놓음
                        db.collection("Users").document(curUser.getUid()).collection("iSchedule")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if (document.exists()) {
                                                    if (String.valueOf(document.getData().get("date")) //선택 날짜와 파베 날짜가 동일할 경우
                                                            .equals(String.valueOf(date.getYear()) + "-" + String.valueOf(date.getMonth() + 1) + "-" + String.valueOf(date.getDay()))) {
                                                        String context = (String) document.getData().get("str"); //그 일정을 가져오겠다
                                                        textView2.setText(context); //그 일정을 txt2에 넣겠다
                                                        System.out.println("This" + String.valueOf(document.getData().get("date"))+"!!"+context);
                                                        String isd = String.valueOf(document.getData().get("isDone"));
                                                        if(isd.equals("true")) //이거 할라면 수정기능 만들어야함
                                                            textView2.append(" over");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });

                        // 1
                        //저장 버튼을 클릭 한 후 - 저장버튼과 edittext 안보이고 / 수정, 삭제 버튼, 일정 보여주는 거 보이게함
                        save_Btn.setVisibility(View.INVISIBLE);
                        add_Btn.setVisibility(View.VISIBLE);
                        del_Btn.setVisibility(View.VISIBLE);
                        contextEditText.setVisibility(View.INVISIBLE);
                        isDone.setVisibility(View.INVISIBLE);
                        textView2.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        return rootView;
    }


}


//    public void checkDay(int cYear, int cMonth, int cDay)
//    {
//        try
//        {
//            //받아온 연월일을 readday라는 스트링에 저장
//            readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
//            System.out.println(readDay);
//            //파일 내용 읽으려나 봄
//            FileInputStream fis;
//            //readday 스트링을 읽겠다
//            fis = getActivity().openFileInput(readDay);
//
//            byte[] fileData = new byte[fis.available()];
//            //readday를 무튼 fileData에 넣었음
//            fis.read(fileData);
//            fis.close();
//
//            //fileData의 연월일 정보를 str에 넣음
//            str = new String(fileData);
//                // 무튼 위 코드들은 str.txt에 일정을 넣은거임
//
//
//
//            //2222222222222
//            //일정 수정 또는 삽입 후 일정 보여주는 그 시점
//            contextEditText.setVisibility(View.INVISIBLE);
//            textView2.setVisibility(View.VISIBLE);
//            textView2.setText(str);               //입력한 str을 일정으로 보여줌
//            save_Btn.setVisibility(View.INVISIBLE);
//            cha_Btn.setVisibility(View.VISIBLE);
//            del_Btn.setVisibility(View.VISIBLE);
//
//            //수정 버튼 눌리면
//            cha_Btn.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    //1111111111111111
//                    //원래 있던 일정(str)이 edittext에 그대로 나오고
//                    //마지막에 edit의 스트링을 text2에 넣어줌
//                    //마찬가지로 수정 눌리면 edit과 저장버튼만 보이게 해주는거..
//                    contextEditText.setVisibility(View.VISIBLE);
//                    textView2.setVisibility(View.INVISIBLE);
//                    contextEditText.setText(str);
//
//                    save_Btn.setVisibility(View.VISIBLE);
//                    cha_Btn.setVisibility(View.INVISIBLE);
//                    del_Btn.setVisibility(View.INVISIBLE);
//                    textView2.setText(contextEditText.getText());
//                }
//
//            });
//            //삭제 버튼 눌리면
//            del_Btn.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    //11111111111111
//                    //text2 안보이고 edit이 비게 되고,
//                    //edit과 저장버튼만 보이게됨
//                    //readDay의 스트링은 삭제됨
//                    textView2.setVisibility(View.INVISIBLE);
//                    contextEditText.setText("");
//                    contextEditText.setVisibility(View.VISIBLE);
//                    save_Btn.setVisibility(View.VISIBLE);
//                    cha_Btn.setVisibility(View.INVISIBLE);
//                    del_Btn.setVisibility(View.INVISIBLE);
//                    removeDiary(readDay);
//                }
//            });
//
//            //text2가 비어있다면 그냥 바로 edit과 저장버튼만 보이도록
//            if (textView2.getText() == null)
//            {
//                textView2.setVisibility(View.INVISIBLE);
//                diaryTextView.setVisibility(View.VISIBLE);
//                save_Btn.setVisibility(View.VISIBLE);
//                cha_Btn.setVisibility(View.INVISIBLE);
//                del_Btn.setVisibility(View.INVISIBLE);
//                contextEditText.setVisibility(View.VISIBLE);
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

//    @SuppressLint("WrongConstant")
//    public void removeDiary(String readDay)
//    {
//        FileOutputStream fos;
//        try
//        {
//            fos = getActivity().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
//            String content = "";
//            fos.write((content).getBytes());
//            fos.close();
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @SuppressLint("WrongConstant")
//    public void saveDiary(String readDay)
//    {
//        FileOutputStream fos;
//        try
//        {
//            fos = getActivity().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
//            String content = contextEditText.getText().toString();
//            fos.write((content).getBytes());
//            fos.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//}


//원본 save 버튼
////저장 버튼 클릭 시
//                save_Btn.setOnClickListener(new View.OnClickListener()
//                        {
//@Override
//public void onClick(View view)
//        {
//        saveDiary(readDay); //일정 저장???
//        str = contextEditText.getText().toString(); //일정을 쓰면 그 내용이 str로 할당
//        textView2.setText(str); //t2(일정 보여주는)에 str 저장
//
//        //여기서 파베에 str 추가
//
//        // 1
//        //저장 버튼을 클릭 한 후 - 저장버튼과 edittext 안보이고 / 수정, 삭제 버튼, 일정 보여주는 거 보이게함
//        save_Btn.setVisibility(View.INVISIBLE);
//        cha_Btn.setVisibility(View.VISIBLE);
//        del_Btn.setVisibility(View.VISIBLE);
//        contextEditText.setVisibility(View.INVISIBLE);
//        textView2.setVisibility(View.VISIBLE);
//
////                        //파베에 add 함 (쓰기)
////                        Map<String, Object> scd = new HashMap<>();
////                        scd.put("date", String.valueOf(date.getYear()) +"-"+ String.valueOf(date.getMonth()+1) +"-"+ String.valueOf(date.getDay()));
////                        scd.put("isDone", false);
////                        scd.put("context", str);
////
////                        db.collection("Schedule")
////                                .add(scd);
////
////
////                        db.collection("Schedule")
////                                .get()
////                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////                                    @Override
////                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                                        if(task.isSuccessful()){
////                                            for(QueryDocumentSnapshot document : task.getResult()){
////                                                if (document.exists()) {
////                                                    String date = String.valueOf(document.getData().get("date"));
////                                                    String context = (String) document.getData().get("context");
////                                                    //String isDone =  (String) document.getData().get("isDone");
////                                                    System.out.println(date + " " + context);
////                                                }
////                                            }
////                                        }
////                                    }
////                                });
//
//
//
//
//        }
//        });
//        }
//        });
//
//        return rootView;
//        }

////수정 버튼 눌리면 (추가 버튼으로 코드짜봄)
//                cha_Btn.setOnClickListener(new View.OnClickListener()
//                        {
//@Override
//public void onClick(View view)
//        {
//        //2
//        //원래 있던 일정(str)이 edittext에 그대로 나오고
//        //마찬가지로 수정 눌리면 edit과 저장버튼만 보이게 해주는거..
//        contextEditText.setVisibility(View.VISIBLE);
//        textView2.setVisibility(View.INVISIBLE);
//        contextEditText.setText(str);
//
//        save_Btn.setVisibility(View.VISIBLE);
//        cha_Btn.setVisibility(View.INVISIBLE);
//        del_Btn.setVisibility(View.INVISIBLE);
//        textView2.setText(contextEditText.getText()); //마지막에 edit의 스트링을 text2에 넣어줌
//        }
//        });

//날짜 체크 후 일정 삽입 or 수정 작업
//checkDay(date.getYear(), date.getMonth() + 1, date.getDay());

//빨간 점 찍기 -------------
//                calendarView.setSelectedDate(CalendarDay.today());
//                calendarView.addDecorator(new EventDecorator(Color.RED, Collections.singleton(CalendarDay.today())));

// 더블 컬렉션에 넣는 함수
//    public void addScheduleEachPerson(String schedule){ //document(did).set(new LikedBoardItem(title, did))
//        db.collection("Users").document(curUser.getUid()).collection("iSchedule").document().set(new iCalendarItem(schedule)).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Log.d(TAG, "successfully added");
//                }
//            }
//        });
//    }


//파베에 str 저장
//여기서 파베에 str 추가
//                        Map<String, Object> scd = new HashMap<>();
//                        scd.put("date", String.valueOf(date.getYear()) + "-" + String.valueOf(date.getMonth() + 1) + "-" + String.valueOf(date.getDay()));
//                        scd.put("isDone",isDone.isChecked() );
//                        scd.put("context", str);
//                        db.collection("Schedule")
//                                .add(scd);
//



////클릭한 날짜의 일정을 txt2에 써놓음
//                db.collection("Users").document(curUser.getUid()).collection("iSchedule")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//@Override
//public void onComplete(@NonNull Task<QuerySnapshot> task) {
//        if (task.isSuccessful()) {
//        for (QueryDocumentSnapshot document : task.getResult()) {
//        if (document.exists()) {
//        if (String.valueOf(document.getData().get("date")) //선택 날짜와 파베 날짜가 동일할 경우
//        .equals(String.valueOf(date.getYear()) + "-" + String.valueOf(date.getMonth() + 1) + "-" + String.valueOf(date.getDay()))) {
//        String context = ((String) document.getData().get("str")); //그 일정을 가져오겠다
//        textView2.setText(context); //그 일정을 txt2에 넣겠다
//        System.out.println("This" + String.valueOf(document.getData().get("date"))+"!!"+context);
//        String isd = String.valueOf(document.getData().get("isDone"));
//        if(isd.equals("true")) //이거 할라면 수정기능 만들어야함
//        textView2.append(" over");
//        }
//        else textView2.setText("");
//        }
//        }
//        }
//        }
//        });