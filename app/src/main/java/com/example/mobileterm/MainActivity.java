package com.example.mobileterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static Fragment studyFragment, boardFragment, calendarFragment, myHomeFragment;
    private static final int MY_PERMISSION_CAMERA =1111;
    private static final int REQUEST_TAKE_PHOTO =2222;
    private static final int REQUEST_TAKE_ALBUM =3333;
    private static final int REQUEST_IMAGE_CROP =4444;
    public static final int REQUESTCODE =101;
    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;

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
                        return true;
                    case R.id.nav_menu_calendar:
                        // CalendarFragment로 교체
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.main)
//    }
    void captureCamera() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri providerUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerUri;

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerUri);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            } else {
                Toast.makeText(this, "불가능한 접근입니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //촬영 혹은 크롭된 사진에 대한 새로운 이미지 저장 함수
    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName ="JPEG_" +timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+ "/Pictures");

        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath=imageFile.getAbsolutePath();
        return imageFile;
    }
    void getAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }
    public void cropImage(){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI,"image/*");
        cropIntent.putExtra("aspectX",1);
        cropIntent.putExtra("aspectY",1);
        cropIntent.putExtra("scale",true);
        cropIntent.putExtra("output",albumURI);
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);

    }
    //갤러리에 사진 추가 함수
    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        File file = new File(mCurrentPhotoPath);
        Uri contentURI = Uri.fromFile(file);
        mediaScanIntent.setData(contentURI);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "앨범에 저장되었습니다", Toast.LENGTH_SHORT).show();
    }
}