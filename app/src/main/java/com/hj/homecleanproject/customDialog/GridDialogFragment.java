package com.hj.homecleanproject.customDialog;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hj.homecleanproject.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class GridDialogFragment extends DialogFragment {

    TextView dialogBtnOk, dialogBtnCancel;
    ImageView dialogImageView;
    EditText dialogContents;
    int resID = 0;
    String contents;

    public GridDialogFragment(int resID, String contents){ // GridView에 사진과 내용이 있을때
        this.resID = resID;
        this.contents = contents;
    }

    public GridDialogFragment(){ } //그냥 빈거일때

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid_dialog, container, false);

        dialogBtnOk = view.findViewById(R.id.dialog_OK);
        dialogBtnCancel = view.findViewById(R.id.dialog_CANCEL);
        dialogContents = view.findViewById(R.id.dialog_contents);
        dialogImageView = view.findViewById(R.id.dialog_imageView);

        if(resID == R.drawable.moon) {
            //달 그림이면 그 내용들을 setting
            //후, 기본그림일땐 아무내용없게 만들고, 그림이 있다면 그 내용으로 Dialog를 채울 예정
            init();
        }

        dialogImageView.setOnClickListener(v ->{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, 1000);
            }
        });

        dialogBtnCancel.setOnClickListener(v -> {
            dismiss();
        });

        dialogBtnOk.setOnClickListener(v -> {

        });
        return view;
    }

    public void init(){
        dialogImageView.setImageResource(resID);
        dialogContents.setText(contents);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if(requestCode == 1000){
            Bundle extra = intent.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            dialogImageView.setImageBitmap(bitmap);
        }

    }
}