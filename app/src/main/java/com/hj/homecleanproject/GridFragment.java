package com.hj.homecleanproject;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;


//Fragment란, 어플리케이션에서 화면에 직접 보이는 공간의 Activity내에서 분할시키고 다른 화면으로 전환할 수 있는 화면 공간의 단위
//Fragment를 이용하려면, 상위에 있는 Activity에서 출력할 layout을 제어
//하지만 Fragment 내부에서 다른 Fragment로 이동하는 것은 그 Fragment가 자신의 하위레벨이 아니기 때문에
//내부에서 직접 제어할 수 없으므로 , 상위 레벨인 Activity를 호출하여 제어하는 형태가 되어야 함

//onActivityResult -> deprecated -> 확인하기
public class GridFragment extends Fragment{

    ViewGroup viewGroup;
    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;
    FragmentActivity fragmentActivity;
    ImageView cardView;

    NavigationView navigation;
    private GridDialogFragment dialog;
    MyWork_View layoutView;
    LinearLayout fabLayout;

    File photoFile;
    Bitmap bitmap;
    int adapterPosition;

    FirebaseStorage storage;
    StorageReference reference;
    String groupName, memberName;

    byte[] bytes;
    String msg = "";
    long size = 0;

    Map<String, Object> myData;
    FirebaseFirestore db;


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
        fabLayout = viewGroup.findViewById(R.id.fabLayout);

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        myData = new HashMap<>();
        db = FirebaseFirestore.getInstance();

        fabLayout.setOnClickListener(v -> {
            adapter.addItem(new MyWork(R.drawable.baseline_add_a_photo_black_18,""));

            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_grid_anim);
            gridView.setLayoutAnimation(controller);

            adapter.notifyDataSetChanged();
        });




        adapter.setOnImageViewClickListener(new ImageViewClickListener() {
            @Override
            public void adapterToFragment(Intent intent, int position, View view) {
                adapterPosition = position;
                if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    try{
                        photoFile = createFile();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(photoFile != null){
                        //provider를 사용해 외부앱과 연결하여 데이터를 Uri형식으로 연결한다.
                        Uri photoUri = FileProvider.getUriForFile(getContext(),"com.hj.homecleanproject.fileprovider",photoFile);
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, 1000);
                    }
                }
                layoutView = (MyWork_View) view;
            }
        });
        adapter.setOnTextViewClickListener(new TextViewClickListener() {
            @Override
            public void textViewClicked(DialogFragment dialog, int position) {
                adapterPosition = position;
                dialog.show(getActivity().getSupportFragmentManager(),"tag");
                GridDialogFragment dialogFragment = (GridDialogFragment) dialog;
                dialogFragment.setDialogResult(new onDialogResultListener() {
                    @Override
                    public void onMyDialogResult(String contents) {
                        msg = contents;

                        ((MyWork) adapter.getItem(adapterPosition)).setContent(contents);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return viewGroup;
    }

    public File createFile() throws IOException {
        String fileName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //Data/data/패키지/에 app_HomeCleanProject라는 파일이 생성된다.
        File file = File.createTempFile(fileName,".jpg",getContext().getDir("HomeCleanProject", Context.MODE_PRIVATE));

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 용량을 4분의1로 낮춤
            bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options); //photoFile로 만든 이미지를 bitmap으로 형식변경
            layoutView.setScaleTypeImageView(ImageView.ScaleType.FIT_XY);

            Resources resources = Resources.getSystem(); //어느곳에서든지 이렇게 사용하면 context의 내용을 사용할수있다. 무조건! (직접적인 접근 가능)
            Configuration configuration = resources.getConfiguration();

            // 카메라를 가로/세로로 전환시 Destory까지 내려가 팅기는 현상 발생 -> ConfigurationChanged 사용
            if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) { //세로일때
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
                }catch (IOException e) {
                    e.printStackTrace();
                }

            }
            //Bitmap 을 구한후, 디코딩
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,40,output);
            byte[] bytes = output.toByteArray();

            size++;
            myData.put("size",size);
            setData(bytes);

            ((MyWork) adapter.getItem(adapterPosition)).setResID(0);
            ((MyWork) adapter.getItem(adapterPosition)).setEncodeResID(bytes);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        Observable<Map<String,Object>> observable = Observable.create(emitter -> {
           Task<DocumentSnapshot> task = db.collection("yousin").document("yousin").get();
           task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
               public void onSuccess(DocumentSnapshot documentSnapshot) {
                   long leng = (Long) documentSnapshot.get("size");
                   for(int i = 0; i <= leng; i++) {
                       if (documentSnapshot.get("content" + i) != null) {
                           String name = documentSnapshot.get("content" + i).toString();
                           Blob blob = documentSnapshot.getBlob("position" + i);

                           byte[] byteArray = blob.toBytes();

                           adapter.addItem(new MyWork(byteArray,name));
                           adapter.notifyDataSetChanged();
                       }
                   }
               }
           });
        });
    }

    public void setData(byte[] bytes){

        Observable<Object> observable = Observable.create(emitter -> {
           myData.put("position"+adapterPosition,Blob.fromBytes(bytes));
           myData.put("content"+adapterPosition,msg);
           emitter.onNext(myData);
           emitter.onComplete();
        });

        observable.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                   db.collection("yousin").document("yousin").set(data, SetOptions.merge());
                });
    }
}
