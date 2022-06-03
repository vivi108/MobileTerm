package com.example.mobileterm.StudyGroup.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.StudyInfo;
import com.example.mobileterm.StudyGroup.vo.FindStudyVo;

import java.util.ArrayList;
import java.util.List;

public class FindStudyAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<StudyInfo> dataList;
    private ArrayList<StudyInfo> itemList;
    private ArrayList<String> likedStudies;

    private String TAG = "find study adapter";



    public FindStudyAdapter(Context context, ArrayList<StudyInfo> list, ArrayList<String> likedStudies){
        this.context = context;
        this.dataList = new ArrayList<StudyInfo>();
        this.dataList.addAll(list);
        this.likedStudies = new ArrayList<String>();
        this.likedStudies.addAll(likedStudies);
        Log.d(TAG, "list size"+dataList.size());
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public StudyInfo getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        String ID;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_item_study_find, parent, false);
        }else{
            View view = new View(context);
            view = convertView;
        }
        TextView tv_find_study_name = convertView.findViewById(R.id.tv_find_study_name);
        TextView tv_find_study_member = convertView.findViewById(R.id.tv_find_study_member);
        TextView tv_find_study_tag = convertView.findViewById(R.id.tv_find_study_tag);
        TextView tv_find_study_description = convertView.findViewById(R.id.tv_find_study_description);
        ImageView iv_find_study_locked = convertView.findViewById(R.id.iv_find_study_locked);
        ImageButton btn_find_study_liked = convertView.findViewById(R.id.btn_find_study_liked);



        btn_find_study_liked.setFocusable(false);

        StudyInfo findStudy = (StudyInfo) dataList.get(position);
        tv_find_study_name.setText(findStudy.getStudyName());
        tv_find_study_member.setText(Long.toString(findStudy.getMaxNumPeople()));
        tv_find_study_tag.setText(findStudy.getTags());
        tv_find_study_description.setText(findStudy.getDescription());
        if(findStudy.isOpened()){
            iv_find_study_locked.setVisibility(View.GONE);
        }
        else {
            iv_find_study_locked.setVisibility(View.VISIBLE);
        }
        btn_find_study_liked.setImageResource(R.drawable.ic_heart);
        tv_find_study_name.setTag("xptmxm");
        btn_find_study_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,position+" liked clicked");
            }
        });

//        if(findStudy.getIsLiked() == "false"){
//            viewHolder.btn_find_study_liked.setImageResource(R.drawable.ic_heart);
//        }
//        else if(findStudy.getIsLiked() == "true"){
//            viewHolder.btn_find_study_liked.setImageResource(R.drawable.ic_heart_filled);
//        }

//        viewHolder.btn_find_study_liked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(findStudy.getIsLiked() == "true"){
//                    viewHolder.btn_find_study_liked.setImageResource(R.drawable.ic_heart);
//                    findStudy.setIsLiked("false");
//                }
//                else if(findStudy.getIsLiked() == "false"){
//                    viewHolder.btn_find_study_liked.setImageResource(R.drawable.ic_heart_filled);
//                    findStudy.setIsLiked("true");
//                }
//            }
//        });
//
//        ID = findStudy.getIsOpened() + " " + findStudy.getStudyName();

//        viewHolder.tv_find_study_name.setTag(ID);

        return convertView;
    }
}