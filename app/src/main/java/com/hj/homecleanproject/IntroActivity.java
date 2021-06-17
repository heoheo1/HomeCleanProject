package com.hj.homecleanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class IntroActivity extends AppCompatActivity {


    Handler handler =new Handler();
    final int delayed =2500;
    Animation logo;
    LinearLayout intro_Layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        intro_Layout=findViewById(R.id.introlayout);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(IntroActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        },delayed);
        logo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_animation);
        intro_Layout.startAnimation(logo);

    }
}

