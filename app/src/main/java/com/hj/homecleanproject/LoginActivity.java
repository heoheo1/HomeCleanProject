package com.hj.homecleanproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hj.homecleanproject.customInterface.onBackPressedListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout_SignIn;
    FragmentManager fm;
    FragmentTransaction ft;
    private long lastTimeBackPressed;
    SignInFragment signInFragment;
    Button btn_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);


        signInFragment =new SignInFragment();

        btn_login =findViewById(R.id.login);
        layout_SignIn=findViewById(R.id.layout_SignIn);

        btn_login.setOnClickListener(this);
        layout_SignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==btn_login){
            Intent intent =new Intent(LoginActivity.this,FragmentActivity.class);
            startActivity(intent);
            finish();
        }else if(v==layout_SignIn){
            frgManagerBeginTransaction(R.id.login_layout, signInFragment); //프래그먼트 매니저를 계속 생성해주어야함
        }

    }
    @Override
    public void onBackPressed() {

        //프래그먼트 onBackPressedListener사용
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        getSupportFragmentManager().getBackStackEntryCount();
        for(Fragment fragment : fragmentList){
            if(fragment instanceof onBackPressedListener){
                ((onBackPressedListener)fragment).onBackPressed();
                return;
            }
        }

        //두 번 클릭시 어플 종료
        if(System.currentTimeMillis() - lastTimeBackPressed < 2000){
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this,"'뒤로' 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();

    }

    public void frgManagerBeginTransaction(int layout,Fragment fragment){
        fm =getSupportFragmentManager();
        ft =fm.beginTransaction();
        ft.replace(layout,fragment,null);
        ft.commit();
    }

}