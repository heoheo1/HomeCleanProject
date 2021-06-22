package com.hj.homecleanproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    long lastPressed =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button =findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,FragmentActivity.class);
                startActivity(intent);
                finish();
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