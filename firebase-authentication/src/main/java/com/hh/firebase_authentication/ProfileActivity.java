package com.hh.firebase_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogout;
    TextView txtDelete, userEmail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogout =findViewById(R.id.btnLogOut);
        txtDelete=findViewById(R.id.txtDelete);
        userEmail=findViewById(R.id.txtUserEmail);

        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()==null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        FirebaseUser user =auth.getCurrentUser();
        userEmail.setText(user.getEmail()+"으로 로그인하였습니다.");
        btnLogout.setOnClickListener(this);
        txtDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btnLogout){
            auth.signOut(); //로그아웃
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }else if(v==txtDelete){
            AlertDialog.Builder alert_Confirm =new AlertDialog.Builder(ProfileActivity.this);
            alert_Confirm.setMessage("계정을 삭제 하겠습니까?").setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user =auth.getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProfileActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();
                                        }
                                    });
                        }

                    });
            alert_Confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(ProfileActivity.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            alert_Confirm.show();

        }
    }
}