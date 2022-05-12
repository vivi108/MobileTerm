package com.example.mobileterm.Calendar;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileterm.R;


public class CalendarFragment extends Fragment {
    iCalendarFragment iCalendarFragment;
    gCalendarFragment gCalendarFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        iCalendarFragment = new iCalendarFragment();
        getFragmentManager().beginTransaction().replace(R.id.container,iCalendarFragment).commit();
        gCalendarFragment = new gCalendarFragment();
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

}