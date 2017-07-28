package com.bb_sz.wechatlucky;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CoreReceiver extends BroadcastReceiver {
    private static final String TAG = "CoreSL360";

    public static final String ACTION = "com.bb_sz.intent.action.sl360";
    public static String input = "消消乐";
    public static String txt = "萌萌消消乐";
    public static String act = "SearchActivity";
    public static String act1 = "GenericWordCategoryActivity";
    public static String key = "查看全部";
    public static int index = 1;
    public static int type = 1;

    public CoreReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "action = " + action);
        if (ACTION.equals(action)) {
            txt = intent.getStringExtra("sl360_txt");
            act = intent.getStringExtra("sl360_act");
            act1 = intent.getStringExtra("sl360_act1");
            input = intent.getStringExtra("sl360_input");
            key = intent.getStringExtra("sl360_key");
            index = intent.getIntExtra("sl360_index", 0);
            type = intent.getIntExtra("sl360_type", 0);
            Log.e(TAG, "txt = " + txt + ", act = " + act + ", input = " + input + ", type = " + type + ", act1 = " + act1 + ", key = " + key + ", index = " + index);
        }
    }
}
