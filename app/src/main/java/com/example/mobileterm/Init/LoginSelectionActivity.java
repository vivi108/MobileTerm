package com.example.mobileterm.Init;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobileterm.R;

public class LoginSelectionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        findViewById(R.id.emailLoginButton).setOnClickListener(onClickListener);
        findViewById(R.id.googleLogionButton).setOnClickListener(onClickListener);


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.emailLoginButton:
                    StartActivity(LoginActivity.class);
                    break;
                case R.id.googleLogionButton:
                    StartActivity(GoogleLoginActivity.class);
                    break;
            }
        }
    };

    private void StartActivity(Class c){
        Intent intent = new Intent(this, c);
        // 동일한 창이 여러번 뜨게 하지 않고 기존에 켜져있던 창을 앞으로 끌어와주는 기능
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}