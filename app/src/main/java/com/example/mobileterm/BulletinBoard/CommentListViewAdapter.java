package com.example.mobileterm.BulletinBoard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mobileterm.R;

import java.util.ArrayList;

public class CommentListViewAdapter extends BaseAdapter {
    private static String TAG = "CommentListViewAdapter";
    Context mContext;
    LayoutInflater inflater;
    ArrayList<CommentInfo> dataList;
//    String did;

    public CommentListViewAdapter(Context context, ArrayList<CommentInfo> dataList) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.dataList = new ArrayList<CommentInfo>();
        this.dataList.addAll(dataList);
        Log.e(TAG, "CommentListViewAdapter: "+this.dataList.size());


    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View itemView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final CommentInfo commentItem = dataList.get(i);

        if (itemView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.fragment_board_comment_item_view, viewGroup, false);
        }else{
            View view = new View(context);
            view = itemView;
        }

        TextView commentContentTextView = itemView.findViewById(R.id.commentContentTextView);
        TextView commentNameTextView = itemView.findViewById(R.id.commentNameTextView);
        TextView commentWrittenTimeView = itemView.findViewById(R.id.commentWrittenTimeView);
        commentContentTextView.setText(commentItem.getContent());
        commentNameTextView.setText(commentItem.getName());
        commentWrittenTimeView.setText(commentItem.getWrittenTime());

        return itemView;
    }

    public void addComment(CommentInfo newComment){
        dataList.add(0,newComment);
        notifyDataSetChanged();
    }
}
