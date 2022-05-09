package com.example.mobileterm.BulletinBoard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileterm.MainActivity;
import com.example.mobileterm.R;

import java.util.ArrayList;

public class BoardItemFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_item, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        BoardInfo selectedBoardItem = mainActivity.sendBoardItem();
        TextView nameTextViewBoardItem = rootView.findViewById(R.id.nameTextViewBoardItem);
        TextView contentTextViewBoardItem = rootView.findViewById(R.id.contentTextViewBoardItem);
        TextView titleTextViewBoardItem = rootView.findViewById(R.id.titleTextViewBoardItem);

        nameTextViewBoardItem.setText(selectedBoardItem.getName());
        contentTextViewBoardItem.setText(selectedBoardItem.getContent());
        titleTextViewBoardItem.setText(selectedBoardItem.getTitle());


        return rootView;
    }
}
