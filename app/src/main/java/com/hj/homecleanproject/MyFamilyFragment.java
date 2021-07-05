package com.hj.homecleanproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


public class MyFamilyFragment extends Fragment {

    ViewGroup viewGroup;
    FragmentActivity fragmentActivity;
    RecyclerView recyclerView;
    ArrayList<FamilyItem> familyItemArrayList;
    FirebaseAuth auth;
    String email;
    String name;
    String position;
    String groupName;
    String url;
    DatabaseReference realtime;
    Handler handler;

    FirebaseFirestore db;

    Bitmap bitmap;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentActivity=(FragmentActivity)getActivity();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_my_family, container, false);
        init();

        recyclerView =viewGroup.findViewById(R.id.recyclerView);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        realtime= FirebaseDatabase.getInstance().getReference("users");


        String uid =currentUser.getUid();

        Log.d("uid",uid);
        realtime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator(); //아이터레이터를 얻어온다.
                while (child.hasNext()) { //최상위 디렉토리를 가르키고있다. 다음이 있냐?
                    if(child.next().getKey().equals(uid)){
                        groupName = snapshot.child(uid).getValue().toString();
                        Log.d("yousin","groupName : " +groupName);
                        profileSave();
                    }
                }
                realtime.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return viewGroup;
    }

    public void init(){
        familyItemArrayList=new ArrayList<>();
        handler = new Handler();
    }

    public void profileSave(){
        if(groupName!=null) {
            Log.d("yousin","groupName is null ?: "+ groupName);
        }
        db.collection(groupName).whereEqualTo("groupName",groupName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d("yousin",document.getId()+" => "+document.getData());
                        UserInfo userInfo = document.toObject(UserInfo.class);
                        Log.d("yousin","userInfo.getGroupName : "+userInfo.getGroupName());
                        Log.d("yousin",userInfo.getName());
                        Log.d("yousin",userInfo.getEmail());
                        Log.d("yousin",userInfo.getPosition());

                        url =userInfo.getUrl();

                        groupName =userInfo.getGroupName();
                        name =userInfo.getName();
                        email =userInfo.getEmail();
                        position=userInfo.getPosition();

                            Observable<UserInfo> observable = Observable.create(emitter -> {
                                emitter.onNext(userInfo);
                            });

                            observable.subscribeOn(AndroidSchedulers.mainThread())
                                    .observeOn(Schedulers.io())
                                    .subscribe(data -> {
                                        url =data.getUrl();
                                        URL url2 = new URL(url);
                                        HttpURLConnection connection =(HttpURLConnection) url2.openConnection();
                                        connection.setDoInput(true);
                                        connection.connect();

                                        InputStream inputStream = connection.getInputStream();
                                        bitmap = BitmapFactory.decodeStream(inputStream);

                                        handler.post(() -> {

                                            groupName =data.getGroupName();
                                            name =data.getName();
                                            email =data.getEmail();
                                            position=data.getPosition();
                                            Log.d("yousin","url : "+url);
                                            Log.d("yousin","groupName : "+groupName);
                                            Log.d("yousin","name : "+ name);
                                            Log.d("yousin","email" + email);
                                            Log.d("yousin","position : " +position);
                                            familyItemArrayList.add(new FamilyItem(groupName,name,email,position,bitmap));
                                            FamilyAdapter familyAdapter =new FamilyAdapter(familyItemArrayList);
                                            GridLayoutManager gridLayoutManager =new GridLayoutManager(getActivity(),2);

                                            recyclerView.setLayoutManager(gridLayoutManager);
                                            // recyclerView.setLayoutManager(new LinearLayoutManager(fragmentActivity));
                                            recyclerView.setAdapter(familyAdapter);
                                            familyAdapter.notifyDataSetChanged();

                                        });

                                    }, e -> {
                                        e.printStackTrace();
                                    });
                    }
                }
            }
        });
    }
}