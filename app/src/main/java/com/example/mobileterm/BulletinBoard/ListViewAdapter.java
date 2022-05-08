package com.example.mobileterm.BulletinBoard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileterm.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = "Adapter";
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<BoardInfo> DataList;

    public ListViewAdapter(Context context, ArrayList<BoardInfo> dataList){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.DataList = new ArrayList<BoardInfo>();
        this.DataList.addAll(dataList);
        Log.d(TAG, "ListViewAdapter : "+DataList.size());
    }


    public View getView(final int position, View itemView, ViewGroup parent){
        final Context context = parent.getContext();
        final BoardInfo boardItem = DataList.get(position);
        if (itemView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.fragment_board_item_view, parent, false);
        }else{
            View view = new View(context);
            view = itemView;
        }
        TextView titleTextView = itemView.findViewById(R.id.titleTextView);
        TextView contentTextView = itemView.findViewById(R.id.contentTextView);
        TextView nameTextView = itemView.findViewById(R.id.nameTextView);
        TextView tagTextView = itemView.findViewById(R.id.tagTextView);

        titleTextView.setText(boardItem.getTitle().toString());
        String contentPreview = boardItem.getContent().toString().substring(0,10)+"...";
        contentTextView.setText(contentPreview);
        nameTextView.setText(boardItem.getName().toString());
        tagTextView.setText(boardItem.getTags().toString());

        Log.d(TAG, "view - "+position+" - "+boardItem.getName());

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(context, boardItem.getName(), Toast.LENGTH_SHORT).show();
//                Log.d("boardItemClicked","by onclick");
//            }
//        });


        return itemView;
    }

    @Override
    public int getCount() { return DataList.size(); }

    @Override
    public long getItemId(int position ){ return position;}

    @Override
    public BoardInfo getItem(int position) { return DataList.get(position) ;}
}
