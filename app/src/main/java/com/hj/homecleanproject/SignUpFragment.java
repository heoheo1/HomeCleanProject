package com.hj.homecleanproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hj.homecleanproject.customInterface.onBackPressedListener;


public class SignUpFragment extends Fragment implements onBackPressedListener {

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    Animation animation,animation2;
    ViewGroup viewGroup;

    LinearLayout login_anim4;
    EditText edt_SignEmail,edt_SignPassword,edt_SignPasswordCheck;
    Button btn_Sign;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    String email,passwordCheck,password;
    Context context;
    SignUpDialogFragment signUpDialogFragment;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_sign_up,container,false);
        login_anim4= viewGroup.findViewById(R.id.login_anim4);
        animation2 =AnimationUtils.loadAnimation(getContext(), R.anim.login_animation2);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.login_animation);

        login_anim4.startAnimation(animation2);
        context =container.getContext();
        signUpDialogFragment =new SignUpDialogFragment();

        setRetainInstance(true);

        edt_SignEmail=viewGroup.findViewById(R.id.edt_SignEmail);
        edt_SignPassword=viewGroup.findViewById(R.id.edt_SignPassword);
        edt_SignPasswordCheck=viewGroup.findViewById(R.id.edt_SignPasswordCheck);
        btn_Sign=viewGroup.findViewById(R.id.btn_profile);
        progressDialog =new ProgressDialog(getContext());

        btn_Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email =edt_SignEmail.getText().toString().trim();
                password =edt_SignPassword.getText().toString().trim();
                passwordCheck =edt_SignPasswordCheck.getText().toString().trim();

                if(email.isEmpty()||password.isEmpty()||passwordCheck.isEmpty()) {
                    Log.d("dd", "dd");
                    if (email.isEmpty()) {
                        Toast.makeText(getActivity(), "이메일 주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                        Log.d("dd", "dd");
                        return;
                    } else if (password.isEmpty()) {
                        Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;

                    } else if (passwordCheck.isEmpty()) {
                        Toast.makeText(context, "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;

                    }
                } else if(!(password.equals(passwordCheck))) {
                    Toast.makeText(getContext(), "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }else if(password.length()<6){
                    Toast.makeText(context, "비밀번호는 최소 6자 이상으로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!(email.contains("@"))){
                    Toast.makeText(context,"이메일형식으로 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser();


                }
            }
        });


        return viewGroup;
    }


    private void registerUser(){
        email =edt_SignEmail.getText().toString();
        password =edt_SignPassword.getText().toString();
        passwordCheck =edt_SignPasswordCheck.getText().toString();
        Bundle emailResult = new Bundle();
        emailResult.putString("email",email);
        signUpDialogFragment.setArguments(emailResult);

        auth = FirebaseAuth.getInstance();
        progressDialog.setMessage("등록중입니다.");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                progressDialog.dismiss();
                signUpDialogFragment.show(getActivity().getSupportFragmentManager(),"dialog");
                signUpDialogFragment.setCancelable(false);
                goToMain();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(context, "이미 존재하는 아이디", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        goToMain();
    }
    //프래그먼트 종료
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); //requireActivity=getActivity(있다는걸 보장,없으면 Excepion. app crash 발생) activity가 null한 상황을 아예 없애려고 쓰인다.
        fragmentManager.beginTransaction().remove(SignUpFragment.this).commit();//프래그먼트를 지운다.
        fragmentManager.popBackStack();
    }
}