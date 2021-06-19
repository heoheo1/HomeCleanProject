package com.hj.homecleanproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MyFamilyFragment extends Fragment {

    ViewGroup viewGroup;

    public static MyFamilyFragment newInstance() {
        return new MyFamilyFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_my_family, container, false);

        return viewGroup;
    }
}