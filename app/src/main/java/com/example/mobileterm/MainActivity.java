package com.example.mobileterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mobileterm.BulletinBoard.BoardFragment;
import com.example.mobileterm.BulletinBoard.BoardInfo;
import com.example.mobileterm.BulletinBoard.BoardItemFragment;
import com.example.mobileterm.Calendar.CalendarFragment;
import com.example.mobileterm.Calendar.gCalendarFragment;
import com.example.mobileterm.Calendar.iCalendarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static Fragment studyFragment, boardFragment, calendarFragment, myHomeFragment, boardItemFragment;
    ArrayList<BoardInfo> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiate_fragment();
        initiate_nav_menu();
    }

    private void initiate_fragment() {
        studyFragment = new StudyFragment();
        myHomeFragment = new MyHomeFragment();
        calendarFragment = new CalendarFragment();
        boardFragment = new BoardFragment();
        boardItemFragment = new BoardItemFragment();
    }

    private void initiate_nav_menu() {
        // 처음 시작할 때 화면 초기화해주는 단계
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_layout, studyFragment)  // MyHomeFragment() -> StudyFragment()
                .commit();

        // 각각의 하단 네비게이션 아이콘 클릭했을 때 어떻게 할 건지
        BottomNavigationView botNavView = findViewById(R.id.main_bnv);
        botNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_menu_study:
                        // StudyFragment로 교체
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, studyFragment)
                                .commit();
                        return true;
                    case R.id.nav_menu_board:
                        // BoardFragment로 교체
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, boardFragment)
                                .commit();
                        return true;
                    case R.id.nav_menu_calendar:
                        // CalendarFragment로 교체
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, calendarFragment)
                                .commit();
                        return true;
                    case R.id.nav_menu_my_home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, myHomeFragment)
                                .commit();
                        return true;
                }

                return false;
            }
        });
    }
    com.example.mobileterm.Calendar.iCalendarFragment iCalendarFragment;
    com.example.mobileterm.Calendar.gCalendarFragment gCalendarFragment;

    public void onFragmentChanged(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, iCalendarFragment).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, gCalendarFragment).commit();
        }
    }

    public void onFragmentChanged(ArrayList<BoardInfo> data){
        arrayList = data;
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardItemFragment).commit();
    }
}