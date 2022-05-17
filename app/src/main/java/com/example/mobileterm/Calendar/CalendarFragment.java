package com.example.mobileterm.Calendar;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileterm.R;


public class CalendarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);

        return rootView;
    }


//    iCalendarFragment iCalendarFragment;
//    gCalendarFragment gCalendarFragment;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);
//
//        iCalendarFragment = new iCalendarFragment();
//        getChildFragmentManager().beginTransaction().replace(R.id.container,iCalendarFragment).commit();
//        gCalendarFragment = new gCalendarFragment();
//
//        return rootView;
//    }
//
//    public void onFragmentChanged(int index) {
//        if (index == 0) {
//            getChildFragmentManager().beginTransaction().replace(R.id.container, iCalendarFragment).commit();
//        } else if (index == 1) {
//            getChildFragmentManager().beginTransaction().replace(R.id.container, gCalendarFragment).commit();
//        }
//    }

}