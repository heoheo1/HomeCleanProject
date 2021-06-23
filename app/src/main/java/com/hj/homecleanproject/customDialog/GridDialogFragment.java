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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hj.homecleanproject.R;
import com.hj.homecleanproject.customInterface.onDialogResultListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class GridDialogFragment extends DialogFragment {

    TextView dialogBtnOk, dialogBtnCancel;
    EditText dialogContents;

    String contents;


    onDialogResultListener result;

    public GridDialogFragment(String contents){ // GridView에 사진과 내용이 있을때
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
        setRetainInstance(true); //Fragment의 생명주기를

        if(contents != null) {
            init();
        }

        dialogBtnCancel.setOnClickListener(v -> {
            dismiss();
        });

        dialogBtnOk.setOnClickListener(v -> { //확인버튼을 눌렀을때
            contents = dialogContents.getText().toString(); // Contents 내용

            if(result != null) {
                result.onMyDialogResult(contents); //interface로 선언해 dialog와 fragment사이에 데이터 전달하기
            }
            dismiss();
        });
        return view;
    }

    public void init(){
        dialogContents.setText(contents);
    }

    public void setDialogResult (onDialogResultListener listener){
        result = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.fragment_horizontal_margin);
        int height = getResources().getDimensionPixelSize(R.dimen.fragment_vertical_margin);
        getDialog().getWindow().setLayout(width,height);
    }


}