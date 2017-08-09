package com.bb_sz.wx;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.bb_sz.FZHelper;


/**
 * Created by Administrator on 2017/2/5.
 */

public class WeChatService extends AccessibilityService {

    private static final String ACT_LAUNCH_UI = "ui.LauncherUI";
    private static final String ACT_CONTACT_INFO_UI = ".plugin.profile.ui.ContactInfoUI";
    private static final String ACT_CHATING_UI = ".ui.chatting.En_5b8fbb1e";
    private static final String ACT_GROUP_UI = ".ChatroomContactUI";

    private String currentActivityName;
    private static final boolean debug = true;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (debug)
            Log.v("SKY", "onAccessibilityEvent,...EventType is " + (null == event ? "null" : event.getEventType()));
        //监视聊天列表
        watchList(event);
    }

    @Override
    public void onInterrupt() {
        Log.d("SKY", "onInterrupt,...");
    }

    //监视聊天列表
    private boolean watchList(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED://32
                if (debug)
                    Log.d("SKY", "setCurrentActivityName,...PackageName is " + event.getPackageName().toString());
                if (debug)
                    Log.d("SKY", "setCurrentActivityName,...ClassName is " + event.getClassName().toString());
                ComponentName componentName = new ComponentName(event.getPackageName().toString(), event.getClassName().toString());
                try {
                    getPackageManager().getActivityInfo(componentName, 0);
                    currentActivityName = componentName.flattenToShortString();
                } catch (PackageManager.NameNotFoundException ignored) {
                }
                if (debug)
                    Log.d("SKY", "setCurrentActivityName,...currentActivityName is " + currentActivityName);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED://2048
                if (debug)
                    Log.e("SKY", "setCurrentActivityName,...currentActivityName is " + currentActivityName);
                if (null == currentActivityName) break;
                boolean res;
                if (TextUtils.isEmpty(CoreReceiver.contact) && TextUtils.isEmpty(CoreReceiver.group)
                        && TextUtils.isEmpty(CoreReceiver.nickname) && TextUtils.isEmpty(CoreReceiver.sendTxt)) {
                    WXLike.comment(this, currentActivityName);
                }

                if (currentActivityName.contains(ACT_LAUNCH_UI)) {
                    if (!TextUtils.isEmpty(CoreReceiver.contact)) {
                        res = FZHelper.viewClickForId(this, "com.tencent.mm:id/bw3");
                        if (res) CoreReceiver.contact = null;
                    }

                    if (!TextUtils.isEmpty(CoreReceiver.group)) {
                        res = FZHelper.viewClickFroTxt(this, CoreReceiver.group);
                        if (res) CoreReceiver.group = null;
                    }

                } else if (currentActivityName.contains(ACT_GROUP_UI)) {
                    if (!TextUtils.isEmpty(CoreReceiver.nickname)) {
                        res = FZHelper.viewClickFroTxt(this, CoreReceiver.nickname);
                        if (res) CoreReceiver.nickname = null;
                    }
                } else if (currentActivityName.contains(ACT_CONTACT_INFO_UI)) {
                } else if (currentActivityName.contains(ACT_CHATING_UI)) {
                    if (!TextUtils.isEmpty(CoreReceiver.sendTxt)) {
                        res = FZHelper.edittextInput(this, "com.tencent.mm:id/a5e", CoreReceiver.sendTxt);
                        if (res) CoreReceiver.sendTxt = null;
                    }
                    if (null == CoreReceiver.sendTxt) {
                        //send msg
                        res = FZHelper.viewClickForId(this, "com.tencent.mm:id/a5k");
                        if (res) closeWx();
                    }
                }
                break;
            default:
                break;
        }
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
            return false;

        return false;
    }


    private void closeWx() {
        doBackBtn();
        home();
    }

    private void home() {
        FZHelper.systemEvent(this, AccessibilityService.GLOBAL_ACTION_HOME);
    }

    private void doBackBtn() {
        FZHelper.systemEvent(this, AccessibilityService.GLOBAL_ACTION_BACK);
        FZHelper.systemEvent(this, AccessibilityService.GLOBAL_ACTION_BACK);
    }
}
