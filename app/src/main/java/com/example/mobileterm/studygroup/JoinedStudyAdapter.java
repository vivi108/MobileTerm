package com.example.mobileterm.studygroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mobileterm.R;

import java.util.ArrayList;

public class JoinedStudyAdapter extends BaseAdapter {
    private ArrayList<JoinedStudyItem> customList = new ArrayList<>();

    // the number of item which showed in ListView
    @Override
    public int getCount() {
        return customList.size();
    }

    // one item
    @Override
    public Object getItem(int position) {
        return customList.get(position);
    }

    // id of item : to distinct item, use position
    @Override
    public long getItemId(int position) {
        return position;
    }

    // show the item
    @Override
    public View getView(int position, View v, ViewGroup viewGroup) {
        CustomViewHolder holder;
        if(v == null){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_item_study_joined, null, false);
            holder = new CustomViewHolder();
            holder.joined_study_name = (TextView) v.findViewById(R.id.tv_joined_study_name);
            holder.joined_study_member = (TextView) v.findViewById(R.id.tv_joined_study_member);
            int[] tvId = {R.id.tv_joined_tag0, R.id.tv_joined_tag1, R.id.tv_joined_tag2, R.id.tv_joined_tag3, R.id.tv_joined_tag4};
            for(int i = 0; i < 5; i++){
                holder.tag[i] = (TextView) v.findViewById(tvId[i]);
            }
            v.setTag(holder);
        }
        else {
            holder = (CustomViewHolder) v.getTag();
        }

        JoinedStudyItem item = customList.get(position);

        holder.joined_study_name.setText(item.getJoined_study_name());
        holder.joined_study_member.setText(item.getJoined_study_member());
        for(int i = 0; i < 5; i++){
            holder.tag[i].setText(item.getTag(i));
        }

        return v;
    }

    class CustomViewHolder {
        TextView joined_study_name;
        TextView joined_study_member;
        TextView[] tag = new TextView[5];
        // 데이터가 들어가지 않아서 화면이 안 보이는 걸 수도 있음 log 찍어서 확인해보기
    }

    // add data on ArrayList at Adapter in StudyMainActivity
    public void addItem(JoinedStudyItem item){
        customList.add(item);
    }
}
