package com.bb_sz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bb_sz.manager.CheckAct;

/**
 * Created by Administrator on 2017/8/10.
 */

public class ACTReceiver extends BroadcastReceiver {

    private static final String TAG = "ACTReceiver";
    public static String act;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action:" + action);
        if ("SKY_ACTIVITY_ACTION".equals(action)) {
            act = intent.getStringExtra("act");
            Log.d(TAG, "act:" + act);
            CheckAct.check(context, act);
        }
    }
}