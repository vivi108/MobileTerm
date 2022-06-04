package com.example.mobileterm.ChattingActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileterm.ChattingActivity.Code;
import com.example.mobileterm.ChattingActivity.DataItem;
import com.example.mobileterm.R;

import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataItem> myDataList = null;
    public MyAdapter(ArrayList<DataItem> dataList) {myDataList=dataList;}
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType== Code.ViewType.CENTER_CONTENT){
            view =inflater.inflate(R.layout.room_center_item_list, parent, false);
            return new CenterViewHolder(view);
        }
        else if(viewType== Code.ViewType.LEFT_CONTENT){
            view =inflater.inflate(R.layout.room_left_item_list, parent, false);
            return new LeftViewHolder(view);
        }
        else {
            view =inflater.inflate(R.layout.room_right_item_list, parent, false);
            return new RightViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder( @NonNull RecyclerView.ViewHolder viewHolder, int position){
        if(viewHolder instanceof CenterViewHolder){
            ((CenterViewHolder)viewHolder).textv.setText(myDataList.get(position).getContent());
        }
        else if(getItemViewType(position)== Code.ViewType.LEFT_CONTENT){
            ((LeftViewHolder)viewHolder).textv_nicname.setText(myDataList.get(position).getName());
            ((LeftViewHolder)viewHolder).textv_msg.setText(myDataList.get(position).getContent());
            ((LeftViewHolder)viewHolder).textv_time.setText(myDataList.get(position).getTime().toString());

        }
        else {
            ((RightViewHolder)viewHolder).textv_msg.setText(myDataList.get(position).getContent());
            ((RightViewHolder)viewHolder).textv_time.setText(myDataList.get(position).getTime().toString());;
        }
    }
    @Override
    public int getItemCount(){
        return myDataList.size();
    }
    @Override
    public int getItemViewType(int position){
        return myDataList.get(position).getViewType();
    }
    // "리사이클러뷰에 들어갈 뷰 홀더", 그리고 "그 뷰 홀더에 들어갈 아이템들을 셋팅"
    public class CenterViewHolder extends RecyclerView.ViewHolder{
        TextView textv;

        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            textv = (TextView)itemView.findViewById(R.id.textv);
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder{
        ImageView imgv;
        TextView textv_nicname;
        TextView textv_msg;
        TextView textv_time;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            imgv = (ImageView)itemView.findViewById(R.id.imgv);
            textv_nicname = (TextView)itemView.findViewById(R.id.textv_nicname);
            textv_msg = (TextView)itemView.findViewById(R.id.textv_msg);
            textv_time = (TextView)itemView.findViewById(R.id.textv_time);


        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{
        TextView textv_msg;
        TextView textv_time;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            textv_msg = (TextView)itemView.findViewById(R.id.textv_msg);
            textv_time = (TextView)itemView.findViewById(R.id.textv_time);
        }
    }
}
