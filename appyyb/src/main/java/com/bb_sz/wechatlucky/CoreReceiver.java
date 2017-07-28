package com.bb_sz.wechatlucky;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CoreReceiver extends BroadcastReceiver {
    private static final String TAG = "CoreSLYYB";

    public static final String ACTION = "com.bb_sz.intent.action.slyyb";
    public static String input = "L手电筒";
    public static String txt = "L手电筒";
    public static String act = "SearchActivity";
//    public static String downKey = "下载(";
//    public static String down = "AppDetailActivityV5";

    public CoreReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "action = " + action);
        if (ACTION.equals(action)) {
            txt = intent.getStringExtra("slyyb_txt");
            act = intent.getStringExtra("slyyb_act");
            input = intent.getStringExtra("slyyb_input");
            Log.e(TAG, "txt = " + txt + ", act = " + act + ", input = " + input);
        }
    }
}
