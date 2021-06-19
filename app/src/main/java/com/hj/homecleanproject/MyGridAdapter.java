package com.hj.homecleanproject;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class MyGridAdapter extends BaseAdapter {

    ArrayList<MyWork> list = new ArrayList<>();

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
        view.setImageView(work.getResID());
        view.setTextView(work.getContent());

        return view;
    }

    public void addItem(MyWork work){
        list.add(work);
    }
}
