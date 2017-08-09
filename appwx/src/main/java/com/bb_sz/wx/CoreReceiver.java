package com.bb_sz.wx;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataOutputStream;

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
            openWx(context);
            Log.i(TAG, "contact = " + contact + ", nickname = " + nickname + ", group = " + group + ", sendBtn = " + sendBtn + ", sendTxt = " + sendTxt);
        }
    }

    private void openWx(Context context) {
        String[] cmd = {"am start -n com.tencent.mm/.ui.LauncherUI"};
        doSuExec(cmd);
//        Intent intent = new Intent();
//        ComponentName cmp = new ComponentName(" com.tencent.mm ","com.tencent.mm.ui.LauncherUI");
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setComponent(cmp);
//        context.startActivity(intent);
    }

    public static void doSuExec(String[] cmds) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            for (String cmd : cmds) {
                Log.d("cmd", "cmd = " + cmd);
                os.writeBytes(cmd + "\n");
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
