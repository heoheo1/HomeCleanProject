package com.hj.homecleanproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hj.homecleanproject.customInterface.onBackPressedListener;

import java.util.ArrayList;


public class GridFragment extends Fragment implements onBackPressedListener {

    IntroActivity introActivity;
    ViewGroup viewGroup;
    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;
    BottomNavigationView bottomNavi;
    LoginFragment loginFragment;
    NavigationView navigation;

    long lastBackPressed = 0;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        introActivity = (IntroActivity) getActivity();

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_grid, container, false);
        bottomNavi = viewGroup.findViewById(R.id.bottomNavi);
        gridView = viewGroup.findViewById(R.id.grid);
        workList = new ArrayList<>();
        adapter = new MyGridAdapter();
        navigation = viewGroup.findViewById(R.id.nav_view);
        gridView.setAdapter(adapter);

        adapter.addItem(new MyWork(R.drawable.moon, "하이요"));
        adapter.addItem(new MyWork(R.drawable.moon, "하이요"));
        adapter.notifyDataSetChanged();

        bottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_login:
                        // getActivity()로 IntroActivity replaceFragment를 불러옵니다.
                        ((IntroActivity) getActivity()).replaceFragment(LoginFragment.newInstance());
                }
                return true;
            }
        });

        viewGroup.findViewById(R.id.hamburger).setOnClickListener(v -> { //hamburger button 클릭시
            navigation.setVisibility(View.VISIBLE);
        });

        return viewGroup;
    }

    @Override
    public void onBackPressed() {
        if (navigation.getVisibility() == View.VISIBLE) {
            navigation.setVisibility(View.INVISIBLE);
        } else {
            //마지막누른시간의 2초사이에 버튼을 누르면
            if (System.currentTimeMillis() <= lastBackPressed + 2000) {
                introActivity.finish(); //Activity를 종료 -> 앱 종료
            }
            lastBackPressed = System.currentTimeMillis(); //마지막으로 누른 시간을 가져옴
            Toast.makeText(getActivity(), "한번 더 뒤로가기 버튼을 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }//onBackPressed()
}
