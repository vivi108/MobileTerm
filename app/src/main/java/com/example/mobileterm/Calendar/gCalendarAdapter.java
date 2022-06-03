package com.example.mobileterm.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class gCalendarAdapter extends BaseAdapter {


    Context mContext;
    private static final String TAG = "gCalendarAdapter";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference docref = db.collection("Schedule").document("aGroup").collection("gSchedule");

    LayoutInflater inflater;
    private ArrayList<gCalendarItem> scheduleList;

    public gCalendarAdapter(ArrayList<gCalendarItem> arrayList) {
        this.scheduleList = new ArrayList<gCalendarItem>();
        this.scheduleList.addAll(arrayList);
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
    public gCalendarItem getItem(int position) {
        return scheduleList.get(position);
    }


    public void addItem(gCalendarItem newItem) // 일단 스케줄만 add 하도록 테스트
    {
        scheduleList.add(newItem);
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gcontent, parent, false);
        }else{
            View view = new View(context);
            view = convertView;
        }

        TextView scheduleText = (TextView) convertView.findViewById(R.id.text1);

        gCalendarItem a = scheduleList.get(position);
        String docAA = a.getDocA();

        scheduleText.setText(a.getSchedule());


        LinearLayout eachlist = (LinearLayout) convertView.findViewById(R.id.eachlist);
        eachlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit, null); //일단 여기까지

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogTheme));

                builder.setTitle("Confirm").setMessage("삭제하시겠습니까?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        docref.document(docAA).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "successfully delete");
                                    //더이상 이거 안보이도록
                                    eachlist.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Log.d(TAG, "취소" + docAA);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });





        return convertView;

    }


}