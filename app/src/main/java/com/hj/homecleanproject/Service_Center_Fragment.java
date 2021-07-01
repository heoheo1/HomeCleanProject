package com.hj.homecleanproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.zip.Inflater;


public class Service_Center_Fragment extends PreferenceFragmentCompat {
    Preference soundPreference; //프리퍼런스를 전역변수 선언
    Preference keywordScreen;
    Preference editPreference;
    Preference switchPreference;


    @Override
    public void onNavigateToScreen(PreferenceScreen preferenceScreen) {
        super.onNavigateToScreen(preferenceScreen);
        Intent intent = new Intent(getActivity(),FragmentActivity.class);
        intent.putExtra("target",preferenceScreen.getKey()); //키값을 받아서 보내준다.
        startActivity(intent);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey);

        if (rootKey == null) {
            soundPreference = findPreference("sound_list");//findViewBy 참조할수있도록 지정하겠다.
            keywordScreen = findPreference("keyword_screen");
            editPreference = findPreference("nickname");


        }

    }


    }