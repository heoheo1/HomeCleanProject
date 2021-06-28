package com.hj.homecleanproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.hj.homecleanproject.customDialog.GridDialogFragment;
import com.hj.homecleanproject.customInterface.onDialogResultListener;

public class MyWork_View extends LinearLayout {
    ImageView imageView;
    TextView textView;
    CardView cardView;


    public MyWork_View(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.show_my_work, this, true);
        imageView = findViewById(R.id.CardViewimage);
        textView = findViewById(R.id.CardViewText);
        cardView = findViewById(R.id.cardView);

    }

    public void setImageView(int resID){
        imageView.setImageResource(resID);
    }

    public void setTextView(String text){
        textView.setText(text);
    }

    public void setImageViewToBitmap(byte[] resID){
        //encoder된 이미지를 다시 decode해 ImageView에 붙인다.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeByteArray(resID,0,resID.length,options);
        imageView.setImageBitmap(bitmap);
    }

    public void setScaleTypeImageView(ImageView.ScaleType scaleType){
        imageView.setScaleType(scaleType);
    }
}
