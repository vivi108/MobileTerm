package com.example.mobileterm.studygroup;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.R;

public class StudyMainActivity extends Fragment {
    private JoinedStudyAdapter adapter;
    private ListView lv_studies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_study_main);

        adapter = new JoinedStudyAdapter();
        lv_studies = (ListView) findViewById(R.id.lv_studies);

        setData();

        lv_studies.setAdapter(adapter);
    }

    private void setData() {
        String[] group_name = getResources().getStringArray(R.array.study_name);
        String[] group_member = getResources().getStringArray(R.array.study_members);
        String[] tags = getResources().getStringArray(R.array.tag);

        for (int i = 0; i < group_name.length; i++){
            JoinedStudyItem item = new JoinedStudyItem();
            item.setJoined_study_name(group_name[i]);
            item.setJoined_study_member(group_member[i]);
            String[] split_tags = tags[i].split(" ");
            for (int j = 0; j < 5; j++){
                item.setTag(split_tags[i], i);
            }

            adapter.addItem(item);
        }

    }
}
