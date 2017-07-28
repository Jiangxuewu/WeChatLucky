package com.bb_sz.wx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CoreReceiver extends BroadcastReceiver {
    private static final String TAG = "CoreSLWX";

    public static final String ACTION = "com.bb_sz.intent.action.slwx";
    public static String contact = "";
    public static String group = "";
    public static String nickname = "";
//    public static String sendMsg = "";
    public static String sendBtn = "";
    public static String sendTxt = "";

    public CoreReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "action = " + action);
        if (ACTION.equals(action)) {
            contact = intent.getStringExtra("contact");
            nickname = intent.getStringExtra("nickname");
            group = intent.getStringExtra("group");
            sendBtn = intent.getStringExtra("sendBtn");
            sendTxt = intent.getStringExtra("sendTxt");
            openWx();
            Log.i(TAG, "contact = " + contact + ", nickname = " + nickname + ", group = " + group + ", sendBtn = " + sendBtn + ", sendTxt = " + sendTxt);
        }
    }

    private void openWx() {
        String[] cmd = {"am start -n com.tencent.mm/.ui.LauncherUI"};
        WeChatService.doSuExec(cmd);
    }
}
