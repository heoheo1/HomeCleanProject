package com.hh.firebase_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail,edtPassword;
    TextView txtSignIn,txtMessage;
    Button btnSignInUp;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtEmail =findViewById(R.id.edtEmail);
        edtPassword =findViewById(R.id.edtPassword);
        txtMessage=findViewById(R.id.textView2);
        txtSignIn=findViewById(R.id.viewSignIn);
        btnSignInUp =findViewById(R.id.btnSignUP);

        auth = FirebaseAuth.getInstance();
//        if(auth.getCurrentUser()!=null){
//            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
//            finish();
//        }

        progressDialog =new ProgressDialog(this);

        btnSignInUp.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==btnSignInUp){
            registerUser();
        }else  if(v==txtSignIn){
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    public  void  registerUser() {
        String email =edtEmail.getText().toString();
        String password =edtPassword.getText().toString();
        if(email.isEmpty()||password.isEmpty()){ //둘중의 하나라도 비어있으면
            Toast.makeText(this, "이메일 주소나 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("등록중입니다. 기다려주세요");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SignInActivity.this, "회원가입 등록 실패", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}