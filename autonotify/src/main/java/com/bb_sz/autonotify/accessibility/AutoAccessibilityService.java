package com.bb_sz.autonotify.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.bb_sz.FZHelper;
import com.bb_sz.autonotify.receiver.ACTReceiver;

/**
 * Created by Administrator on 2017/6/22.
 */

public class AutoAccessibilityService extends AccessibilityService {
    private static final String TAG = "AccessibilityService";
    private static final CharSequence FORCE_TOP_ACT = "com.tencent.server.fore.DeskTopActivity";
    private String currentActivityName;
    private static final String qihoo = "com.qihoo360.mobilesafe";
    private static final String qq = "com.tencent.qqpimsecure";
    private static final String baidu = "cn.opda.a.phonoalbumshoushou";
    private static final String supersu = "com.kingroot.kinguser";

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
                Log.d(TAG, "setCurrentActivityName,...currentActivityName is " + currentActivityName);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                String pkgName = event.getPackageName().toString();
                Log.e(TAG, "pkgName = " + pkgName);
                boolean res = false;
                if (supersu.equals(pkgName)) {

                } else if (baidu.equals(pkgName)) {
//                    String txt = FZHelper.viewTextForId(this, "cn.opda.a.phonoalbumshoushou:id/a");
                    res = FZHelper.viewClickFroTxt(this, "发现病毒应用");
                    Log.e(TAG, "res = " + res);
                    if (res){
                        res = FZHelper.viewClickFroTxt(this, "卸载");
                        if (res){
                            ACTReceiver.sendInfoToWx(getApplicationContext(), "百度报毒:" + ACTReceiver.pkg);
                        }
                    }
//                    if (null != txt && txt.contains("病毒")) {
                        // 信任
//                        res = FZHelper.viewClickForId(this, "cn.opda.a.phonoalbumshoushou:id/a");

//                    }

                } else if (qihoo.equals(pkgName)) {
                    String txt = FZHelper.viewTextForId(this, "com.qihoo360.mobilesafe.antivirus:id/dy");

                    if (null != txt && txt.contains("您安装的软件存在风险")) {
                        res = FZHelper.viewClickForId(this, "com.qihoo360.mobilesafe.antivirus:id/e0");

                        if (res)
                            ACTReceiver.sendInfoToWx(getApplicationContext(), "360报毒:" + ACTReceiver.pkg);
                    }
                } else if (qq.equals(pkgName)) {
                    Log.e(TAG, "pkgName qq = " + pkgName);
                    String txt = FZHelper.viewTextForId(this, "com.tencent.qqpimsecure:id/a5");

                    if (null != txt && txt.contains("安装包建议清理")) {
                        //立即清理
                        res = FZHelper.viewClickForId(this, "com.tencent.qqpimsecure:id/dr");
                        Log.e(TAG, "res = " + res);
                    }

                    txt = FZHelper.viewTextForId(this, "com.tencent.qqpimsecure.plugin.ppp:id/nr");
                    Log.e(TAG, "txt 2 = " + txt);
                    if (null != txt && (txt.contains("风险") || txt.contains("谨慎") || txt.contains("诱导"))) {
                        res = FZHelper.systemEvent(this, AccessibilityService.GLOBAL_ACTION_BACK);
                        ACTReceiver.sendInfoToWx(getApplicationContext(), "qq报毒:" + ACTReceiver.pkg);
                        Log.e(TAG, "res 2 = " + res);
                    }
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        Log.d(TAG, "onServiceConnected,...");
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        setServiceInfo(info);
        info.packageNames = new String[]{qihoo, qq, baidu, supersu};
        setServiceInfo(info);
        super.onServiceConnected();
    }

}
