package com.hj.homecleanproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MyFamilyFragment extends Fragment {

    ViewGroup viewGroup;
    FragmentActivity fragmentActivity;
    RecyclerView recyclerView;
    ArrayList<FamilyItem> familyItemArrayList;

    public static MyFamilyFragment newInstance() {
        return new MyFamilyFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentActivity=(FragmentActivity)getActivity();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_my_family, container, false);

        recyclerView =viewGroup.findViewById(R.id.recyclerView);

        familyItemArrayList=new ArrayList<>();
        familyItemArrayList.add(new FamilyItem("아이유","딸","01033333333",R.drawable.iu));
        familyItemArrayList.add(new FamilyItem("아이유","딸","01033333333",R.drawable.iu));
        familyItemArrayList.add(new FamilyItem("아이유","딸","01033333333",R.drawable.iu));
        familyItemArrayList.add(new FamilyItem("아이유","딸","01033333333",R.drawable.iu));
        familyItemArrayList.add(new FamilyItem("아이유","딸","01033333333",R.drawable.iu));

        FamilyAdapter familyAdapter =new FamilyAdapter(familyItemArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentActivity));
        recyclerView.setAdapter(familyAdapter);



        return viewGroup;
    }
}