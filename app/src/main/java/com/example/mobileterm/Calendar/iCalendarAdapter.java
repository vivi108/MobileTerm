package com.example.mobileterm.Calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mobileterm.BulletinBoard.BoardInfo;
import com.example.mobileterm.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class iCalendarAdapter extends BaseAdapter {

    Context mContext;
    private static final String TAG = "iCalendarAdapter";

//    private TextView scheduleTextView;
    private TextView isdoneCheck;
    LayoutInflater inflater;
    private ArrayList<iCalendarItem> scheduleList;
    //private ArrayList<iCalendarItem> scheduleList1;
    private FirebaseFirestore db;
    private FirebaseUser curUser;

    public iCalendarAdapter(ArrayList<iCalendarItem> arrayList) {
        this.scheduleList = new ArrayList<iCalendarItem>();
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.icontent, parent, false);
        }else{
            View view = new View(context);
            view = convertView;
        }

        TextView scheduleTextView = (TextView) convertView.findViewById(R.id.text1);

        iCalendarItem a = scheduleList.get(position);

        scheduleTextView.setText(a.getSchedule());

        return convertView;

    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public iCalendarItem getItem(int position) {
        return scheduleList.get(position);
    }


    public void addItem(String schedule) // 일단 스케줄만 add 하도록 테스트
    {
        iCalendarItem item = new iCalendarItem();

        item.setSchedule(schedule);

        scheduleList.add(item);
    }
}











//    public void addItem(iCalendarItem item) {
//        scheduleList.add(item);
//        notifyDataSetChanged();
//    }

//    public View getView(final int position, View itemView, ViewGroup parent) {
//        final Context context = parent.getContext();
//        final iCalendarItem a = scheduleList.get(position);
//        if (itemView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            itemView = inflater.inflate(R.layout.icontent, parent, false);
//        } else {
//            View view = new View(context);
//            view = itemView;
//        }
//
//        scheduleTextView = (TextView) itemView.findViewById(R.id.text1);
//        isdoneCheck = (TextView) itemView.findViewById(R.id.isDone);
//
//        scheduleTextView.setText(a.getSchedule());
//        isdoneCheck.setText(a.getIsDone());
//        db = FirebaseFirestore.getInstance();
//
//        CollectionReference docref = db.collection("Users").document(curUser.getUid()).collection("iSchedule");
//
//
//        return itemView;
//
//    }


//    public iCalendarAdapter(Context context, ArrayList<iCalendarItem> scheduleList2) {
//        mContext = context;
//        this.scheduleList1 = new ArrayList<iCalendarItem>();
//        this.scheduleList1.addAll(scheduleList2);
//        inflater = LayoutInflater.from(mContext);
//        this.scheduleList = new ArrayList<iCalendarItem>();
//        this.scheduleList.addAll(scheduleList2);
//        Log.d(TAG, "ListViewAdapter : " + scheduleList.size());
//    }

