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
    FragmentManager fragmentManager;
    IntroActivity introActivity;
    FrameLayout login_Frame;
    GridFragment gridFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        introActivity = (IntroActivity) getActivity();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);

        btn_Login=viewGroup.findViewById(R.id.login);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridFragment =new GridFragment();
                introActivity.fragmentManager.beginTransaction().replace(R.id.login_Frame, gridFragment ,null).commit();
            }
        });

        return viewGroup;


    }

}