package com.hj.homecleanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hj.homecleanproject.customInterface.onBackPressedListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout_SignIn;
    FragmentManager fm;
    FragmentTransaction ft;
    private long lastTimeBackPressed;
    SignUpFragment signUpFragment;
    ProgressDialog progressDialog;
    Button btn_login;
    EditText edt_Email,edt_Password;
    String email,password;
    FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(this,FragmentActivity.class));
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 0);



        signUpFragment =new SignUpFragment();

        btn_login =findViewById(R.id.login);
        layout_SignIn=findViewById(R.id.layout_SignIn);
        edt_Email =findViewById(R.id.edt_Email);
        edt_Password=findViewById(R.id.edt_password);

        btn_login.setOnClickListener(this);
        layout_SignIn.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        progressDialog =new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btn_login) {

            email = edt_Email.getText().toString();
            password = edt_Password.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                return;
            }
            else if (!(email.contains("@"))) {
                Toast.makeText(LoginActivity.this, "이메일형식으로 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            } else if (password.length() < 6) {
                Toast.makeText(LoginActivity.this, "비밀번호는 최소 6자 이상으로 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }else{
                userLogin();
            }

            } else if (v == layout_SignIn) {
                frgManagerBeginTransaction(R.id.login_layout, signUpFragment); //프래그먼트 매니저를 계속 생성해주어야함
            }

        }
        @Override
        public void onBackPressed () {
            //프래그먼트 onBackPressedListener사용
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            getSupportFragmentManager().getBackStackEntryCount();
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof onBackPressedListener) {
                    ((onBackPressedListener) fragment).onBackPressed();
                    return;
                }
            }

            //두 번 클릭시 어플 종료
            if (System.currentTimeMillis() - lastTimeBackPressed < 2000) {
                finish();
                return;
            }
            lastTimeBackPressed = System.currentTimeMillis();
            Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        }

        public void frgManagerBeginTransaction ( int layout, Fragment fragment){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(layout, fragment, null);
            ft.commit();
        }

        public  void userLogin(){
            email = edt_Email.getText().toString().trim();
            password = edt_Password.getText().toString().trim();

            progressDialog.setMessage("로그인중입니다.");
            progressDialog.show();
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"이메일과 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


}