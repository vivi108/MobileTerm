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
    String groupSchedule;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup main_frame_layout, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_g_calendar, main_frame_layout, false);
        activity = (MainActivity) getActivity();
        myNickname = activity.sendUserNickname();
        groupSchedule = activity.sendGroupSchedule();
        calendarView = rootView.findViewById(R.id.calendarView);
        diaryTextView = rootView.findViewById(R.id.diaryTextView); //????????? ?????? ?????? ????????????
        save_Btn = rootView.findViewById(R.id.save_Btn);
        add_Btn = rootView.findViewById(R.id.add_Btn);
        textView2 = rootView.findViewById(R.id.textView2); //?????? ????????? ?????? ?????? ??? ?????? ???????????? ???????????? ???
        contextEditText = rootView.findViewById(R.id.contextEditText); //?????? ?????? ?????? ???????????? ???
        ctype = rootView.findViewById(R.id.ctype);
        radIndividual = rootView.findViewById(R.id.radIndividual);
        radGroup = rootView.findViewById(R.id.radGroup);
        isDone = rootView.findViewById(R.id.isDone);
        listview = rootView.findViewById(R.id.list);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();

        dialogShow = new Dialog(getActivity());
        dialogShow.setContentView(R.layout.dialog_show);

        //?????? ??????????????? ??????????????? ???????????????
//        CollectionReference docref = db.collection("GSchedule");

        //??? ???????????????
//        ctype.check(radGroup.getId());
//        ctype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                MainActivity activity = (MainActivity) getActivity();
//                activity.onFragmentChanged(0);
////                Toast.makeText(view.getContext(), "?????? ??????????????????", Toast.LENGTH_SHORT).show();
//            }
//        });
        radIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctype.check(radGroup.getId());
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
                Toast.makeText(view.getContext(), "?????? ??????????????????", Toast.LENGTH_SHORT).show();
//                ctype.check(radIndividual.getId());
//                int radioId = ctype.getCheckedRadioButtonId();
//                if(radIndividual.getId()==radioId) {
//                    MainActivity activity = (MainActivity) getActivity();
//                    activity.onFragmentChanged(0);
//                }
            }
        });


        // ?????? ????????? ??? ????????? ??????
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                diaryTextView.setVisibility(View.VISIBLE);
                //save_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.VISIBLE);
                //add_Btn.setVisibility(View.VISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", date.getYear(), date.getMonth() + 1, date.getDay())); //?????? ?????? ???????????? ????????????
                ArrayList<GScheduleInfo> newArrayList = new ArrayList<GScheduleInfo>();

                String month = String.valueOf(date.getMonth() + 1);
                if(date.getMonth()+1 < 10)
                    month = "0".concat(month); //?????? ????????? ???????????? ?????? month ?????????
                String day = String.valueOf(date.getDay());
                if(date.getDay() < 10)
                    day = "0".concat(day); //?????? ????????? ???????????? ?????? day ?????????

                String calendarDate = String.valueOf(date.getYear()) + month + day; //???????????? ?????? ?????? ??? ????????? ?????????
                System.out.println("???????????? : " + calendarDate);


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
                                                          if (groupSchedule.equals("default")){
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
                                                                          adapter = new gCalendarAdapter(dateTable.get(calendarDate), dialogShow); //???????????? ??? ?????? ?????? ????????? ??? ?????????
                                                                          listview.setAdapter(adapter);
                                                                      }
                                                                  }
                                                              });
                                                          }else{
                                                              db.collection("GSchedule").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                  @Override
                                                                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                      if (task.isSuccessful()) {
                                                                          QuerySnapshot querySnapshot1 = task.getResult();
                                                                          for (DocumentSnapshot doc : querySnapshot1) {
                                                                              String studyName = (String) doc.getData().get("studyName");
                                                                              String studyDay = (String) doc.getData().get("meetingDay");
                                                                              if (joinedStudy.contains(studyName) && studyDay.equals(calendarDate) && studyName.equals(groupSchedule)) {
                                                                                  com.example.mobileterm.StudyGroup.GScheduleInfo temp = doc.toObject(com.example.mobileterm.StudyGroup.GScheduleInfo.class);
                                                                                  gSchedules.add(temp);
                                                                                  Log.d(TAG,temp.getStudyName()+" "+temp.getScheduleName());
                                                                              }
                                                                          }
                                                                          dateTable.put(calendarDate, gSchedules);
                                                                          adapter = new gCalendarAdapter(dateTable.get(calendarDate), dialogShow); //???????????? ??? ?????? ?????? ????????? ??? ?????????
                                                                          listview.setAdapter(adapter);
                                                                      }
                                                                  }
                                                              });
                                                          }
                                                      }
                                                  }
                                              });


                                //????????? ????????? ????????? ???????????? ???????????? ??? ?????? ???????????? ?????? ???????????? ???????????? ??????
//                docref
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        if (document.exists()) {
//                                            if (String.valueOf(document.getData().get("meetingDay")).equals(calendarDate))  //?????? ????????? ?????? ????????? ????????? ??????
//                                            {
//                                                System.out.println("???????????? : " + calendarDate);
//
//                                                String study = ((String) document.getData().get("studyName"));
//                                                String schedule = ((String) document.getData().get("scheduleName")); //??? ????????? ???????????????
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
//                                    { //?????? : ?????? + ????????? : ??????????????????(??????, ??????, isdone)
//                                        if(!dateTable.get(calendarDate).equals(newArrayList))
//                                        {
//                                            dateTable.put(calendarDate, newArrayList); //????????? ???????????? ????????? ????????? ??????(newArr) ??????
//                                        }
//                                    }catch (Exception e){
//                                        dateTable.put(calendarDate, newArrayList); //????????? ???????????? ????????? ????????? ??????(newArr) ??????
//                                    }
//
//                                    for (gCalendarItem itr : dateTable.get(calendarDate)){ //??? ????????? ?????? ?????????????????? ?????? ??????
//                                        Log.d(TAG,"item : "+itr.getDate()+" "+itr.getSchedule()+ " " + itr.getTime());
//                                    }
//
//                                    dialogShow = new Dialog(getActivity());
//                                    adapter = new gCalendarAdapter(dateTable.get(calendarDate), dialogShow); //???????????? ??? ?????? ?????? ????????? ??? ?????????
//                                    listview.setAdapter(adapter); //??????????????? ???????????? ???
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
//                    @Override //?????? ?????? ?????? ??? - ???????????? ??????
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
//                                        Date = calendarDate; //?????? ??? ????????????
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
//                        //?????? ????????? ?????? ??? ??? - ??????????????? edittext ???????????? / ??????, ?????? ??????, ?????? ???????????? ??? ????????????
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

