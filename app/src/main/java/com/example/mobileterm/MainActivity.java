package com.example.mobileterm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import androidx.loader.content.CursorLoader;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.util.Log;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobileterm.BulletinBoard.BoardAddItemFragment;
import com.example.mobileterm.BulletinBoard.BoardFragment;
import com.example.mobileterm.BulletinBoard.BoardInfo;
import com.example.mobileterm.BulletinBoard.BoardItemFragment;
import com.example.mobileterm.Calendar.gCalendarFragment;
import com.example.mobileterm.Calendar.iCalendarFragment;
import com.example.mobileterm.Init.LoginSelectionActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static Fragment studyFragment, boardFragment, iCalendarFragment, gCalendarFragment, myHomeFragment, boardItemFragment, boardAddItemFragment;
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    public static final int REQUESTCODE = 101;

    public BoardInfo selectedBoardItem;
    private static final String TAG = "MainActivity:";

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;


    private String pathUri;
    private File tempFile;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage mStorage;


    private String selectedBoardItemDid;
    private String uid;
    private String name;
    private String email;

    //유저 프로필 pram
    private String nickname;
    private Uri photoUrl;
    private String phone;
    private int token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        //로그인 액티비티에서 메인엑티비티 호출할때 넘겨준 유저 ID값
        uid = data.getString("uid");
        name = data.getString("name");
        email = data.getString("email");
        Log.d(TAG, "main : " + name + ", " + email + ", " + uid);


//        Bundle putdata = new Bundle();
//        putdata.putString("uid", uid);
//        myHomeFragment.setArguments(putdata);


        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        initiate_fragment();
        initiate_nav_menu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LogoutMenu:
                FirebaseAuth.getInstance().signOut();
                StartActivity(LoginSelectionActivity.class);

        }
        return super.onOptionsItemSelected(item);
    }

    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        // 동일한 창이 여러번 뜨게 만드는 것이 아니라 기존에 켜져있던 창을 앞으로 끌어와주는 기능.
        // 이 플래그를 추가하지 않을 경우 창들이 중복돼서 계속 팝업되게 된다.
        // 메인화면을 띄우는 모든 코드에서 이 플래그를 추가해줘야 하는 것 같다.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initiate_fragment() {
        studyFragment = new StudyFragment();
        myHomeFragment = new MyHomeFragment();
        gCalendarFragment = new gCalendarFragment();
        iCalendarFragment = new iCalendarFragment();
        boardFragment = new BoardFragment();
        boardItemFragment = new BoardItemFragment();
        boardAddItemFragment = new BoardAddItemFragment();
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
                switch (item.getItemId()) {
                    case R.id.nav_menu_study:
                        // StudyFragment로 교체
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, studyFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.nav_menu_board:
                        // BoardFragment로 교체
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, boardFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.nav_menu_calendar:
                        // CalendarFragment로 교체
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, iCalendarFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.nav_menu_my_home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, myHomeFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView profile_iv = findViewById(R.id.profile_setting_iv);
        ImageView myhome_profile_iv = findViewById(R.id.my_home_profile_iv);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_TAKE_ALBUM: { // 앨범에서 사진골라오면, 스토리지에 저장 & 프로필 사진 변경
                // Uri
                imageUri = Uri.parse(getPath(data.getData()));
                clickUpload(uid);
                RequestOptions cropOptions = new RequestOptions();
                Glide.with(getApplicationContext())
                        .load(imageUri)//사진의 절대경로
                        .apply(cropOptions.optionalCircleCrop()) //원형태
                        .into(profile_iv); //imageview에 출력
                Glide.with(getApplicationContext())
                        .load(imageUri)//사진의 절대경로
                        .apply(cropOptions.optionalCircleCrop()) //원형태
                        .into(myhome_profile_iv); //imageview에 출력

                break;
            }
            case REQUEST_TAKE_PHOTO: { // 카메라로 사진찍으면 로컬에 저장 & 스토리지에 저장 & 프로필 사진 변경
                Log.v("알림", "FROM_CAMERA 처리");
                galleryAddPic();
                clickUpload(uid);
                RequestOptions cropOptions = new RequestOptions();
                Glide.with(getApplicationContext())
                        .load(imageUri)//사진의 절대경로
                        .apply(cropOptions.optionalCircleCrop()) //원형태
                        .into(profile_iv); //imageview에 출력
                Glide.with(getApplicationContext())
                        .load(imageUri)//사진의 절대경로
                        .apply(cropOptions.optionalCircleCrop()) //원형태
                        .into(myhome_profile_iv); //imageview에 출력

                break;
            }

        }
    }

    //권한 체크 함수
//        public void checkPermission() {
//            String[] permissionList = {Manifest.permission.READ_EXTERNAL_STORAGE};
//
//            //현재 버전 6.0 미만이면 종료 --> 6이후 부터 권한 허락
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
//
//            //각 권한 허용 여부를 확인
//            for (String permission : permissionList) {
//                int chk = checkCallingOrSelfPermission(permission);
//                //거부 상태라면
//                if (chk == PackageManager.PERMISSION_DENIED) {
//                    //사용자에게 권한 허용여부를 확인하는 창을 띄운다.
//                    requestPermissions(permissionList, 0); //권한 검사 필요한 것들만 남는다.
//                    break;
//                }
//            }
//        }
    // uri 절대경로 가져오기
    private String getPath(Uri uri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }

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

    //촬영 후 이미지 가져옴
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

//    public void cropImage() {
//        Intent cropIntent = new Intent("com.android.camera.action.CROP");
//        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        cropIntent.setDataAndType(photoURI, "image/*");
//        cropIntent.putExtra("aspectX", 1);
//        cropIntent.putExtra("aspectY", 1);
//        cropIntent.putExtra("scale", true);
//        cropIntent.putExtra("output", albumURI);
//        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
//
//    }

    //갤러리에 사진 추가 함수
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        File file = new File(mCurrentPhotoPath);
        Uri contentURI = Uri.fromFile(file);
        mediaScanIntent.setData(contentURI);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "앨범에 저장되었습니다", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void noti() {
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (!notificationManager.isNotificationPolicyAccessGranted()) {

            startActivity(new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
        }


    }

    public void clickUpload(String uid) {
        //firebase storage에 업로드하기
        //1. FirebaseStorage을 관리하는 객체 얻어오기
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        //2. 업로드할 파일의 node를 참조하는 객체
        String filename = "profile" + uid + ".jpg";//profile1.jpg 형태로 저장
        //원래 확장자는 파일의 실제 확장자를 얻어와서 사용해야함. 그러려면 이미지의 절대 주소를 구해야함.

        StorageReference imgRef = firebaseStorage.getReference("profile_image/" + filename);
        //uploads라는 폴더가 없으면 자동 생성

        //참조 객체를 통해 이미지 파일 업로드
        // imgRef.putFile(imgUri);
        //업로드 결과를 받고 싶다면..
        UploadTask uploadTask = imgRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "success upload", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void onFragmentChanged(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, iCalendarFragment).addToBackStack(null).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, gCalendarFragment).addToBackStack(null).commit();
        } else if (index == 201) {
            Log.d(TAG, "should show board add item fragment");
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardAddItemFragment);
//            transaction.addToBackStack(null).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardAddItemFragment).addToBackStack(null).commit();

        }
    }

    public void onFragmentChanged(BoardInfo data, String did) {
        selectedBoardItem = data;
        selectedBoardItemDid = did;
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardItemFragment);
//        transaction.addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardItemFragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    public BoardInfo sendBoardItem() {
        return selectedBoardItem;
    }

    public String sendDid() {
        return selectedBoardItemDid;
    }

}

