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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hj.homecleanproject.customDialog.GridDialogFragment;
import com.hj.homecleanproject.customInterface.GetCustomView;
import com.hj.homecleanproject.customInterface.ImageViewClickListener;
import com.hj.homecleanproject.customInterface.TextViewClickListener;
import com.hj.homecleanproject.customInterface.onDialogResultListener;

import java.util.ArrayList;

public class MyGridAdapter extends BaseAdapter {

    ArrayList<MyWork> list = new ArrayList<>();

    private ImageViewClickListener listener;
    private TextViewClickListener textViewClickListener;
    private GridDialogFragment dialog;
    private GetCustomView getCustomView;
    MyWork_View view;
    MyWork work;
    String name;

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

        view.setUserName(name);

        if (list.get(position).getEncodeResID() != null) {
            view.setImageViewToBitmap(list.get(position).getEncodeResID());
        }

        work = list.get(position);


        //기존 없는 이미지 -> 카메라모양의 이미지를 Drawable을 통해서 이용할 예정
        view.setTextView(work.getContent());

        MyWork_View finalView = view;
        view.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (listener != null) {
                listener.adapterToFragment(intent, position, finalView);
            }
        });

        if (getCustomView != null) {
            getCustomView.getCustomView(finalView, position);
        }


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

    public void setOnCustomView(GetCustomView getCustomView) {
        this.getCustomView = getCustomView;
    }

    public void remove(int position) {
        list.remove(position);
    }

}
