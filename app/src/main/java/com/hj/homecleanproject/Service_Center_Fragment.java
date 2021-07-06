package com.hj.homecleanproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.Inflater;


public class Service_Center_Fragment extends PreferenceFragmentCompat {
    Preference soundPreference; //프리퍼런스를 전역변수 선언
    Preference keywordScreen;
    Preference editPreference;
    Preference switchPreference;
    Preference screen;


    @Override
    public void onNavigateToScreen(PreferenceScreen preferenceScreen) {
        super.onNavigateToScreen(preferenceScreen);
        Intent intent = new Intent(getActivity(), FragmentActivity.class);
        intent.putExtra("target", preferenceScreen.getKey()); //키값을 받아서 보내준다.
        startActivity(intent);


    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey);

        if (rootKey == null) {
            soundPreference = findPreference("sound_list");//findViewBy 참조할수있도록 지정하겠다.
            keywordScreen = findPreference("keyword_screen");
            editPreference = findPreference("nickname");
            screen=findPreference("key_test");
        }




    }


}




