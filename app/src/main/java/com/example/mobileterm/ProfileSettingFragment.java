package com.example.mobileterm;

import static java.security.AccessController.checkPermission;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//해결해야하는 문제
//앨범에서 사진을 골랐을때 서버에 업로드가 안됨.
public class ProfileSettingFragment extends Fragment {
    //카메라, 앨범등의 기능을 사용하기 위해서 상수들을 지정해주고 필요한 uri변수나 경로변수들을 지정
//    private static final int MY_PERMISSION_CAMERA =1111;
//    private static final int REQUEST_TAKE_PHOTO =2222;
//    private static final int REQUEST_TAKE_ALBUM =3333;
//    private static final int REQUEST_IMAGE_CROP =4444;
//    public static final int REQUESTCODE =101;
//    String mCurrentPhotoPath;
//    Uri imageUri;
//    Uri photoURI, albumURI;

    FirebaseUser user ;
    ImageView profile;
    ImageView camera_menu;
    EditText nickname;
    PopupMenu pop;
    TextView ok;

    // Id of the provider (ex: google.com)
    String providerId ;
    // UID specific to the provider
    String uid ;
    // Name, email address, and profile photo Url
    String name ;
    String email;
    Uri photoUrl ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile_setting, container, false);
        profile = (ImageView) rootView.findViewById(R.id.profile_setting_iv);
        camera_menu = (ImageView) rootView.findViewById(R.id.profile_setting_camera_iv);
        nickname = (EditText) rootView.findViewById(R.id.profile_setting_name_tv);
        ok = (TextView) rootView.findViewById(R.id.profile_setting_OK_tv);

        // 유저의 닉네임을 서버에서 불러와서 보여주기
        // 유저 닉네임 변경하면 setText 후 서버에 저장하기.
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Gmail로 로그인했을때도 되는건지 확인해야함.
//        if (user != null) {
//            for (UserInfo profile : user.getProviderData()) {
//                // Id of the provider (ex: google.com)
//                providerId = profile.getProviderId();
//
//                // UID specific to the provider
//                uid = profile.getUid();
//
//                // Name, email address, and profile photo Url
//                name = profile.getDisplayName();
//                email = profile.getEmail();
//                photoUrl = profile.getPhotoUrl();
//
//
//            }
            if (user != null) {
                // Name, email address, and profile photo Url
                name = user.getDisplayName();
                email = user.getEmail();
                photoUrl = user.getPhotoUrl();

                // Check if user's email is verified
                boolean emailVerified = user.isEmailVerified();

                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getIdToken() instead.
                uid = user.getUid();
            }

            loadImage(uid);
            setHasOptionsMenu(true); //프래그먼트에서 메뉴사용
            camera_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pop = new PopupMenu(rootView.getContext(), view);
                    pop.getMenuInflater().inflate(R.menu.profile_popup_menu, pop.getMenu());

                    pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.profile_menu_camera:
                                    ((MainActivity) getActivity()).captureCamera(); // 카메라 구동함수 추후처리
                                    break;
                                case R.id.profile_menu_gallery:
                                    ((MainActivity) getActivity()).getAlbum();
                                    break;
                                case R.id.profile_menu_basic:// 기본이미지 설정
                                    profile.setImageResource(R.drawable.ic_profile_person);
                            }
                            return true;
                        }
                    });
                    pop.show();
                    //checkPermission();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //완료버튼을 누르면
                    //변경된 이미지 서버에 저장 후, 보여주기
                    //변경된 이름 서버에 저장 후 보여주기.
//                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                            .setDisplayName("Jane Q. User")
//                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                            .build();
                }
            });

            return rootView;
        }


    private void loadbasicImage() {

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://mptermproject-75489.appspot.com"); //firebase storate 경로
        StorageReference storageRef = storage.getReference();
        if (storageRef.child("profile_image/ic_profile_gray.jpg") != null) {
            storageRef.child("profile_image/ic_profile_gray.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //이미지 로드 성공시


                    Glide.with(getActivity())
                            .load(uri)
                            .into(profile);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //이미지 로드 실패시
                    Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    private void loadImage(String uid){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://mptermproject-75489.appspot.com"); //firebase storate 경로
        StorageReference storageRef = storage.getReference();

        //파일 명 만들기
        String filename = "profile" +uid +".jpg"; //ex) profile1.jpg 로그인 하는 사람의 식별값에 맞는 사진 가져오기
        if(storageRef.child("profile_image/"+filename)!=null) {
            storageRef.child("profile_image/" + filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //이미지 로드 성공시

                    Glide.with(getActivity())
                            .load(uri)
                            .into(profile);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //이미지 로드 실패시
                    //Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    loadbasicImage();
                }
            });
        }
    }
}


