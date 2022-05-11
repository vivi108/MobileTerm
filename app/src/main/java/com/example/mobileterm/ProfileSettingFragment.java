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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileSettingFragment extends Fragment {
    //카메라, 앨범등의 기능을 사용하기 위해서 상수들을 지정해주고 필요한 uri변수나 경로변수들을 지정
    private static final int MY_PERMISSION_CAMERA =1111;
    private static final int REQUEST_TAKE_PHOTO =2222;
    private static final int REQUEST_TAKE_ALBUM =3333;
    private static final int REQUEST_IMAGE_CROP =4444;
    public static final int REQUESTCODE =101;
    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;


    ImageView profile;
    ImageView camera_menu;
    EditText nickname;
    PopupMenu pop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile_setting, container, false);
        profile = (ImageView) rootView.findViewById(R.id.profile_setting_iv);
        camera_menu = (ImageView) rootView.findViewById(R.id.profile_setting_camera_iv);
        nickname = (EditText) rootView.findViewById(R.id.profile_setting_name_tv);

        setHasOptionsMenu(true); //프래그먼트에서 메뉴사용
        camera_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop= new PopupMenu(rootView.getContext(), view);
                pop.getMenuInflater().inflate(R.menu.profile_popup_menu, pop.getMenu());

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
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
                checkPermission();
            }
        });



        return rootView;
    }

}


