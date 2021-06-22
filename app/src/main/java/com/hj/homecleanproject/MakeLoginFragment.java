package com.hj.homecleanproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hj.homecleanproject.R;


public class MakeLoginFragment extends Fragment {

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static MakeLoginFragment newInstance() {
        return new MakeLoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_make_login, container, false);
    }
}