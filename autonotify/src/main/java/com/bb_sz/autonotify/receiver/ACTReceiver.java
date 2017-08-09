package com.bb_sz.autonotify.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bb_sz.autonotify.R;
import com.bb_sz.autonotify.service.MyCoreService;

public class ACTReceiver extends BroadcastReceiver {

    private static final String TAG = "ACTReceiver";

    public static String act;
    public static String pkg;

    public static long lastSendTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("SKY_ACTIVITY_ACTION".equals(action)) {
            act = intent.getStringExtra("act");
            if ("com.qihoo360.mobilesafe.antivirus.plugin.InstallMalwareAlertActivity".equals(act)) {
//                doBackBtn();
//                sendInfoToWx(context, "QH_" + pkg);
            } else if ("com.tencent.server.fore.DeskTopActivity".equals(act)) {
//                doBackBtn();
//                sendInfoToWx(context, "TX_" + pkg);
            } else if ("com.dianxinos.optimizer.module.antivirus.activity.AVMonitorInstallReportActivity".equals(act)) {
                doBackBtn();
                sendInfoToWx(context, "百度报毒_" + pkg);
            }
        }
    }

    private void doBackBtn() {
        MyCoreService.doSuExec(new String[]{"input keyevent 4"});
    }

    public static void sendInfoToWx(Context context, String i) {
        if (lastSendTime >= 0 && System.currentTimeMillis() - lastSendTime > 1000 * 2) {
            Log.i(TAG, "sendInfoToWx(), i = " + i);
            Intent intent = new Intent("com.bb_sz.intent.action.slwx");
            intent.putExtra("contact", context.getResources().getString(R.string.contact));
            intent.putExtra("nickname", context.getResources().getString(R.string.nickname));
            intent.putExtra("group", context.getResources().getString(R.string.group));
            intent.putExtra("sendBtn", context.getResources().getString(R.string.sendBtn));
            intent.putExtra("sendTxt", i);
            context.sendBroadcast(intent);
            lastSendTime = System.currentTimeMillis();
        }
    }
}
