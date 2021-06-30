package com.hj.homecleanproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hj.homecleanproject.customDialog.GridDialogFragment;
import com.hj.homecleanproject.customInterface.ImageViewClickListener;
import com.hj.homecleanproject.customInterface.TextViewClickListener;
import com.hj.homecleanproject.customInterface.onDialogResultListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



//Fragment란, 어플리케이션에서 화면에 직접 보이는 공간의 Activity내에서 분할시키고 다른 화면으로 전환할 수 있는 화면 공간의 단위
//Fragment를 이용하려면, 상위에 있는 Activity에서 출력할 layout을 제어
//하지만 Fragment 내부에서 다른 Fragment로 이동하는 것은 그 Fragment가 자신의 하위레벨이 아니기 때문에
//내부에서 직접 제어할 수 없으므로 , 상위 레벨인 Activity를 호출하여 제어하는 형태가 되어야 함

public class GridFragment extends Fragment {

    ViewGroup viewGroup;
    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;
    FragmentActivity fragmentActivity;

    File photoFile; // 찍은 사진에 대한 임시파일
    Bitmap bitmap; // 내장 카메라에서 찍은 사진파일형식
    int adapterPosition; //내가 선택한 cardView의 Position
    String collections, documents; // 그룹명, 유저이름
    LinearLayout fabLayout; // 누르면 CardView 추가되는 Layout
    Map<String, Object> myData; // FireStore에 넣어 주기 위한 Map
    String fileName; // TimeStamp! 현재 시간을 뽑아냄

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference reference;

    public static GridFragment newInstance() {
        return new GridFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_grid, container, false);
        init(viewGroup); // 초기화

//        FirebaseApp.initializeApp(getContext());
//        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
//        firebaseAppCheck.installAppCheckProviderFactory(
//                SafetyNetAppCheckProviderFactory.getInstance());


        fabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new MyWork(R.drawable.baseline_add_a_photo_black_18, " "));
                myData.put("size", adapter.getCount());
                db.collection("yousin").document("yousin").set(myData,SetOptions.merge());
                adapter.notifyDataSetChanged();
            }
        });

        adapter.setOnImageViewClickListener(new ImageViewClickListener() {
            @Override
            public void adapterToFragment(Intent intent, int position, View view) {
                adapterPosition = position;
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    try {
                        photoFile = createFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (photoFile != null) {
                        //provider를 사용해 외부앱과 연결하여 데이터를 Uri형식으로 연결한다.
                        Uri photoUri = FileProvider.getUriForFile(getContext(), "com.hj.homecleanproject.fileprovider", photoFile);
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, 1000);
                    }
                }
            }
        });

        adapter.setOnTextViewClickListener(new TextViewClickListener() {
            @Override
            public void textViewClicked(DialogFragment dialog, int position) {
                adapterPosition = position;
                dialog.show(getActivity().getSupportFragmentManager(), "tag");
                GridDialogFragment dialogFragment = (GridDialogFragment) dialog;
                dialogFragment.setDialogResult(new onDialogResultListener() {
                    @Override
                    public void onMyDialogResult(String contents) {
                        myData.put("myContentsTo" + adapterPosition, contents);
                        db.collection("yousin").document("yousin").set(myData, SetOptions.merge());
                        ((MyWork) adapter.getItem(adapterPosition)).setContent(contents);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreMyData();
    }

    public void restoreMyData() {
        db.collection("yousin").document("yousin").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Long size = documentSnapshot.getLong("size") == null ? 0 : documentSnapshot.getLong("size");
                myData.put("size",size);
                if(size > 0){
                    for(int i = 0; i<size; i++){
                        String msg = String.valueOf(documentSnapshot.get("myContentsTo"+i)) == "null" ? " " : String.valueOf(documentSnapshot.get("myContentsTo"+i));
                        myData.put("myContentsTo"+i,msg);
                        Log.d("yousin",myData.get("myContentsTo"+i)+" : contents"+i);

                        String imgName = String.valueOf(documentSnapshot.get("ImageNameTo"+i)) == "null" ? " " :String.valueOf(documentSnapshot.get("ImageNameTo"+i));
                        myData.put("ImageNameTo"+i,imgName);
                        Log.d("yousin",myData.get("ImageNameTo"+i)+"");

                        if(imgName.equals(" ")) {
                            adapter.addItem(new MyWork(R.drawable.baseline_add_a_photo_black_18, msg));
                        }else{
                            StorageReference imgReference = storage.getReferenceFromUrl("gs://homeclean-ba4fc.appspot.com").child("userName/"+imgName);
                            Log.d("yousin",imgReference.getPath());

                            imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(fragmentActivity, "잘 다운로드됨", Toast.LENGTH_SHORT).show();
                                    String frontUrl = "https://firebasestorage.googleapis.com";
                                    String name =uri.getEncodedPath();
                                    name.replace(" ","%20");
                                    String backUrl="?alt=media";
                                    String resultUrl = frontUrl + name +backUrl;
                                    Log.d("re",resultUrl);
                                    //Bitmap bitmap = null;
                                    try {
                                        URL url = new URL(resultUrl);
                                       ImageView im=adapter.getImageView();
                                        imgTask(url.toString(),im);

//                                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                                        ByteArrayOutputStream output = new ByteArrayOutputStream();
//                                        bitmap.compress(Bitmap.CompressFormat.PNG, 40, output);
//                                        byte[] bytes = output.toByteArray();
//                                        adapter.addItem(new MyWork(bytes,msg));


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("yousin",name);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });

                            Log.d("yousin","끝");
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void imgTask(String imgUrl, ImageView imageView) {

        ImageLoadTask task = new ImageLoadTask(imgUrl, imageView);
        task.execute();

    }

    public void init(ViewGroup viewGroup) { //초기화
        fragmentActivity = (FragmentActivity) getActivity();
        gridView = viewGroup.findViewById(R.id.grid);
        fabLayout = viewGroup.findViewById(R.id.fabLayout);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        workList = new ArrayList<>();
        adapter = new MyGridAdapter();
        myData = new HashMap<>();

        gridView.setAdapter(adapter);
    }

    public File createFile() throws IOException { //임시파일생성
        fileName = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        //Data/data/패키지/에 app_HomeCleanProject라는 파일이 생성된다.
        File file = File.createTempFile(fileName, ".png", getContext().getDir("HomeCleanProject", Context.MODE_PRIVATE));

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 용량을 4분의1로 낮춤 -> 이미지는 용량이 크기 때문에 임의로 줄이지 않으면 앱이 죽어버릴수있음
            bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options); //photoFile로 만든 이미지를 bitmap으로 형식변경

            ChangeRotate(); //이미지 각도 돌리기

            //Bitmap 을 구한후, 디코딩 -> Bitmap을 통째로 넘기기는 좀 그러니,
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, output);
            byte[] bytes = output.toByteArray();

            //FirebaseStorage에 저장하기 -> userName을 collection으로 바꿀 예정
            storage.getReferenceFromUrl("gs://homeclean-ba4fc.appspot.com").child("userName/" + fileName + ".png".trim()).putBytes(bytes);


            myData.put("ImageNameTo" + adapterPosition, fileName+".png".trim());
            db.collection("yousin").document("yousin").set(myData);


            ((MyWork) adapter.getItem(adapterPosition)).setResID(0);
            ((MyWork) adapter.getItem(adapterPosition)).setEncodeResID(bytes);
            adapter.notifyDataSetChanged();
        }
    }


    public void ChangeRotate() { //이미지 각도 돌리기
        Resources resources = Resources.getSystem(); //어느곳에서든지 이렇게 사용하면 context의 내용을 사용할수있다. 무조건! (직접적인 접근 가능)
        Configuration configuration = resources.getConfiguration();

        // 카메라를 가로/세로로 전환시 Destory까지 내려가 팅기는 현상 발생 -> ConfigurationChanged 사용
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) { //세로일때
            ExifInterface exif = null; //이미지가 갖고 있는 정보의 집합 클래스다
            try {
                exif = new ExifInterface(photoFile.getCanonicalPath());

                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                int rotate = 0;

                switch (exifOrientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                }

                if (rotate != 0) {
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();

                    // Setting pre rotate
                    Matrix mtx = new Matrix();
                    mtx.preRotate(rotate);

                    // Rotating Bitmap & convert to ARGB_8888, required by tess
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
