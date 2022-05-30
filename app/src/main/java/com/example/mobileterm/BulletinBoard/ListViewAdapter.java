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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = "Adapter";
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<BoardInfo> DataList;
    private ArrayList<BoardInfo> itemList;
    private FirebaseFirestore db;
    private String did;

    //테스트 커밋
    public ListViewAdapter(Context context, ArrayList<BoardInfo> dataList){
        mContext = context;
        this.itemList = new ArrayList<BoardInfo>();
        this.itemList.addAll(dataList);
        inflater = LayoutInflater.from(mContext);
        this.DataList = new ArrayList<BoardInfo>();
        this.DataList.addAll(dataList);
//        Log.d(TAG, "ListViewAdapter : "+DataList.size());
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
        TextView writtenTimeTextView = itemView.findViewById(R.id.writtenTimeTextView);
        TextView likedCountView = itemView.findViewById(R.id.likedCountView);
        TextView hiddendDid = itemView.findViewById(R.id.hiddenDid);




        titleTextView.setText(boardItem.getTitle().toString());
        String contentPreview = boardItem.getContent().toString()+"...";
        contentTextView.setText(contentPreview);
        nameTextView.setText(boardItem.getName().toString());
        tagTextView.setText("");
        writtenTimeTextView.setText(boardItem.getWrittenTime());
        String tempLike = Long.toString(boardItem.getLikedCount());
        hiddendDid.setText(boardItem.getDid());
        likedCountView.setText(tempLike);
        did = boardItem.getDid();
//        Log.d(TAG,did);
        db = FirebaseFirestore.getInstance();

        CollectionReference docref = db.document("BulletinBoard/"+did).collection("BoardTags");
        docref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tagTextView.setText("");
                    for (DocumentSnapshot document : task.getResult()){
                        String tag = tagTextView.getText().toString();

//                        Log.d(TAG, "view - "+position+" - "+boardItem.getName());
                        if (document.exists()) {
                            tag += document.get("name")+" ";
//                            Log.d(TAG,"must be data of boardTags : "+document.getString("name"));
                            tagTextView.setText(tag);
                        }else{
                            Log.d(TAG, "no doc");
                        }
                    }
                }else{
                    Log.d(TAG,"query fail");
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

    // 검정
    public void filter(String searchText, int checkedId) {
        //검색만 한거임, 여기에 정렬만 추가되면 될듯?
        if (searchText != null) {
            searchText = searchText.toLowerCase(Locale.getDefault());
            if (searchText.length() == 0) {
                switch (checkedId){
                    case R.id.orderLike:{
                        for (BoardInfo itr:itemList){
                            DataList.add(itr);
                        }
                        Collections.sort(DataList);
                        notifyDataSetChanged();
                        break;
                    }
                    case R.id.orderOld:{
                        DataList.clear();
                        for (BoardInfo itr:itemList){
                            DataList.add(0, itr);
                        }
                        notifyDataSetChanged();
                        break;
                    }
                    case R.id.orderRecent:{
                        renew();
                        break;
                    }
                }
            } else {
                DataList.clear();
                switch (checkedId){
                    case R.id.orderLike:{
                        for (BoardInfo itr : itemList) {
                            if (itr.getContent().toLowerCase(Locale.getDefault()).contains(searchText) ||
                                    itr.getTitle().toLowerCase(Locale.ROOT).toLowerCase(Locale.getDefault()).contains(searchText) ||
                                    itr.getName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                                DataList.add(itr);
                                Log.d(TAG, itr.getTitle() + " " + itr.getName() + " " + itr.getContent());
                            }
                        }
                        Collections.sort(DataList);
                        notifyDataSetChanged();
                        break;
                    }
                    case R.id.orderOld:{
                        for (BoardInfo itr : itemList) {
                            if (itr.getContent().toLowerCase(Locale.getDefault()).contains(searchText) ||
                                    itr.getTitle().toLowerCase(Locale.ROOT).toLowerCase(Locale.getDefault()).contains(searchText) ||
                                    itr.getName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                                DataList.add(0,itr);
                                Log.d(TAG, itr.getTitle() + " " + itr.getName() + " " + itr.getContent());
                            }
                        }

                        notifyDataSetChanged();
                        break;
                    }
                    case R.id.orderRecent:{
                        for (BoardInfo itr : itemList) {
                            if (itr.getContent().toLowerCase(Locale.getDefault()).contains(searchText) ||
                                    itr.getTitle().toLowerCase(Locale.ROOT).toLowerCase(Locale.getDefault()).contains(searchText) ||
                                    itr.getName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                                DataList.add(itr);
                                Log.d(TAG, itr.getTitle() + " " + itr.getName() + " " + itr.getContent());
                            }
                        }

                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }
    // 태정
    public void filter(ArrayList<String> didList, int checkedId){
        //태그 정렬
        DataList.clear();
        switch (checkedId){
            case R.id.orderLike:{
                if (didList.size() > 0){

                    for (BoardInfo itr : itemList){
                        if (didList.contains(itr.getDid())){
                            DataList.add(itr);
                        }
                    }

                }
                Collections.sort(DataList);
                notifyDataSetChanged();
                break;
            }
            case R.id.orderOld:{
                DataList.clear();
                if (didList.size() > 0){

                    for (BoardInfo itr : itemList){
                        if (didList.contains(itr.getDid())){
                            DataList.add(0,itr);
                        }
                    }

                }
                for (BoardInfo itr:itemList){
                    DataList.add(0, itr);
                }
                notifyDataSetChanged();
                break;
            }
            case R.id.orderRecent:{
                if (didList.size() > 0){

                    for (BoardInfo itr : itemList){
                        if (didList.contains(itr.getDid())){
                            DataList.add(itr);
                        }
                    }
                }
                notifyDataSetChanged();
                break;
            }
        }

    }

    public void renew(){
        DataList.clear();
        for (BoardInfo itr : itemList){
            DataList.add(itr);
        }
        notifyDataSetChanged();
    }

    // 정렬
    public void reorder(int checkedId){
        switch (checkedId){
            case R.id.orderLike:{
                Collections.sort(DataList);
                notifyDataSetChanged();
                break;
            }
            case R.id.orderOld:{
                DataList.clear();
                for (BoardInfo itr:itemList){
                    DataList.add(0, itr);
                }
                notifyDataSetChanged();
                break;
            }
            case R.id.orderRecent:{
                renew();
                break;
            }
        }
    }

    // 검태정
    public void searchTagReorder(String searchText, ArrayList<String> didList, int checkedId){
        // 검색 태그 정렬
        if (searchText != null) {
            searchText = searchText.toLowerCase(Locale.getDefault());

            if (searchText.length() == 0) {
               filter(didList, checkedId);
            } else {
                DataList.clear();
                switch (checkedId){
                    case R.id.orderLike:{
                        if (didList.size() > 0){

                            for (BoardInfo itr : itemList){
                                if (didList.contains(itr.getDid()) && itr.getContent().toLowerCase(Locale.getDefault()).contains(searchText) ||
                                        itr.getTitle().toLowerCase(Locale.ROOT).toLowerCase(Locale.getDefault()).contains(searchText) ||
                                        itr.getName().toLowerCase(Locale.getDefault()).contains(searchText)){
                                    DataList.add(itr);
                                }
                            }

                        }
                        Collections.sort(DataList);
                        notifyDataSetChanged();
                        break;
                    }
                    case R.id.orderOld:{
                        DataList.clear();
                        if (didList.size() > 0){

                            for (BoardInfo itr : itemList){
                                if (didList.contains(itr.getDid()) && itr.getContent().toLowerCase(Locale.getDefault()).contains(searchText) ||
                                        itr.getTitle().toLowerCase(Locale.ROOT).toLowerCase(Locale.getDefault()).contains(searchText) ||
                                        itr.getName().toLowerCase(Locale.getDefault()).contains(searchText)){
                                    DataList.add(0,itr);
                                }
                            }

                        }

                        notifyDataSetChanged();
                        break;
                    }
                    case R.id.orderRecent:{
                        if (didList.size() > 0){

                            for (BoardInfo itr : itemList){
                                if (didList.contains(itr.getDid()) && itr.getContent().toLowerCase(Locale.getDefault()).contains(searchText) ||
                                        itr.getTitle().toLowerCase(Locale.ROOT).toLowerCase(Locale.getDefault()).contains(searchText) ||
                                        itr.getName().toLowerCase(Locale.getDefault()).contains(searchText)){
                                    DataList.add(itr);
                                }
                            }
                        }
                        notifyDataSetChanged();
                        break;
                    }
                }


            }
        }
    }
}
