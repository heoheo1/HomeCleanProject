package com.hj.homecleanproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;

public class PictureFragment extends Fragment {

    private GalleryAdapter adapter;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager manager;

    private String filePath;

    public static PictureFragment newInstance() {
        return new PictureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture,container,false);
        adapter = new GalleryAdapter();
        recyclerView = view.findViewById(R.id.galleryRecycler);
        manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


        File[] imageList = getContext().getDir("HomeCleanProject",Context.MODE_PRIVATE).listFiles();

        for(File file : imageList){
            filePath = file.getAbsolutePath();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);

            adapter.addItem(bitmap);
            adapter.notifyDataSetChanged();
        }
        return view;
    }
}