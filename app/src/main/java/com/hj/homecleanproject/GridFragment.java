package com.hj.homecleanproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hj.homecleanproject.customDialog.GridDialogFragment;

import java.util.ArrayList;


public class GridFragment extends Fragment{

    IntroActivity introActivity;
    ViewGroup viewGroup;
    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;
    FragmentActivity fragmentActivity;

    NavigationView navigation;
    private GridDialogFragment dialog;

    public static GridFragment newInstance() {
        return new GridFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentActivity = (FragmentActivity) getActivity();

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_grid, container, false);
        gridView = viewGroup.findViewById(R.id.grid);
        navigation =viewGroup.findViewById(R.id.nav_view);


        workList = new ArrayList<>();
        adapter = new MyGridAdapter();
        gridView.setAdapter(adapter);

        adapter.addItem(new MyWork(R.drawable.moon, "하이요"));
        adapter.addItem(new MyWork(R.drawable.moon, "하이요"));
        adapter.notifyDataSetChanged();



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyWork work = (MyWork) adapter.getItem(position);
                int resID = work.getResID();
                String contents = work.getContent();

                if(resID == R.drawable.moon){
                    dialog = new GridDialogFragment(resID, contents);
                }else{
                    dialog = new GridDialogFragment();
                }
                dialog.setCancelable(false);
                dialog.show(getFragmentManager(),null);
            }
        });

        viewGroup.findViewById(R.id.hamburger).setOnClickListener(v -> { //hamburger button 클릭시
            navigation.setVisibility(View.VISIBLE);
        });
        return viewGroup;
    }
}
