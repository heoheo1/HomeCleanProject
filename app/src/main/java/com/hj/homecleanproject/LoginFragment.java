package com.hj.homecleanproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;


//Fragment란, 어플리케이션에서 화면에 직접 보이는 공간의 Activity내에서 분할시키고 다른 화면으로 전환할 수 있는 화면 공간의 단위
//Fragment를 이용하려면, 상위에 있는 Activity에서 출력할 layout을 제어
//하지만 Fragment 내부에서 다른 Fragment로 이동하는 것은 그 Fragment가 자신의 하위레벨이 아니기 때문에
//내부에서 직접 제어할 수 없으므로 , 상위 레벨인 Activity를 호출하여 제어하는 형태가 되어야 함




public class LoginFragment extends Fragment {

    ViewGroup viewGroup;
    Button btn_Login;
    IntroActivity introActivity;
    GridFragment gridFragment;

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성, Fragment들이 메소드를 가지고 있어야 화면전환 가능
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        introActivity = (IntroActivity) getActivity();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);

        btn_Login=viewGroup.findViewById(R.id.login);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                introActivity.fragmentManager.beginTransaction().replace(R.id.login_layout,new GridFragment() ,null).commit();
            }
        });

        return viewGroup;
    }
}