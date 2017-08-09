package com.bb_sz.wechatlucky;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.bb_sz.FZHelper;


/**
 * Created by Administrator on 2017/2/5.
 */
public class WeChatService extends AccessibilityService {
    private static final String TAG = "SKYHelper";

    private static final String qihoo = "com.qihoo.appstore";
    private static final String yyb = "com.tencent.android.qqdownloader";
    private static final String oppo = "com.oppo.market";
    private static final String packageinstaller = "com.android.packageinstaller";
    private static final String supersu = "eu.chainfire.supersu";
    private String currentActivityName;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED://32
                ComponentName componentName = new ComponentName(event.getPackageName().toString(), event.getClassName().toString());
                try {
                    getPackageManager().getActivityInfo(componentName, 0);
                    currentActivityName = componentName.flattenToShortString();
                } catch (Exception ignored) {
                }
                Log.i(TAG, "setCurrentActivityName,...currentActivityName is " + currentActivityName);
                break;
        }
        String pkgName = event.getPackageName().toString();
        Log.e(TAG, "pkg name is " + pkgName + ", cur act name is " + currentActivityName);
        boolean res = false;
        if (supersu.equals(pkgName)) {
            res = FZHelper.viewClickForId(this, "eu.chainfire.supersu:id/prompt_deny");
            Log.i(TAG, "spuer su deny res = " + res);
        }

        if (TextUtils.isEmpty(currentActivityName)) {
            if (yyb.equals(pkgName)) {
                //6.0以上的手机，去授权
                res = FZHelper.viewClickForId(this, "com.tencent.android.qqdownloader:id/a2y");
            }
            return;
        }

        if (qihoo.equals(pkgName)) {
            com.bb_sz.qh360.Helper.handlerEvent(this, currentActivityName);
        } else if (yyb.equals(pkgName)) {
            com.bb_sz.yyb.Helper.handlerEvent(this, currentActivityName);
        } else if (packageinstaller.equals(pkgName)) {
            com.bb_sz.installer.Helper.handlerEvent(this, currentActivityName);
        } else if (oppo.equals(pkgName)) {
            // TODO: 2017/8/8
        }

    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt,...");
    }

    @Override
    protected void onServiceConnected() {
        Log.d(TAG, "onServiceConnected,...");
        AccessibilityServiceInfo info = getServiceInfo();
        info.packageNames = new String[]{yyb, qihoo, oppo, packageinstaller, supersu};
        setServiceInfo(info);
        super.onServiceConnected();
    }
}
