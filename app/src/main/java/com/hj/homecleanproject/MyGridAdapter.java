package com.hj.homecleanproject;

import android.app.Dialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hj.homecleanproject.customDialog.GridDialogFragment;
import com.hj.homecleanproject.customInterface.ImageViewClickListener;
import com.hj.homecleanproject.customInterface.TextViewClickListener;
import com.hj.homecleanproject.customInterface.onDialogResultListener;

import java.util.ArrayList;

public class MyGridAdapter extends BaseAdapter {

    ArrayList<MyWork> list = new ArrayList<>();

    private ImageViewClickListener listener;
    private TextViewClickListener textViewClickListener;
    private GridDialogFragment dialog;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyWork_View view = null;

        if(convertView == null){
            view = new MyWork_View(parent.getContext());
        }else{
            view = (MyWork_View) convertView;
        }



        MyWork work = list.get(position);

        //기존 없는 이미지 -> 카메라모양의 이미지를 Drawable을 통해서 이용할 예정
        if(list.get(position).getResID() == 0){ //만약 카메라에서 사진을 찍었다면, 그 사진을 붙이고

            view.setImageViewToBitmap(work.getEncodeResID());
        }else{ // 이용안한 카드뷰라면? 기본 이미지를 사용하도록 한다.
            view.setImageView(work.getResID());
        }
        view.setTextView(work.getContent());

        view.imageView.setOnClickListener(v ->{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(listener != null){
                listener.adapterToFragment(intent,position);
            }
        });

        view.textView.setOnClickListener(v -> {
            dialog = new GridDialogFragment();
            if(textViewClickListener != null){
                textViewClickListener.textViewClicked(dialog, position);
            }
        });

        return view;
    }

    public void addItem(MyWork work){
        list.add(work);
    }

    public void setOnImageViewClickListener(ImageViewClickListener listener){
        this.listener = listener;
    }

    public void setOnTextViewClickListener(TextViewClickListener listener){
        textViewClickListener = listener;
    }
}
