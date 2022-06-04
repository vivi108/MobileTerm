package com.example.mobileterm.StudyGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.PostComment;
import com.example.mobileterm.StudyGroup.StudyInfo;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PostComment> dataList;

    public CommentAdapter(Context context, ArrayList<PostComment> list) {
        this.context = context;
        this.dataList = new ArrayList<PostComment>();
        dataList.addAll(list);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public PostComment getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.post_comment_view, viewGroup, false);
        }else{
            View view1 = new View(context);
            view1 = view;
        }

        TextView postcommentContentTextView = view.findViewById(R.id.postcommentContentTextView);
        TextView postcommentWrittentTimeTextView = view.findViewById(R.id.postcommentWrittenTimeView);
        TextView postcommentNameTextView = view.findViewById(R.id.postcommentNameTextView);

        final PostComment temp = dataList.get(i);
        postcommentContentTextView.setText(temp.getContent());
        postcommentNameTextView.setText(temp.getName());
        postcommentWrittentTimeTextView.setText(temp.getWtime());
        return view;
    }

    public void addComment(PostComment temp){
        dataList.add(temp);
        notifyDataSetChanged();
    }
}
