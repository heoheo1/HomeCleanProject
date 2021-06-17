package com.hj.homecleanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.rxjava3.core.Flowable;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<MyWork> workList;

    //2021 06 17
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        workList = new ArrayList<>();





    }
}