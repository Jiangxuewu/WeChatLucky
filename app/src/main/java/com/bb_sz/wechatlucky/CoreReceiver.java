package com.bb_sz.wechatlucky;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Random;

public class CoreReceiver extends BroadcastReceiver {
    private static final String TAG = "CoreHelper";

    public static final String ACTION = "com.bb_sz.intent.action.helper";
    public static String input = "手电";
    public static String txt = "L手电筒";
    public static String moreApp = "更多应用";
    public static String act = "";
    public static String act1 = "";
    public static String key = "";
    public static String app_name = "L手电筒";
    public static int index = 0;
    public static int type = 1;
    public static boolean isOpen = false;

    public CoreReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG, "action = " + action);
        if (ACTION.equals(action)) {
//            txt = intent.getStringExtra("sl360_txt");
//            act = intent.getStringExtra("sl360_act");
//            act1 = intent.getStringExtra("sl360_act1");
//            input = intent.getStringExtra("sl360_input");
//            key = intent.getStringExtra("sl360_key");
//            index = intent.getIntExtra("sl360_index", 0);
//            type = intent.getIntExtra("sl360_type", 0);
//
            String json = intent.getStringExtra("msg_json");
            if (!TextUtils.isEmpty(json)) {
                App app = new Gson().fromJson(json, App.class);
                input = app.input;
                txt = app.name;
                app_name = app.app_name;
                moreApp = "更多应用";
                isOpen = new Random().nextInt(100) >= app.open;
                act = "SearchActivity";
                act1 = "GenericWordCategoryActivity";
                key = app.qh360typekey;
                index = app.qh360typeindex;
                type = app.qh360type;
                Log.e(TAG, "txt = " + txt + ", act = " + act + ", input = " + input + ", type = " + type + ", act1 = " + act1 + ", key = " + key + ", index = " + index + ", isOpen = " + isOpen);
            }
        }
    }
}
