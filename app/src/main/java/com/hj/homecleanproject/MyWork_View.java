package com.hj.homecleanproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyWork_View extends LinearLayout {
    ImageView imageView;
    TextView textView;

    public MyWork_View(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.show_my_work,this,true);
        imageView = findViewById(R.id.CardViewimage);
        textView = findViewById(R.id.CardViewText);
    }

    public void setImageView(int resID){
        imageView.setImageResource(resID);
    }

    public void setTextView(String text){
        textView.setText(text);
    }
}
