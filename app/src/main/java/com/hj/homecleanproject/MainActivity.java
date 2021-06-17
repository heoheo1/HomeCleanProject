package com.hj.homecleanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;

    //2021 06 17
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.grid);
        workList = new ArrayList<>();
        adapter = new MyGridAdapter();
        gridView.setAdapter(adapter);

        adapter.addItem(new MyWork(R.drawable.moon,"하이요"));
        adapter.addItem(new MyWork(R.drawable.moon,"하이요"));
        adapter.notifyDataSetChanged();



    }
}