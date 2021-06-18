package com.hj.homecleanproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;




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

                introActivity.fragmentManager.beginTransaction().replace(R.id.login_Frame,new GridFragment() ,null).commit();
            }
        });

        return viewGroup;


    }



}