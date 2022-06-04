package com.example.mobileterm.Calendar;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.GScheduleInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;

public class gCalendarFragment extends Fragment {
    public String str = null;
    MaterialCalendarView calendarView;
    public Button add_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;
    public RadioGroup ctype;
    public RadioButton radIndividual, radGroup;
    public CheckBox isDone;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private gCalendarFragment gCalendarFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser curUser;
    private HashMap<String, ArrayList<GScheduleInfo>> dateTable = new HashMap<String, ArrayList<GScheduleInfo>>();
    ArrayList<String> joinedStudy;
    ArrayList<com.example.mobileterm.StudyGroup.GScheduleInfo> gSchedules;
    MainActivity activity;
    String myNickname;
    Dialog dialogShow;
    private ListView listview;
    private gCalendarAdapter adapter;
    String TAG = "gCalendarFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup main_frame_layout, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_g_calendar, main_frame_layout, false);
        activity = (MainActivity) getActivity();
        myNickname = activity.sendUserNickname();
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

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();

        dialogShow = new Dialog(getActivity());
        dialogShow.setContentView(R.layout.dialog_show);

        //현재 들어와있는 그룹원만의 그룹스케줄
//        CollectionReference docref = db.collection("GSchedule");

        //위 라디오버튼
        ctype.check(radGroup.getId());
        radIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctype.check(radIndividual.getId());
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
                Toast.makeText(view.getContext(), "개인 캘린더입니다", Toast.LENGTH_SHORT).show();
//                ctype.check(radIndividual.getId());
//                int radioId = ctype.getCheckedRadioButtonId();
//                if(radIndividual.getId()==radioId) {
//                    MainActivity activity = (MainActivity) getActivity();
//                    activity.onFragmentChanged(0);
//                }
            }
        });


        // 날짜 클릭할 때 일어날 일들
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                diaryTextView.setVisibility(View.VISIBLE);
                //save_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.VISIBLE);
                //add_Btn.setVisibility(View.VISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", date.getYear(), date.getMonth() + 1, date.getDay())); //중간 일정 보여주는 텍스트뷰
                ArrayList<GScheduleInfo> newArrayList = new ArrayList<GScheduleInfo>();

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
                    dateTable.put(calendarDate, new ArrayList<GScheduleInfo>());
                    System.out.println("139" + dateTable);
                }

                joinedStudy = new ArrayList<String>();
                gSchedules = new ArrayList<com.example.mobileterm.StudyGroup.GScheduleInfo>();
                db.collection("Study").
                        get().
                        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                      if(task.isSuccessful()){
                                                          QuerySnapshot querySnapshot = task.getResult();
                                                          for(DocumentSnapshot document : querySnapshot){
                                                              String studyName = (String) document.getData().get("studyName");
                                                              ArrayList<String> memberList = (ArrayList<String>) document.getData().get("memberList");
                                                              if (memberList.contains(myNickname)){
                                                                  joinedStudy.add(studyName);
                                                              }

                                                          }
                                                          db.collection("GSchedule").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                  if (task.isSuccessful()) {
                                                                      QuerySnapshot querySnapshot1 = task.getResult();
                                                                      for (DocumentSnapshot doc : querySnapshot1) {
                                                                          String studyName = (String) doc.getData().get("studyName");
                                                                          String studyDay = (String) doc.getData().get("meetingDay");
                                                                          if (joinedStudy.contains(studyName) && studyDay.equals(calendarDate)) {
                                                                              com.example.mobileterm.StudyGroup.GScheduleInfo temp = doc.toObject(com.example.mobileterm.StudyGroup.GScheduleInfo.class);
                                                                              gSchedules.add(temp);
                                                                              Log.d(TAG,temp.getStudyName()+" "+temp.getScheduleName());
                                                                          }
                                                                      }
                                                                      dateTable.put(calendarDate, gSchedules);
                                                                      adapter = new gCalendarAdapter(dateTable.get(calendarDate), dialogShow); //어댑터에 이 날짜 해당 데이터 다 넘겨줌
                                                                      listview.setAdapter(adapter);
                                                                  }
                                                              }
                                                          });
                                                      }
                                                  }
                                              });


                                //클릭한 날짜의 일정을 파베에서 불러와서 그 날에 해당하는 일정 리스트뷰 어댑터에 넣기
//                docref
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        if (document.exists()) {
//                                            if (String.valueOf(document.getData().get("meetingDay")).equals(calendarDate))  //선택 날짜와 파베 날짜가 동일할 경우
//                                            {
//                                                System.out.println("클릭날짜 : " + calendarDate);
//
//                                                String study = ((String) document.getData().get("studyName"));
//                                                String schedule = ((String) document.getData().get("scheduleName")); //그 일정을 가져오겠다
//                                                String date = ((String) document.getData().get("meetingDay"));
//                                                String time = ((String) document.getData().get("meetingTime"));
//                                                String docA = ((String) document.getId());
//                                                String place = ((String) document.getData().get("place"));
//
//                                                gCalendarItem data = new gCalendarItem(schedule, date, time, study, place );
//                                                newArrayList.add(0, data);
//                                            }
//                                        }
//                                    }
//                                    try
//                                    { //키값 : 날짜 + 밸류값 : 어레이리스트(일정, 날짜, isdone)
//                                        if(!dateTable.get(calendarDate).equals(newArrayList))
//                                        {
//                                            dateTable.put(calendarDate, newArrayList); //날짜를 키값으로 밸류에 리스트 하나(newArr) 추가
//                                        }
//                                    }catch (Exception e){
//                                        dateTable.put(calendarDate, newArrayList); //날짜를 키값으로 밸류에 리스트 하나(newArr) 추가
//                                    }
//
//                                    for (gCalendarItem itr : dateTable.get(calendarDate)){ //이 날짜에 어떤 어레이리스트 있나 출력
//                                        Log.d(TAG,"item : "+itr.getDate()+" "+itr.getSchedule()+ " " + itr.getTime());
//                                    }
//
//                                    dialogShow = new Dialog(getActivity());
//                                    adapter = new gCalendarAdapter(dateTable.get(calendarDate), dialogShow); //어댑터에 이 날짜 해당 데이터 다 넘겨줌
//                                    listview.setAdapter(adapter); //리스트뷰에 보이도록 함
//                                }
//                            }
//                        });


//                add_Btn.setOnClickListener(new View.OnClickListener() {
//
//                    String Schedule;
//                    String Date;
//                    String time, atime;
//                    String docA;
//                    String place;
//
//                    @Override //추가 버튼 클릭 시 - 그룹일정 저장
//                    public void onClick(View view) {
//                         View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit, null);
//                         EditText placeEdit = (EditText)dialogView.findViewById(R.id.placeEdit);
//                         EditText timeEdit = (EditText)dialogView.findViewById(R.id.timeEdit);
//                         EditText scheduleEdit = (EditText)dialogView.findViewById(R.id.scheduleEdit);
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogTheme));
//                        builder.setView(dialogView);
//
//
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int pos) {
//                                        Schedule = scheduleEdit.getText().toString();
//                                        atime = timeEdit.getText().toString();
//                                        time = atime.substring(0,2) + ":" + atime.substring(2,4);
//                                        place = placeEdit.getText().toString();
//                                        Date = calendarDate; //일정 쓴 날짜겠지
//                                        docA = Date +" "+ Schedule;
//                                        Log.d("gfrag 1 - ", " schdule:" + Schedule +" time:"+ time + " place:"+ place);
//                                        gCalendarItem newItem = new gCalendarItem(Schedule, Date, time, docA, place);
//
//                                        docref
//                                                .document(Date +" "+ Schedule)
//                                                .set(newItem).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Log.d("gfrag 2 - ", " schdule:" + Schedule +" time:"+ time + " place:"+ place);
//                                                }
//                                            }
//                                        });
//                                        dateTable.get(calendarDate).add(newItem);
//                                        adapter.addItem(newItem);
//                                    }
//                                });
//
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//
//                        //저장 버튼을 클릭 한 후 - 저장버튼과 edittext 안보이고 / 수정, 삭제 버튼, 일정 보여주는 거 보이게함
//                        save_Btn.setVisibility(View.INVISIBLE);
//                        add_Btn.setVisibility(View.VISIBLE);
//                        contextEditText.setVisibility(View.INVISIBLE);
//                        listview.setVisibility(View.VISIBLE);
//                    }
//                });
            }
        });
        return rootView;
    }
}

