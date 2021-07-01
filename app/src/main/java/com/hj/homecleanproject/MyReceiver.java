package com.hj.homecleanproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"알림의 Add action의 결과입니다.",Toast.LENGTH_SHORT).show();
    }
}
