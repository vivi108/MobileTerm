package com.example.mobileterm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import androidx.loader.content.CursorLoader;

import android.app.Activity;
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
import com.example.mobileterm.StudyGroup.StudyFindFragment;
import com.example.mobileterm.StudyGroup.StudyFragment;
import com.example.mobileterm.StudyGroup.StudyGroupFragment;
import com.example.mobileterm.StudyGroup.StudyMakeFragment;
import com.example.mobileterm.StudyGroup.StudyPostFragment;
import com.example.mobileterm.StudyGroup.WritePostFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static Fragment studyFragment, boardFragment, iCalendarFragment, gCalendarFragment, myHomeFragment, boardItemFragment, boardAddItemFragment, studyFindFragment, studyMakeFragment;
    private static Fragment studyGroupFragment,writePostFragment,studyPostFragment;
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    public static final int REQUESTCODE = 101;

    public BoardInfo selectedBoardItem;
    public String selectedAuthor;
    private static final String TAG = "MainActivity:";

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;


    private String pathUri;
    private File tempFile;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseStorage mStorage;


    private String selectedBoardItemDid;
    private String uid;
//    private String name;
    private String email;

    //?????? ????????? pram
    private String nickname;
    private String studyTitle;
    private String pid;
    private String groupSchedule;
    private String userAddress = "";
    private Uri photoUrl;
    private String phone;
    private int token;

    //?????? ?????? var
    private Intent intent;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        //????????? ?????????????????? ?????????????????? ???????????? ????????? ?????? ID???
        uid = data.getString("uid");
        nickname = data.getString("nickname");
        email = data.getString("email");
        Log.d(TAG, "main : " + nickname + ", " + email + ", " + uid);


//        Bundle putdata = new Bundle();
//        putdata.putString("uid", uid);
//        myHomeFragment.setArguments(putdata);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
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
        // ????????? ?????? ????????? ?????? ????????? ?????? ????????? ????????? ???????????? ?????? ????????? ??????????????? ??????.
        // ??? ???????????? ???????????? ?????? ?????? ????????? ???????????? ?????? ???????????? ??????.
        // ??????????????? ????????? ?????? ???????????? ??? ???????????? ??????????????? ?????? ??? ??????.
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
        studyFindFragment = new StudyFindFragment();
        studyMakeFragment = new StudyMakeFragment();
        studyGroupFragment = new StudyGroupFragment();
        writePostFragment = new WritePostFragment();
        studyPostFragment = new StudyPostFragment();
    }

    private void initiate_nav_menu() {
        // ?????? ????????? ??? ?????? ?????????????????? ??????
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_layout, studyFragment)  // MyHomeFragment() -> StudyFragment()
                .commit();

        // ????????? ?????? ??????????????? ????????? ???????????? ??? ????????? ??? ??????
        BottomNavigationView botNavView = findViewById(R.id.main_bnv);
        botNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu_study:
                        // StudyFragment??? ??????
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, studyFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.nav_menu_board:
                        // BoardFragment??? ??????
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame_layout, boardFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.nav_menu_calendar:
                        // CalendarFragment??? ??????
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
            case REQUEST_TAKE_ALBUM: { // ???????????? ??????????????????, ??????????????? ?????? & ????????? ?????? ??????
                // Uri
                imageUri = Uri.parse(getPath(data.getData()));
                clickUpload(uid);
                RequestOptions cropOptions = new RequestOptions();
                Glide.with(getApplicationContext())
                        .load(imageUri)//????????? ????????????
                        .apply(cropOptions.optionalCircleCrop()) //?????????
                        .into(profile_iv); //imageview??? ??????
                Glide.with(getApplicationContext())
                        .load(imageUri)//????????? ????????????
                        .apply(cropOptions.optionalCircleCrop()) //?????????
                        .into(myhome_profile_iv); //imageview??? ??????

                break;
            }
            case REQUEST_TAKE_PHOTO: { // ???????????? ??????????????? ????????? ?????? & ??????????????? ?????? & ????????? ?????? ??????
                Log.v("??????", "FROM_CAMERA ??????");
                galleryAddPic();
                clickUpload(uid);
                RequestOptions cropOptions = new RequestOptions();
                Glide.with(getApplicationContext())
                        .load(imageUri)//????????? ????????????
                        .apply(cropOptions.optionalCircleCrop()) //?????????
                        .into(profile_iv); //imageview??? ??????
                Glide.with(getApplicationContext())
                        .load(imageUri)//????????? ????????????
                        .apply(cropOptions.optionalCircleCrop()) //?????????
                        .into(myhome_profile_iv); //imageview??? ??????

                break;
            }

        }
    }

    //?????? ?????? ??????
//        public void checkPermission() {
//            String[] permissionList = {Manifest.permission.READ_EXTERNAL_STORAGE};
//
//            //?????? ?????? 6.0 ???????????? ?????? --> 6?????? ?????? ?????? ??????
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
//
//            //??? ?????? ?????? ????????? ??????
//            for (String permission : permissionList) {
//                int chk = checkCallingOrSelfPermission(permission);
//                //?????? ????????????
//                if (chk == PackageManager.PERMISSION_DENIED) {
//                    //??????????????? ?????? ??????????????? ???????????? ?????? ?????????.
//                    requestPermissions(permissionList, 0); //?????? ?????? ????????? ????????? ?????????.
//                    break;
//                }
//            }
//        }
    // uri ???????????? ????????????
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
                Toast.makeText(this, "???????????? ???????????????", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //?????? ??? ????????? ?????????
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

    //???????????? ?????? ?????? ??????
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        File file = new File(mCurrentPhotoPath);
        Uri contentURI = Uri.fromFile(file);
        mediaScanIntent.setData(contentURI);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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
        //firebase storage??? ???????????????
        //1. FirebaseStorage??? ???????????? ?????? ????????????
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        //2. ???????????? ????????? node??? ???????????? ??????
        String filename = "profile" + uid + ".jpg";//profile1.jpg ????????? ??????
        //?????? ???????????? ????????? ?????? ???????????? ???????????? ???????????????. ???????????? ???????????? ?????? ????????? ????????????.

        StorageReference imgRef = firebaseStorage.getReference("profile_image/" + filename);
        //uploads?????? ????????? ????????? ?????? ??????

        //?????? ????????? ?????? ????????? ?????? ?????????
        // imgRef.putFile(imgUri);
        //????????? ????????? ?????? ?????????..
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
        }else if (index == 300){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, studyFragment).addToBackStack(null).commit();
        }else if (index == 301){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, studyMakeFragment).addToBackStack(null).commit();
        }else if (index == 302) {
            if (userAddress.length() > 0 ){
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, studyFindFragment).addToBackStack(null).commit();
            }else{
                db.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            userAddress = (String) document.getData().get("address");
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, studyFindFragment).addToBackStack(null).commit();
                        }
                    }
                });
            }


        }else if (index == 400){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, writePostFragment).addToBackStack(null).commit();
        }
    }

    public void onFragmentChanged(int index, String postid){
        if (index == 1) {
            groupSchedule = postid;
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, gCalendarFragment).addToBackStack(null).commit();
        }else{
            pid = postid;
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, studyPostFragment).addToBackStack(null).commit();
        }

    }

    public void onFragmentChanged(String title, int index){
        studyTitle = title;
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, studyGroupFragment).addToBackStack(null).commit();
    }

//    public void onFragmentChanged(BoardInfo data, String did) {
//        selectedBoardItem = data;
//        selectedBoardItemDid = did;
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardItemFragment).addToBackStack(null).commit();
//    }

    public void onFragmentChanged(String title, String content, String uName, String wTime){
        selectedBoardItemDid = wTime+" "+title;
        selectedAuthor = uName;
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardItemFragment).addToBackStack(null).commit();
    }

    public void onFragmentChanged(String deleted){
        if (deleted.equals("deleted")){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, boardFragment).addToBackStack(null).commit();
        }
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

    public String sendAuthorName() {
        return selectedAuthor;
    }

    public String sendUserNickname() {
        return nickname;
    }

    public String sendStudyTitle(){
        return studyTitle;
    }

    public String sendPid() {
        return pid;
    }

    public String sendGroupSchedule(){
        return groupSchedule;
    }

    public String sendUserAddress() { return userAddress; }

}

