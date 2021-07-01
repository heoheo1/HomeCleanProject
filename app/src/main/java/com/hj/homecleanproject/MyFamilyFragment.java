package com.hj.homecleanproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class MyFamilyFragment extends Fragment {

    ViewGroup viewGroup;
    FragmentActivity fragmentActivity;
    RecyclerView recyclerView;
    ArrayList<FamilyItem> familyItemArrayList;
    FirebaseFirestore db;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth auth;
    String url;
    String group;
    String email;
    String name;
    String position;
    Uri uri;



    public static MyFamilyFragment newInstance() {
        return new MyFamilyFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentActivity=(FragmentActivity)getActivity();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_my_family, container, false);

        recyclerView =viewGroup.findViewById(R.id.recyclerView);



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String eamil =currentUser.getEmail();
        SharedPreferences prf = fragmentActivity.getSharedPreferences("test", Context.MODE_PRIVATE);
        String groupName = prf.getString("groupName",null);


        if(groupName!=null) {
            Log.d("dd", groupName);
        }
        db.collection(groupName).whereEqualTo("email",eamil).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult()){

                        Log.d("Data",document.getId()+" => "+document.getData());
                        UserInfo userInfo = document.toObject(UserInfo.class);
                        Log.d("dd",userInfo.getGroupName());
                        Log.d("dd",userInfo.getName());
                        Log.d("dd",userInfo.getEmail());
                        Log.d("dd",userInfo.getPosition());
                        Log.d("dd",userInfo.getUrl());
                        url =userInfo.getUrl();

                         group =userInfo.getGroupName();
                         name =userInfo.getName();
                        email =userInfo.getEmail();
                        position=userInfo.getPosition();
                         uri = Uri.parse(url);

                        familyItemArrayList=new ArrayList<>();
                        familyItemArrayList.add(new FamilyItem(group,name,email,position,uri));
                        FamilyAdapter familyAdapter =new FamilyAdapter(familyItemArrayList);
                        GridLayoutManager gridLayoutManager =new GridLayoutManager(getActivity(),2);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        // recyclerView.setLayoutManager(new LinearLayoutManager(fragmentActivity));
                        recyclerView.setAdapter(familyAdapter);
                        familyAdapter.notifyDataSetChanged();



                    }
                }
            }
        });











        return viewGroup;
    }
}