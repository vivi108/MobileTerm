package com.example.mobileterm.StudyGroup;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.ChattingActivity.ChattingActivity;
import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.adapter.PostAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudyGroupFragment extends Fragment {
    ImageButton btn_studygroup_back, btn_studygroup_setting, btn_studygroup_write_post;
    TextView tv_studygroup_title;
    ListView lv_studygroup_posts;
    ArrayList<StudyPostInfo> arrayList;
    private PostAdapter adapter;
    String title;
    String myNickname;
    MainActivity activity;
    FirebaseFirestore db ;
    Dialog settingDialog ;
    Dialog gScheduleDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_study_studygroup, container, false);
        activity = (MainActivity) getActivity();
        title = activity.sendStudyTitle();
        myNickname = activity.sendUserNickname();
        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<StudyPostInfo>();
        settingDialog = new Dialog(getActivity());
        settingDialog.setContentView(R.layout.activity_study_setting);
        gScheduleDialog = new Dialog(getActivity());
        gScheduleDialog.setContentView(R.layout.dialog_gschedule);
//        btn_studygroup_back = rootView.findViewById(R.id.btn_studygroup_back);
        btn_studygroup_setting = rootView.findViewById(R.id.btn_studygroup_setting);
        btn_studygroup_write_post = rootView.findViewById(R.id.btn_studygroup_write_post);

        tv_studygroup_title = rootView.findViewById(R.id.tv_studygroup_title);
        lv_studygroup_posts = rootView.findViewById(R.id.lv_studygroup_posts);

        tv_studygroup_title.setText(title);

        db.collection(title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot document : querySnapshot) {
                        if (document.exists()) {
                            StudyPostInfo temp = document.toObject(StudyPostInfo.class);
                            arrayList.add(temp);
                        }
                    }
                    adapter = new PostAdapter(rootView.getContext(), arrayList);
                    lv_studygroup_posts.setAdapter(adapter);

                    lv_studygroup_posts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            activity.onFragmentChanged(i+100, adapter.getItem(i).getWrittenTime()+" "+adapter.getItem(i).getTitle());
                        }
                    });
                }
            }
        });

        btn_studygroup_write_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onFragmentChanged(400);
            }
        });

        btn_studygroup_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingDialog.show();
                Button btn_study_setting_groupCalendar = settingDialog.findViewById(R.id.btn_study_setting_groupCalender);
                Button btn_study_chatting = settingDialog.findViewById(R.id.btn_study_chatting);
                btn_study_chatting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ChattingActivity.class);
                        intent.putExtra("nickname",myNickname);
                        intent.putExtra("studyName",title);
                        startActivity(intent);
                        settingDialog.dismiss();
                    }
                });

                btn_study_setting_groupCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gScheduleDialog.show();
                        EditText gscheduleTitleEditText = gScheduleDialog.findViewById(R.id.gscheduleTitleEditText);
                        EditText gschedulePlaceEditText = gScheduleDialog.findViewById(R.id.gschedulePlaceEditText);
                        EditText gscheduleTimeEditText = gScheduleDialog.findViewById(R.id.gscheduleTimeEditText);
                        EditText gscheduleDayEditText = gScheduleDialog.findViewById(R.id.gscheduleDayEditText);

                        ImageButton buttonEndGschedule = gScheduleDialog.findViewById(R.id.buttonEndGschedule);

                        buttonEndGschedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GScheduleInfo newSchedule = new GScheduleInfo(title, gscheduleTitleEditText.getText().toString(), gschedulePlaceEditText.getText().toString(), gscheduleTimeEditText.getText().toString(), gscheduleDayEditText.getText().toString(), title+gscheduleTitleEditText.getText().toString());

                                db.collection("GSchedule").document(title+gscheduleTitleEditText.getText().toString()).set(newSchedule).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("그룹 스케줄 추가","성공");
                                        newSchedule.setDocA(title+gscheduleTitleEditText.getText().toString());
                                        activity.onFragmentChanged(1,title);
                                        settingDialog.dismiss();
                                        gScheduleDialog.dismiss();
                                    }
                                });
                            }
                        });


                    }
                });
            }
        });


        return rootView;
    }
}
