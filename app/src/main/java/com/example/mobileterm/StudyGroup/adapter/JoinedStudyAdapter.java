package com.example.mobileterm.StudyGroup.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.vo.JoinedStudyVo;

import java.util.ArrayList;
import java.util.List;

public class JoinedStudyAdapter extends ArrayAdapter {

    private Context context;
    private List list;

    class ViewHolder{
        public TextView tv_joined_study_name;
        public TextView tv_joined_study_member;
        public TextView tv_joined_tag;
    }

    public JoinedStudyAdapter(Context context, ArrayList list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.custom_item_study_joined, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.tv_joined_study_name = (TextView) convertView.findViewById(R.id.tv_joined_study_name);
        viewHolder.tv_joined_study_member = (TextView) convertView.findViewById(R.id.tv_joined_study_member);
        viewHolder.tv_joined_tag = (TextView) convertView.findViewById(R.id.tv_joined_tag);

        final JoinedStudyVo joinedStudy = (JoinedStudyVo) list.get(position);
        viewHolder.tv_joined_study_name.setText(joinedStudy.getStudyName());
        viewHolder.tv_joined_study_member.setText(joinedStudy.getStudyCapacity());
        viewHolder.tv_joined_tag.setText(joinedStudy.getTags());
        viewHolder.tv_joined_study_name.setTag(joinedStudy.getStudyName());

        return convertView;
    }
}
