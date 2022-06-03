package com.example.mobileterm.StudyGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.StudyPostInfo;
import com.example.mobileterm.StudyGroup.vo.PostVo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<StudyPostInfo> dataList;
    private ArrayList<StudyPostInfo> itemList;


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public StudyPostInfo getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public PostAdapter(Context context, ArrayList<StudyPostInfo> list){
        this.context = context;
        this.dataList = new ArrayList<StudyPostInfo>();
        this.dataList.addAll(list);
        this.itemList = new ArrayList<StudyPostInfo>();
        this.itemList.addAll(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String ID;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.custom_item_study_post, parent, false);
        }

        TextView tv_posted_user_name = convertView.findViewById(R.id.tv_posted_user_name);
        TextView tv_post_upload_date = convertView.findViewById(R.id.tv_post_upload_date);
        TextView tv_post_title = convertView.findViewById(R.id.tv_post_title);
        TextView tv_post_description = convertView.findViewById(R.id.tv_post_description);
        TextView tv_post_tags = convertView.findViewById(R.id.tv_post_tags);
//        Button btn_post_file_down = convertView.findViewById(R.id.btn_post_file_down);
        TextView tv_post_comments_count = convertView.findViewById(R.id.tv_post_comments_count);
        TextView tv_post_likes_count = convertView.findViewById(R.id.tv_post_likes_count);
        TextView tv_post_download_count = convertView.findViewById(R.id.tv_post_download_count);
        RatingBar rb_study_score = convertView.findViewById(R.id.rb_study_score);
        EditText et_study_score_rate = convertView.findViewById(R.id.et_study_score_rate);

        final StudyPostInfo posts = (StudyPostInfo) dataList.get(position);
        tv_posted_user_name.setText(posts.getName());
        tv_post_upload_date.setText(posts.getWrittenTime());
        tv_post_title.setText(posts.getTitle());
        tv_post_description.setText(posts.getContent());
        tv_post_tags.setText(posts.getTags());
//        btn_post_file_down.setText("None");
        tv_post_comments_count.setText("0");
        tv_post_likes_count.setText(Long.toString(posts.getLikedCount()));
        tv_post_download_count.setText(Long.toString(posts.getDownLoad()));
        rb_study_score.setRating(posts.getRating());
        et_study_score_rate.setText(Float.toString(posts.getRating()));
//
//        ID = posts.getUploadDate() + " " + posts.getWriter() + " " + posts.getPostName();

//        tv_post_title.setTag(ID);

        return convertView;
    }

}