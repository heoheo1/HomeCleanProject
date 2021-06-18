package com.hj.homecleanproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class GridFragment extends Fragment {

    IntroActivity introActivity;
    ViewGroup viewGroup;
    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;
    BottomNavigationView bottomNavi;
    LoginFragment loginFragment;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        introActivity = (IntroActivity) getActivity();

        viewGroup =(ViewGroup)inflater.inflate(R.layout.fragment_grid,container,false);
        bottomNavi = viewGroup.findViewById(R.id.bottomNavi);
        gridView = viewGroup.findViewById(R.id.grid);
        workList = new ArrayList<>();
        adapter = new MyGridAdapter();
        gridView.setAdapter(adapter);

        adapter.addItem(new MyWork(R.drawable.moon,"하이요"));
        adapter.addItem(new MyWork(R.drawable.moon,"하이요"));
        adapter.notifyDataSetChanged();

        bottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_login:
                        // getActivity()로 IntroActivity replaceFragment를 불러옵니다.
                        ((IntroActivity)getActivity()).replaceFragment(LoginFragment.newInstance());
                }
                return  true;

            }
        });

        return viewGroup;


    }
}