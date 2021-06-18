package com.hj.homecleanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hj.homecleanproject.customInterface.onBackPressedListener;

import java.util.List;

public class IntroActivity extends AppCompatActivity {


    Handler handler = new Handler();
    final int delayed = 2500;
    Animation logo;
    LinearLayout intro_Layout;
    FrameLayout intro;

    FragmentManager fragmentManager;

    BottomNavigationView bottomNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        intro_Layout = findViewById(R.id.introlayout);
        intro = findViewById(R.id.intro);

        //화면전환 프래그먼트 선언
        fragmentManager = getSupportFragmentManager();

        logo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_animation);
        intro_Layout.startAnimation(logo);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager.beginTransaction().add(R.id.intro, new LoginFragment().newInstance(), null).commit();
                onWindowFocusChanged(false);
            }
        }, delayed);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void replaceFragment(Fragment fragment) {
        //Fragment를 전환할 때 이 메소드를 사용
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.intro, fragment).commit();
//        Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.

        fragmentManager.beginTransaction().replace(R.id.intro, fragment, null).commit();
    }

    @Override
    public void onBackPressed() {
        //프래그먼트들의 리스트를 만들어
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                //만약 프래그먼트가 BackPressClick을 implements하고있다면
                if (fragment instanceof onBackPressedListener) {
                    //해당 fragment의 onBackPressed를 실행하고
                    ((onBackPressedListener) fragment).onBackPressed();
                }
            }
        }
    }//onBackPressed()
}


