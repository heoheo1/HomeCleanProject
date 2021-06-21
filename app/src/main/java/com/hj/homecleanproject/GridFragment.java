package com.hj.homecleanproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.hj.homecleanproject.customDialog.GridDialogFragment;
import com.hj.homecleanproject.customInterface.onDialogResultListener;

import java.util.ArrayList;


public class GridFragment extends Fragment{


    ViewGroup viewGroup;
    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;
    FragmentActivity fragmentActivity;
    ImageView cardView;

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


        workList = new ArrayList<>();
        adapter = new MyGridAdapter();
        gridView.setAdapter(adapter);
        navigation = viewGroup.findViewById(R.id.nav_view);

        adapter.addItem(new MyWork(R.drawable.iu, "화장실 청소 완료"));
        adapter.addItem(new MyWork(R.drawable.iu, "설거지 완료"));
        adapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyWork work = (MyWork) adapter.getItem(position);
                int resID = work.getResID();
                String contents = work.getContent();

                if (resID == R.drawable.moon) {
                    dialog = new GridDialogFragment(resID, contents);
                } else {
                    dialog = new GridDialogFragment();
                }
                dialog.setCancelable(false);
                dialog.show(getFragmentManager(), null);

                dialog.setDialogResult(new onDialogResultListener() {
                    @Override
                    public void onMyDialogResult(byte[] resID, String contents) {
                        ((MyWork) adapter.getItem(position)).setResID(0); //기존 CardView의 이미지는 없애고
                        ((MyWork) adapter.getItem(position)).setEncodeResID(resID); // Dialog의 이미지와
                        ((MyWork) adapter.getItem(position)).setContent(contents); // Contents의 내용을 붙인다.

                        adapter.notifyDataSetChanged();

                    }
                });
            }
        });

//        viewGroup.findViewById(R.id.hamburger).setOnClickListener(v -> { //hamburger button 클릭시
//            navigation.setVisibility(View.VISIBLE);
//        });
        return viewGroup;
    }
}
