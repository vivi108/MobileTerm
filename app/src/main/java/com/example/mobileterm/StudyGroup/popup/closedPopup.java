package com.example.mobileterm.StudyGroup.popup;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.StudyFindActivity;
import com.example.mobileterm.StudyGroup.StudyGroupActivity;
import com.example.mobileterm.StudyGroup.StudyMakeActivity;

public class closedPopup extends Fragment implements View.OnClickListener {

    private ViewGroup rootView;
    private ImageButton btn_join_study_cancel, btn_join_study_show_code;
    private EditText et_join_study_password;
    private Button btn_join_study_with_code;
    private Intent intent;
    String pw;
    String studyName;
    private int i = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.popup_study_join_closed_study, container, false);

        btn_join_study_cancel = rootView.findViewById(R.id.btn_join_study_cancel);
        btn_join_study_show_code = rootView.findViewById(R.id.btn_join_study_show_code);
        et_join_study_password = rootView.findViewById(R.id.et_join_study_password);
        btn_join_study_with_code = rootView.findViewById(R.id.btn_join_study_with_code);

        btn_join_study_cancel.setOnClickListener(this);
        btn_join_study_show_code.setOnClickListener(this);
        btn_join_study_with_code.setOnClickListener(this);

        Bundle bundle = getArguments();
        pw = bundle.getString("PW");
        studyName = bundle.getString("StudyName");

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_join_study_show_code:
                if(i == 0){
                    btn_join_study_show_code.setImageResource(R.drawable.ic_eye_opened);
                    et_join_study_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    i = 1;
                }
                else {
                    btn_join_study_show_code.setImageResource(R.drawable.ic_eye_crossed);
                    et_join_study_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    i = 0;
                }
                break;
            case R.id.btn_join_study_with_code:
                if(isMatched()){
                    intent = new Intent(getActivity(), StudyGroupActivity.class);
                    intent.putExtra("JoinedStudy", studyName);
                    // 파이어베이스에 참여 스터디 부분에 추가
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), "Password is not matched. Please try again.", Toast.LENGTH_SHORT).show();
                }
            case R.id.btn_join_study_cancel:
                et_join_study_password.setText("");
                getActivity().getSupportFragmentManager().beginTransaction().remove(closedPopup.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    private boolean isMatched() {
        return pw.equals(et_join_study_password.getText().toString());
    }
}
