package com.hj.homecleanproject.customDialog;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hj.homecleanproject.R;

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
            init();
        }

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
}