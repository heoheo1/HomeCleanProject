package com.hj.homecleanproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.hj.homecleanproject.customInterface.ImageViewClickListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

import io.reactivex.rxjava3.core.Observable;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class PictureFragment extends Fragment {

    private GalleryAdapter adapter;
    private GridFragment gridFragment;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager manager;
    Handler handler;

    private String groupName, name, email, position;
    private String filePath;
    private Bitmap bitmap;
    private File[] imageList;

    FirebaseStorage firebaseStorage;
    StorageReference reference;
    int adapterPosition;

    FirebaseFirestore db;
    FirebaseUser currentUser;
    DatabaseReference realtime;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        init(view);

        String uid = currentUser.getUid();

        realtime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator(); //????????????????????? ????????????.
                while (child.hasNext()) {
                    if (child.next().getKey().equals(uid)) {
                        groupName = snapshot.child(uid).getValue().toString();
                    }
                }
                getProfile();
                //realtime.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); //cancel

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        imageList = getContext().getDir("HomeCleanProject", Context.MODE_PRIVATE).listFiles();

        adapter.setOnImageViewLongClickListener(position -> {
            adapterPosition = position;
            firebaseStorage.getReferenceFromUrl("gs://homeclean-ba4fc.appspot.com").child(groupName + "/").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    for (StorageReference list : listResult.getItems()) {
                        adapterPosition--;
                        Log.d("yousin",adapterPosition+" position");
                        Log.d("yousin",list.listAll()+"");
                        if(adapterPosition == -1){
                            Log.d("yousin",list.getPath());
                            firebaseStorage.getReferenceFromUrl("gs://homeclean-ba4fc.appspot.com").child(list.getPath()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext().getApplicationContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            handler.post(() -> {
                                adapter.removeItem(position);
                                adapter.notifyDataSetChanged();
                            });
                        }
                    }
                }
            });
        });

        adapter.setOnImageViewClickListener(new ImageViewClickListener() {
            @Override
            public void adapterToFragment(Intent intent, int position, View view) {
                Intent intent2 = new Intent(getContext(), ShowPictureActivity.class);
                Bitmap bitmap = ((Bitmap) (adapter.get(position)));

                ByteArrayOutputStream output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, output);
                byte[] bytes = output.toByteArray();

                intent2.putExtra("byte", bytes);
                startActivity(intent2);
            }
        });


//        //Ui ???????????? ?????? ?????? ?????? -> just???????????? UI Blocking??? ??????
//        //????????? create???????????? ????????? ?????? subscribe???
//        Observable<String> filePath = Observable.create(subscriber -> { //?????????
//            try {
//                if (!subscriber.isDisposed()) {
//                    for (File file : imageList) {
//                        subscriber.onNext(file.getAbsolutePath()); //????????? ??????
//                    }
//                }
//                subscriber.onComplete(); //????????? ????????? ?????????????????? ??????
//            } catch (Exception e) {
//                subscriber.onError(e);
//            }
//        });
//
//        filePath.observeOn(Schedulers.io()) // ?????????, ????????? ?????? ?????? ???????????? ?????? ???????????????.
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(data -> { //????????? data??? ?????? bitmap??? decode??????.
//                            bitmap = BitmapFactory.decodeFile((String) data, options);
//
//                            handler.post(() -> {
//                                adapter.addItem(bitmap);
//                                adapter.notifyDataSetChanged();
//                            });
//                        }
//                );
        return view;
    }

    public void init(View view) { //?????????
        adapter = new GalleryAdapter();
        recyclerView = view.findViewById(R.id.galleryRecycler);
        gridFragment = GridFragment.newInstance();
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        firebaseStorage = FirebaseStorage.getInstance();
        reference = firebaseStorage.getReference();
        handler = new Handler();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        realtime = FirebaseDatabase.getInstance().getReference("users");
    }

    public void getProfile() {
        db.collection(groupName).whereEqualTo("groupName", groupName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserInfo userInfo = document.toObject(UserInfo.class);

                        groupName = userInfo.getGroupName();
                        Log.d("yousin", groupName);
                        name = userInfo.getName();
                        email = userInfo.getEmail();
                        position = userInfo.getPosition();
                    }
                    restoreMyDate();
                }

            }
        });
    }

    public void restoreMyDate() {
        firebaseStorage.getReferenceFromUrl("gs://homeclean-ba4fc.appspot.com").child(groupName + "/").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference list : listResult.getItems()) {
                    list.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Observable<Uri> observable = Observable.create(emitter -> {
                                emitter.onNext(uri);
                            });

                            observable.subscribeOn(AndroidSchedulers.mainThread())
                                    .observeOn(Schedulers.io())
                                    .subscribe(data -> {
                                        URL url = new URL(data.toString());

                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.setDoInput(true);
                                        connection.connect();

                                        InputStream inputStream = connection.getInputStream();
                                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                        handler.post(() -> {
                                            adapter.addItem(bitmap);
                                            adapter.notifyDataSetChanged();
                                        });

                                    }, e -> {
                                        e.printStackTrace();
                                    });
                        }
                    });
                }
            }
        });
    }
}