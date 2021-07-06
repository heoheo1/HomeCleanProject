package com.hj.homecleanproject;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import androidx.preference.PreferenceScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class Service_Center_Fragment extends PreferenceFragmentCompat {
    Preference soundPreference; //프리퍼런스를 전역변수 선언
    Preference editPreference;
    Preference screen;
    FirebaseFirestore db;
    FirebaseStorage storage;
    DatabaseReference realtime;
    FirebaseAuth auth;
    FirebaseUser currentUser;


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
            editPreference = findPreference("nickname");
            screen =findPreference("screen");
        }

        screen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

               alertDialog.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {


                   }
               });

                return false;
            }
        });




    }


}




