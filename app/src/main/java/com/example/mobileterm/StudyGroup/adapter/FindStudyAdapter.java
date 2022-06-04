package com.example.mobileterm.StudyGroup.adapter;

import static android.view.View.GONE;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobileterm.R;
import com.example.mobileterm.StudyGroup.LikedStudyInfo;
import com.example.mobileterm.StudyGroup.StudyInfo;
import com.example.mobileterm.StudyGroup.vo.FindStudyVo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FindStudyAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<StudyInfo> dataList;
    private ArrayList<StudyInfo> itemList;
    private ArrayList<String> likedStudies;

    private String TAG = "find study adapter";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    public FindStudyAdapter(Context context, ArrayList<StudyInfo> list, ArrayList<String> likedStudies){
        this.context = context;
        this.dataList = new ArrayList<StudyInfo>();
        this.dataList.addAll(list);
        this.itemList = new ArrayList<StudyInfo>();
        this.itemList.addAll(list);
        this.likedStudies = new ArrayList<String>();
        this.likedStudies.addAll(likedStudies);
        Log.d(TAG, "list size"+dataList.size());

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public StudyInfo getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        String ID;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_item_study_find, parent, false);
        }else{
            View view = new View(context);
            view = convertView;
        }
        TextView tv_find_study_name = convertView.findViewById(R.id.tv_find_study_name);
        TextView tv_find_study_member = convertView.findViewById(R.id.tv_find_study_member);
        TextView tv_find_study_tag = convertView.findViewById(R.id.tv_find_study_tag);
        TextView tv_find_study_description = convertView.findViewById(R.id.tv_find_study_description);
        ImageView iv_find_study_locked = convertView.findViewById(R.id.iv_find_study_locked);
        ImageButton btn_find_study_liked = convertView.findViewById(R.id.btn_find_study_liked);



        btn_find_study_liked.setFocusable(false);

        StudyInfo findStudy = (StudyInfo) itemList.get(position);
        tv_find_study_name.setText(findStudy.getStudyName());
        tv_find_study_member.setText(Long.toString(findStudy.getMaxNumPeople()));
        tv_find_study_tag.setText(findStudy.getTags());
        tv_find_study_description.setText(findStudy.getDescription());
        if(findStudy.isOpened()){
            iv_find_study_locked.setVisibility(View.GONE);
            tv_find_study_name.setTag("open "+findStudy.getStudyName());
        }
        else {
            iv_find_study_locked.setVisibility(View.VISIBLE);
            tv_find_study_name.setTag("close "+findStudy.getStudyName());
        }
        if (likedStudies.contains(findStudy.getStudyName())) {
            btn_find_study_liked.setImageResource(R.drawable.ic_heart_filled);
        }else{
            btn_find_study_liked.setImageResource(R.drawable.ic_heart);
        }


        btn_find_study_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,position+" liked clicked");
                if (!likedStudies.contains(findStudy.getStudyName())){
                    btn_find_study_liked.setImageResource(R.drawable.ic_heart_filled);
                    // 디비 업데이트하고 유저에 츄가해줘야함, 음 그냥 좋아요니까 유저에만 추가해주면 될듯하네
                    String sid = "";
                    if (findStudy.isOpened()) {
                        sid = "open ";
                    }else{
                        sid = "close ";
                    }
                    sid += findStudy.getStudyName();
                    LikedStudyInfo newLiked = new LikedStudyInfo(sid, findStudy.getStudyName());
                    db.collection("Users").document(user.getUid()).collection("likedStudy").document(sid).set(newLiked).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG,"관심스터디 등록성공");
                        }
                    });
               }
            }
        });

        return convertView;
    }

    public void renew(){
        itemList.clear();
        for (StudyInfo temp : dataList){
            itemList.add(temp);
        }
        notifyDataSetChanged();
    }


    public void filter(String s){
        itemList.clear();
        for (StudyInfo temp:dataList){
            if (temp.getTags().contains(s)){
                itemList.add(temp);
            }
        }
        notifyDataSetChanged();
    }

    public void filter(String s, int a){
        itemList.clear();
        for (StudyInfo temp:dataList){
            if (temp.getStudyName().contains(s)){
                itemList.add(temp);
            }
        }
        notifyDataSetChanged();
    }
}