package com.hj.homecleanproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    long lastPressed =0;
    TextView txtSinUp;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fm =getSupportFragmentManager();
        ft =fm.beginTransaction();

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
                ft.add(R.id.login_layout,new MakeLoginFragment(),null);
                ft.addToBackStack(null);
                ft.commit();
            }
        });



    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() < lastPressed + 2000) {
            onBackPressed();

        } else {
            lastPressed = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "한번 더 클릭하면 종료됩니다.", Toast.LENGTH_LONG).show();
        }
    }
}