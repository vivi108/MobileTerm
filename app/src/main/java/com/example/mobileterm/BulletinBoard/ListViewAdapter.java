package com.example.mobileterm.BulletinBoard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = "Adapter";
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<BoardInfo> DataList;
    private FirebaseFirestore db;
    private String did;


    public ListViewAdapter(Context context, ArrayList<BoardInfo> dataList){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.DataList = new ArrayList<BoardInfo>();
        this.DataList.addAll(dataList);
        Log.e(TAG, "ListViewAdapter : "+DataList.size());
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
        String contentPreview = boardItem.getContent().toString()+"...";
        contentTextView.setText(contentPreview);
        nameTextView.setText(boardItem.getName().toString());
        tagTextView.setText("");
        did = boardItem.getDid();
        Log.e(TAG,did);
        db = FirebaseFirestore.getInstance();

        CollectionReference docref = db.document("BulletinBoard/"+did).collection("BoardTags");

        docref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()){
                        String tag = tagTextView.getText().toString();

                        Log.e(TAG, "view - "+position+" - "+boardItem.getName());
                        if (document.exists()) {
                            tag += "#"+document.get("name")+" ";
                            Log.e(TAG,"must be data of boardTags : "+document.getString("name"));
                            tagTextView.setText(tag);
                        }else{
                            Log.e(TAG, "no doc");
                        }
                    }
                }else{
                    Log.e(TAG,"query fail");
                }
            }
        });


        return itemView;
    }

    @Override
    public int getCount() { return DataList.size(); }

    @Override
    public long getItemId(int position ){ return position;}

    @Override
    public BoardInfo getItem(int position) { return DataList.get(position) ;}

    public void addItem(BoardInfo item) {
        DataList.add(item);
        notifyDataSetChanged();
    }
}
