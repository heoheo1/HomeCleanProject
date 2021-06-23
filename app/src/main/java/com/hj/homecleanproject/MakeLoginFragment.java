package com.hj.homecleanproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hj.homecleanproject.R;
import com.hj.homecleanproject.customInterface.onBackPressedListener;

import java.util.Objects;


public class MakeLoginFragment extends Fragment implements onBackPressedListener {

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static MakeLoginFragment newInstance() {
        return new MakeLoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_make_login, container, false);
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