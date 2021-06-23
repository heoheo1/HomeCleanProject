package com.hj.homecleanproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


import com.hj.homecleanproject.customInterface.onBackPressedListener;

import java.security.acl.AclNotFoundException;


public class MakeLoginFragment extends Fragment implements onBackPressedListener {

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static MakeLoginFragment newInstance() {
        return new MakeLoginFragment();
    }

    Animation animation,animation2;
    ViewGroup viewGroup;
    LinearLayout login_anim3;
    LinearLayout login_anim4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_make_login,container,false);
       login_anim3= viewGroup.findViewById(R.id.login_anim3);
       login_anim4= viewGroup.findViewById(R.id.login_anim4);
       animation2 =AnimationUtils.loadAnimation(getContext(), R.anim.login_animation2);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.login_animation);
        login_anim3.startAnimation(animation);
        login_anim4.startAnimation(animation2);

        return viewGroup;
    }

    @Override
    public void onBackPressed() {
        goToMain();
    }
    //프래그먼트 종료
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); //requireActivity=getActivity(있다는걸 보장,없으면 Excepion. app crash 발생) activity가 null한 상황을 아예 없애려고 쓰인다.
        fragmentManager.beginTransaction().remove(MakeLoginFragment.this).commit();
        fragmentManager.popBackStack();
    }
}