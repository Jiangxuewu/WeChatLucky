package com.bb_sz.installer;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bb_sz.FZHelper;
import com.bb_sz.wechatlucky.CoreReceiver;

/**
 * Created by Administrator on 2017/8/8.
 */

public class Helper {
    private static final String TAG = "InstallHelper";

    public static void handlerEvent(AccessibilityService service, String name) {
        //6.0 允许所有的授权
        boolean res = FZHelper.viewClickForId(service, "com.android.packageinstaller:id/permission_allow_button");

        if (name.contains(".PackageInstallerActivity")) {
            //下一步 or 安装
            res = FZHelper.viewClickForId(service, "com.android.packageinstaller:id/ok_button");
        } else if (name.contains(".InstallAppProgress")) {
            if (CoreReceiver.isOpen) {
                // 打开
                res = FZHelper.viewClickForId(service, "com.android.packageinstaller:id/launch_button");
                if (res) {
                    installOver(service);
                }
            } else {
                //完成
                res = FZHelper.viewClickForId(service, "com.android.packageinstaller:id/done_button");
                if (res) {
                    installOver(service);
                }
            }
        }
    }

    private static void installOver(AccessibilityService service) {
//        String txt = FZHelper.viewTextForId(service, "android:id/message");
        String txt = FZHelper.viewTextForId(service, "com.android.packageinstaller:id/center_text");
        Log.e(TAG, "installOver(), center_text = " + txt);
        if (null != txt && txt.contains("应用安装完成")) {
            //app name
            txt = FZHelper.viewTextForId(service, "com.android.packageinstaller:id/app_name");
            Log.e(TAG, "installOver(), app_name = " + txt);
            if (null != CoreReceiver.app_name && CoreReceiver.app_name.equals(txt)) {
                //完成任务
                notifAutoSuccess(service.getApplicationContext());
                //返回home界面
                FZHelper.systemEvent(service, AccessibilityService.GLOBAL_ACTION_HOME);
            }
//            //完成任务
//            notifAutoSuccess(service.getApplicationContext());
//            //返回home界面
//            FZHelper.systemEvent(service, AccessibilityService.GLOBAL_ACTION_HOME);
        }
    }

    private static void notifAutoSuccess(Context context) {
//        Log.e("SKY", "notifAutoSuccess(), action: SKY_ACTIVITY_ACTION");
        Intent intent = new Intent("SKY_ACTIVITY_ACTION");
        intent.putExtra("success", "SKY_AUTO_RUN_SUCCESS");
        context.sendBroadcast(intent);
    }
}
