package com.hj.homecleanproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedImageDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    MyWork_View view;
    MyWork work;

    Animation animation;

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
        view = null;

        if (convertView == null) {
            view = new MyWork_View(parent.getContext());
        } else {
            view = (MyWork_View) convertView;
        }

        work = list.get(position);

        //여기를 바꿔야함 -> 기본 사진일때와, 새로운 사진이 들어갈때
        //현재는 무조건 byte[] 의 값이 image에 찎힘
        if(list.get(position).getResID() == R.drawable.baseline_add_a_photo_black_18){
            view.setImageViewToBitmap(list.get(position).getEncodeResID());
        }

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                list.remove(position);
                return false;
            }
        });

        //기존 없는 이미지 -> 카메라모양의 이미지를 Drawable을 통해서 이용할 예정
        view.setTextView(work.getContent());

        MyWork_View finalView = view;
        view.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (listener != null) {
                listener.adapterToFragment(intent, position, finalView);
            }
        });

        view.textView.setOnClickListener(v -> {
            dialog = new GridDialogFragment();
            if (textViewClickListener != null) {
                textViewClickListener.textViewClicked(dialog, position);
            }
        });

        return view;
    }

    public void addItem(MyWork work) {
        list.add(work);
    }

    public void setOnImageViewClickListener(ImageViewClickListener listener) {
        this.listener = listener;
    }

    public void setOnTextViewClickListener(TextViewClickListener listener) {
        textViewClickListener = listener;
    }

    public ImageView getImageView() {
        return view.imageView;
    }

    public void remove(int position){
        list.remove(position);
    }
}
