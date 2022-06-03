package com.example.mobileterm.BulletinBoard;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CommentListViewAdapter extends BaseAdapter {
    private static String TAG = "CommentListViewAdapter";
    Context mContext;
    LayoutInflater inflater;
    ArrayList<CommentInfo> dataList;
    String curNickname;
    String did;
    FirebaseFirestore db;
    Dialog editCommentDialog;
    public CommentListViewAdapter(Context context, ArrayList<CommentInfo> dataList, String userNickname, String did, Dialog editCommentDialog) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.dataList = new ArrayList<CommentInfo>();
        this.dataList.addAll(dataList);
        this.curNickname = userNickname;
        this.did = did;
        this.editCommentDialog = editCommentDialog;
        this.editCommentDialog.setContentView(R.layout.edit_comment_dialog);
        Log.e(TAG, "CommentListViewAdapter: "+this.dataList.size());

        db = FirebaseFirestore.getInstance();
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
        ImageButton commentEditButton = itemView.findViewById(R.id.commentEditButton);
        ImageButton commentDeleteButton = itemView.findViewById(R.id.commentDeleteButton);


        if (curNickname.equals(commentItem.getName())){
            commentDeleteButton.setVisibility(View.VISIBLE);
            commentEditButton.setVisibility(View.VISIBLE);
        }
        commentContentTextView.setText(commentItem.getContent());
        commentNameTextView.setText(commentItem.getName());
        commentWrittenTimeView.setText(commentItem.getWrittenTime());

        commentEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"number "+Integer.toString(i));
                editCommentDialog.show();
                TextView updateCommentEditText = editCommentDialog.findViewById(R.id.updateCommentEditText);
                ImageButton endEditComment = editCommentDialog.findViewById(R.id.endEditComment);

                endEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("BulletinBoard").document(did).collection("Comments").document(dataList.get(i).getWrittenTime()+dataList.get(i).getName()).update("content",updateCommentEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dataList.get(i).setContent(updateCommentEditText.getText().toString());
                                notifyDataSetChanged();
                                updateCommentEditText.setText("");
                                editCommentDialog.dismiss();
                            }
                        });
                    }
                });

            }
        });

        commentDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("BulletinBoard").document(did).collection("Comments").document(dataList.get(i).getWrittenTime()+dataList.get(i).getName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataList.remove(i);
                        notifyDataSetChanged();
                    }
                });
            }
        });


        return itemView;
    }

    public void addComment(CommentInfo newComment){
        dataList.add(0,newComment);
        notifyDataSetChanged();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}
