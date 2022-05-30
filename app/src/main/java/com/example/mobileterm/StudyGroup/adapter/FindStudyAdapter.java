package com.example.mobileterm.StudyGroup.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.vo.FindStudyVo;

import java.util.ArrayList;
import java.util.List;

public class FindStudyAdapter  extends ArrayAdapter {

    private Context context;
    private List list;

    class ViewHolder{
        public TextView tv_find_study_name;
        public TextView tv_find_study_member;
        public TextView tv_find_study_tag;
        public TextView tv_find_study_description;
        public ImageView iv_find_study_locked;
        public ImageButton btn_find_study_liked;
    }

    public FindStudyAdapter(Context context, ArrayList list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        String ID;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.custom_item_study_find, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.tv_find_study_name = convertView.findViewById(R.id.tv_find_study_name);
        viewHolder.tv_find_study_member = convertView.findViewById(R.id.tv_find_study_member);
        viewHolder.tv_find_study_tag = convertView.findViewById(R.id.tv_find_study_tag);
        viewHolder.tv_find_study_description = convertView.findViewById(R.id.tv_find_study_description);
        viewHolder.iv_find_study_locked = convertView.findViewById(R.id.iv_find_study_locked);
        viewHolder.btn_find_study_liked = convertView.findViewById(R.id.btn_find_study_liked);

        viewHolder.btn_find_study_liked.setFocusable(false);

        final FindStudyVo findStudy = (FindStudyVo) list.get(position);
        viewHolder.tv_find_study_name.setText(findStudy.getStudyName());
        viewHolder.tv_find_study_member.setText(findStudy.getStudyCapacity());
        viewHolder.tv_find_study_tag.setText(findStudy.getTags());
        viewHolder.tv_find_study_description.setText(findStudy.getDescription());
        if(findStudy.getIsOpened() == "open"){
            viewHolder.iv_find_study_locked.setVisibility(View.GONE);
        }
        else if(findStudy.getIsOpened() == "closed"){
            viewHolder.iv_find_study_locked.setVisibility(View.VISIBLE);
        }

        if(findStudy.getIsLiked() == "false"){
            viewHolder.btn_find_study_liked.setImageResource(R.drawable.ic_heart);
        }
        else if(findStudy.getIsLiked() == "true"){
            viewHolder.btn_find_study_liked.setImageResource(R.drawable.ic_heart_filled);
        }

        viewHolder.btn_find_study_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(findStudy.getIsLiked() == "true"){
                    viewHolder.btn_find_study_liked.setImageResource(R.drawable.ic_heart);
                    findStudy.setIsLiked("false");
                }
                else if(findStudy.getIsLiked() == "false"){
                    viewHolder.btn_find_study_liked.setImageResource(R.drawable.ic_heart_filled);
                    findStudy.setIsLiked("true");
                }
            }
        });

        ID = findStudy.getIsOpened() + " " + findStudy.getStudyName();

        viewHolder.tv_find_study_name.setTag(ID);

        return convertView;
    }
}