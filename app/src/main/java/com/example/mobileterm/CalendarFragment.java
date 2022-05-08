package com.example.mobileterm;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CalendarFragment extends Fragment {
    iCalendarFragment iCalendarFragment;
    gCalendarFragment gCalendarFragment;

//0508
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