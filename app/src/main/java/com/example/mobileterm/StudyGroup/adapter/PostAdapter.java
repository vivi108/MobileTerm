package com.example.mobileterm.StudyGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.vo.PostVo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter  extends ArrayAdapter {

    private Context context;
    private List list;

    class ViewHolder{
        public TextView tv_posted_user_name;
        public TextView tv_post_upload_date;
        public TextView tv_post_title;
        public TextView tv_post_description;
        public TextView tv_post_tags;
        public TextView btn_post_file_down;
        public TextView tv_post_comments_count;
        public TextView tv_post_likes_count;
        public TextView tv_post_download_count;
        public RatingBar rb_study_score;
        public EditText et_study_score_rate;
    }

    public PostAdapter(Context context, ArrayList list){
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
            convertView = inflater.inflate(R.layout.custom_item_study_post, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.tv_posted_user_name = convertView.findViewById(R.id.tv_posted_user_name);
        viewHolder.tv_post_upload_date = convertView.findViewById(R.id.tv_post_upload_date);
        viewHolder.tv_post_title = convertView.findViewById(R.id.tv_post_title);
        viewHolder.tv_post_description = convertView.findViewById(R.id.tv_post_description);
        viewHolder.tv_post_tags = convertView.findViewById(R.id.tv_post_tags);
        viewHolder.btn_post_file_down = convertView.findViewById(R.id.btn_post_file_down);
        viewHolder.tv_post_comments_count = convertView.findViewById(R.id.tv_post_comments_count);
        viewHolder.tv_post_likes_count = convertView.findViewById(R.id.tv_post_likes_count);
        viewHolder.tv_post_download_count = convertView.findViewById(R.id.tv_post_download_count);
        viewHolder.rb_study_score = convertView.findViewById(R.id.rb_study_score);
        viewHolder.et_study_score_rate = convertView.findViewById(R.id.et_study_score_rate);

        final PostVo posts = (PostVo) list.get(position);
        viewHolder.tv_posted_user_name.setText(posts.getWriter());
        viewHolder.tv_post_upload_date.setText(posts.getUploadDate());
        viewHolder.tv_post_title.setText(posts.getPostName());
        viewHolder.tv_post_description.setText(posts.getContent());
        viewHolder.tv_post_tags.setText(posts.getTag());
        viewHolder.btn_post_file_down.setText(posts.getFileName());
        viewHolder.tv_post_comments_count.setText(posts.getCommentSize());
        viewHolder.tv_post_likes_count.setText(posts.getLikeNum());
        viewHolder.tv_post_download_count.setText(posts.getDownLoad());
        viewHolder.rb_study_score.setRating(posts.getRating());
        viewHolder.et_study_score_rate.setText(posts.getStarRate());

        ID = posts.getUploadDate() + " " + posts.getWriter() + " " + posts.getPostName();

        viewHolder.tv_post_title.setTag(ID);

        return convertView;
    }

}