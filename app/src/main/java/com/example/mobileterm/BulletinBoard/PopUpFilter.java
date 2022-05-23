package com.example.mobileterm.BulletinBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mobileterm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class PopUpFilter extends Activity {
    EditText tagSearchEditText;
    ImageButton tagSearchButton;
    Button tagEraseButton;
    FirebaseFirestore db;
    String inputTag;
//    String[] tags;
    ArrayList<String> tags;
    ArrayList<String> dids;
    Intent resultIntent;
    private final String TAG = "PopupFilter :";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up_filter);

        tagEraseButton = findViewById(R.id.tagEraseButton);
        tagSearchEditText = findViewById(R.id.tagSearchEditText);
        tagSearchButton = findViewById(R.id.tagSearchButton);

        db = FirebaseFirestore.getInstance();
        dids = new ArrayList<String>();
        tags = new ArrayList<String>();

        tagEraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                data.putStringArrayList("sItems",null);
//                resultIntent.putExtras(data);
//
//                setResult(RESULT_OK, resultIntent);
                finish();

            }
        });


        tagSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTag = tagSearchEditText.getText().toString();
//                tags = inputTag.split("#");
                finish();

            }
        });

    }
}