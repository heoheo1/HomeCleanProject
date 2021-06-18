package com.hj.homecleanproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;


public class GridFragment extends Fragment {

    ViewGroup viewGroup;
    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup =(ViewGroup)inflater.inflate(R.layout.fragment_grid,container,false);
        gridView = viewGroup.findViewById(R.id.grid);
        workList = new ArrayList<>();
        adapter = new MyGridAdapter();
        gridView.setAdapter(adapter);

        adapter.addItem(new MyWork(R.drawable.moon,"하이요"));
        adapter.addItem(new MyWork(R.drawable.moon,"하이요"));
        adapter.notifyDataSetChanged();
        return viewGroup;

    }
}