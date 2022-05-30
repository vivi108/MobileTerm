package com.example.mobileterm.StudyGroup.popup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.StudyFindActivity;
import com.example.mobileterm.StudyGroup.StudyGroupActivity;
import com.example.mobileterm.StudyGroup.StudyMakeActivity;

public class openedPopup extends Fragment implements View.OnClickListener {

    private ViewGroup rootView;
    private ImageButton btn_join_study_cancel;
    private Button btn_join_open_study, btn_join_open_study_no;
    private Intent intent;
    String studyName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.popup_study_join_open_study, container, false);

        btn_join_study_cancel = rootView.findViewById(R.id.btn_join_study_cancel);
        btn_join_open_study = rootView.findViewById(R.id.btn_join_open_study);
        btn_join_open_study_no = rootView.findViewById(R.id.btn_join_open_study_no);

        Bundle bundle = getArguments();
        studyName = bundle.getString("StudyName");

        btn_join_open_study.setOnClickListener(this);
        btn_join_study_cancel.setOnClickListener(this);
        btn_join_open_study_no.setOnClickListener(this);



        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_join_open_study:
                intent = new Intent(getActivity(), StudyGroupActivity.class);
                intent.putExtra("JoinedStudy", studyName);
                // 파이어베이스에 참여 스터디 부분에 추가
                startActivity(intent);
            case R.id.btn_join_study_cancel:
            case R.id.btn_join_open_study_no:
                getActivity().getSupportFragmentManager().beginTransaction().remove(openedPopup.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
