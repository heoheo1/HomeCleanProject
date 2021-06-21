package com.hj.homecleanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FragmentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //하단바
    private FragmentManager fm;
    private FragmentTransaction ft;
    private GridFragment gridFragment;
    private MyFamilyFragment myFamilyFragment;
    private LoginFragment loginFragment;
    private PictureFragment pictureFragment;
    private Service_Center_Fragment serviceCenterFragment;

    private long lastPressed = 0;
    private ArrayList<Integer> positions; // position을 저장할 list
    private int position = 0, lastPosition = 2; //현재 선택값, 마지막에 선택한 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        bottomNavigationView = findViewById(R.id.bottomNavi);

        gridFragment = new GridFragment();
        myFamilyFragment = new MyFamilyFragment();
        loginFragment = new LoginFragment();
        pictureFragment = new PictureFragment();
        serviceCenterFragment = new Service_Center_Fragment();
        positions = new ArrayList<>();
        bottomNavigationView = findViewById(R.id.bottomNavi);
        setFrag(2); //첫 프로그먼트 화면을 무엇으로 지정
        bottomNavigationView.getMenu().getItem(lastPosition).setEnabled(false);
        positions.add(lastPosition);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_login:
                        setFrag(0);
                        position = 0;
                        break;
                    case R.id.action_family:
                        setFrag(1);
                        position = 1;
                        break;
                    case R.id.action_home:
                        setFrag(2);
                        position = 2;
                        break;
                    case R.id.action_Picture:
                        setFrag(3);
                        position = 3;
                        break;
                    case R.id.action_ServiceCenter:
                        setFrag(4);
                        position = 4;
                        break;
                }

                bottomNavigationView.getMenu().getItem(position).setEnabled(false); //현재 선택한 버튼은 잠그고

                if(lastPosition != position){ //만약 전에 선택한 버튼과 이번에 선택한 버튼이 다르다면
                    bottomNavigationView.getMenu().getItem(lastPosition).setEnabled(true); //전에 선택한 버튼은 다시 활성화시키고
                    positions.remove(positions.size()-1); // list의 전 position값은 제거
                }
                lastPosition = position; //전 position값에 현 position값을 저장하고
                positions.add(position); //list에 저장

                return true;
            }
        });
    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:

                ft.replace(R.id.main_frame, loginFragment, "login");
                bottomNavigationView.removeAllViews();
                //login후, 여러 fragment를 클릭후, 다시 로그인페이지로 왔을때
                //back버튼을 누르면 로그인페이지를 누르기전의 Fragment로 이동함
                //BackStack에 들어있는 모든 갯수를 Count해서 한개씩 지운다.
                for(int i = 0; i<fm.getBackStackEntryCount(); i++){
                    fm.popBackStack();
                }
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, myFamilyFragment, "family");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, gridFragment, "home");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, pictureFragment, "picture");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main_frame, serviceCenterFragment, "service");
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }

    public void updateBottomMenu(BottomNavigationView bottomNavi) {
        Fragment tag1 = getSupportFragmentManager().findFragmentByTag("login");
        Fragment tag2 = getSupportFragmentManager().findFragmentByTag("family");
        Fragment tag3 = getSupportFragmentManager().findFragmentByTag("home");
        Fragment tag4 = getSupportFragmentManager().findFragmentByTag("picture");
        Fragment tag5 = getSupportFragmentManager().findFragmentByTag("service");

        if (tag1 != null && tag1.isVisible())
            bottomNavi.getMenu().findItem(R.id.action_login).setChecked(true);
        else if (tag2 != null && tag2.isVisible())
            bottomNavi.getMenu().findItem(R.id.action_family).setChecked(true);
        else if (tag3 != null && tag3.isVisible())
            bottomNavi.getMenu().findItem(R.id.action_home).setChecked(true);
        else if (tag4 != null && tag4.isVisible())
            bottomNavi.getMenu().findItem(R.id.action_Picture).setChecked(true);
        else if (tag5 != null && tag5.isVisible())
            bottomNavi.getMenu().findItem(R.id.action_ServiceCenter).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            if (System.currentTimeMillis() < lastPressed + 2000) {
                ActivityCompat.finishAffinity(this);
            } else {
                lastPressed = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "한번 더 클릭하면 종료됩니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onBackPressed();
            //Back버튼 클릭시, 잠금 현상이 발생, 만약 back버튼 클릭시, lastPosition값을 바꾸고
            lastPosition = positions.get(positions.size() - 1);
            bottomNavigationView.getMenu().getItem(positions.get(positions.size()-1)).setEnabled(true); //선택이 가능하도록 바꿔준다.
            updateBottomMenu(bottomNavigationView);
        }
    }
}