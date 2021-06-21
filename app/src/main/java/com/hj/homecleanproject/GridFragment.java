package com.hj.homecleanproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
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


public class GridFragment extends Fragment{


    ViewGroup viewGroup;
    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;
    FragmentActivity fragmentActivity;
    ImageView cardView;

    NavigationView navigation;
    private GridDialogFragment dialog;

    File photoFile;
    Bitmap bitmap;
    int adapterPosition;

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

        adapter.setOnImageViewClickListener(new ImageViewClickListener() {
            @Override
            public void adapterToFragment(Intent intent, int position) {
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

            Resources resources = Resources.getSystem(); //어느곳에서든지 이렇게 사용하면 context의 내용을 사용할수있다. 무조건! (직접적인 접근 가능)
            Configuration configuration = resources.getConfiguration();

            // 카메라를 가로/세로로 전환시 Destory까지 내려가 팅기는 현상 발생 -> ConfigurationChanged 사용
            if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) { //세로일때
                Matrix matrix = new Matrix();
                matrix.postRotate(90); //이미지가 90도 돌아간상태로 등장하기때문에 원상태로 돌리기위해 90도 회전

                //좌표값 (0,0) 에서 (이미지의 넓이, 이미지의 높이)까지
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            //Bitmap 을 구한후, 디코딩
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,40,output);
            byte[] bytes = output.toByteArray();

            ((MyWork) adapter.getItem(adapterPosition)).setResID(0);
            ((MyWork) adapter.getItem(adapterPosition)).setEncodeResID(bytes);
            adapter.notifyDataSetChanged();
        }
    }
}
