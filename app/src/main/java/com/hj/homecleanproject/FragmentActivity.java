package com.hj.homecleanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //하단바
    private FragmentManager fm;
    private FragmentTransaction ft;
    private  GridFragment gridFragment;
    private MyFamilyFragment myFamilyFragment;
    private  LoginFragment loginFragment;
    private PictureFragment pictureFragment;
    private Service_Center_Fragment serviceCenterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        bottomNavigationView =findViewById(R.id.bottomNavi);

        gridFragment =new GridFragment();
        myFamilyFragment = new MyFamilyFragment();
        loginFragment =new LoginFragment();
        pictureFragment = new PictureFragment();
        serviceCenterFragment = new Service_Center_Fragment();
        setFrag(2); //첫 프로그먼트 화면을 무엇으로 지정
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_login:
                        setFrag(0);
                        break;
                    case R.id.action_family:
                        setFrag(1);
                        break;
                    case R.id.action_home:
                        setFrag(2);
                        break;
                    case R.id.action_Picture:
                        setFrag(3);
                        break;
                    case R.id.action_ServiceCenter:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });



    }

    private void setFrag(int n){
        fm =getSupportFragmentManager();
        ft =fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame,loginFragment,"login");
                bottomNavigationView.removeAllViews();
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame,myFamilyFragment,"family");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame,gridFragment,"home");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame,pictureFragment,"picture");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main_frame,serviceCenterFragment,"service");
                ft.addToBackStack(null);
                ft.commit();
                break;

        }
    }

    public void updateBottomMenu(BottomNavigationView bottomNavi){

        Fragment tag1 = getSupportFragmentManager().findFragmentByTag("login");
        Fragment tag2 = getSupportFragmentManager().findFragmentByTag("family");
        Fragment tag3 = getSupportFragmentManager().findFragmentByTag("home");
        Fragment tag4 = getSupportFragmentManager().findFragmentByTag("picture");
        Fragment tag5 = getSupportFragmentManager().findFragmentByTag("service");

        if(tag1 != null && tag1.isVisible()) bottomNavi.getMenu().findItem(R.id.action_login).setChecked(true);
        else if(tag2 != null && tag2.isVisible()) bottomNavi.getMenu().findItem(R.id.action_family).setChecked(true);
        else if(tag3 != null && tag3.isVisible()) bottomNavi.getMenu().findItem(R.id.action_home).setChecked(true);
        else if(tag4 != null && tag4.isVisible()) bottomNavi.getMenu().findItem(R.id.action_Picture).setChecked(true);
        else if(tag5 != null && tag5.isVisible()) bottomNavi.getMenu().findItem(R.id.action_ServiceCenter).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bottomNavigationView = findViewById(R.id.bottomNavi);
        updateBottomMenu(bottomNavigationView);

    }



}