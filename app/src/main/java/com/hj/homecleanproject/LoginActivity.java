package com.hj.homecleanproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hj.homecleanproject.customInterface.onBackPressedListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    long lastPressed =0;
    TextView txtSinUp;
    FragmentManager fm;
    FragmentTransaction ft;
    private long lastTimeBackPressed;
    MakeLoginFragment makeLoginFragment;
    Animation login;
    LinearLayout layout_anim,layout_anim2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout_anim=findViewById(R.id.login_anim);
        layout_anim2=findViewById(R.id.login_anim2);


        makeLoginFragment=new MakeLoginFragment();

        Button button =findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,FragmentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtSinUp=findViewById(R.id.makeLogin);



        txtSinUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                frgManagerbeginTransaction(R.id.login_layout,makeLoginFragment); //프래그먼트 매니저를 계속 생성해주어야함

            }
        });



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

    public void frgManagerbeginTransaction(int layout,Fragment fragment){
        fm =getSupportFragmentManager();
        ft =fm.beginTransaction();
        ft.replace(layout,fragment,null);
        ft.commit();
    }






}