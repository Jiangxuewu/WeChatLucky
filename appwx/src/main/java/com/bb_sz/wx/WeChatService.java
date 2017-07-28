package com.bb_sz.wx;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.DataOutputStream;
import java.util.List;


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
                if (currentActivityName.contains(ACT_LAUNCH_UI)) {
                    if (!TextUtils.isEmpty(CoreReceiver.contact) && openSearchApp(CoreReceiver.contact, ACT_LAUNCH_UI)) {
                        CoreReceiver.contact = null;
                    }

                    if (!TextUtils.isEmpty(CoreReceiver.group) && openSearchApp(CoreReceiver.group, ACT_LAUNCH_UI)) {
                        CoreReceiver.group = null;
                    }

//                    if (!TextUtils.isEmpty(CoreReceiver.nickname)) {
//                        res = openSearchApp(CoreReceiver.nickname, ACT_LAUNCH_UI);
//                        if (!res) {
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException ignored) {
//                            }
//                            click(265, 983);
//                        }
//                        CoreReceiver.nickname = null;
//                    }
                } else if (currentActivityName.contains(ACT_GROUP_UI)) {
                    if (!TextUtils.isEmpty(CoreReceiver.nickname) && openSearchApp(CoreReceiver.nickname, ACT_GROUP_UI)) {
                        CoreReceiver.nickname = null;
                    }
                } else if (currentActivityName.contains(ACT_CONTACT_INFO_UI)) {
//                    if (!TextUtils.isEmpty(CoreReceiver.sendMsg) && openSearchApp(CoreReceiver.sendMsg, ACT_CONTACT_INFO_UI)) {
//                        CoreReceiver.sendMsg = null;
//                    }
                } else if (currentActivityName.contains(ACT_CHATING_UI)) {
                    if (!TextUtils.isEmpty(CoreReceiver.sendTxt)) {
                        sendComment(CoreReceiver.sendTxt);
                        CoreReceiver.sendTxt = null;
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                    if (null == CoreReceiver.sendTxt && !TextUtils.isEmpty(CoreReceiver.sendBtn) && openSearchApp(CoreReceiver.sendBtn, ACT_CHATING_UI)) {
                        CoreReceiver.sendBtn = null;
                        closeWx();
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

    private void click(int x, int y) {
        doSuExec(new String[]{"input tap " + x + " " + y});
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

    private void inputText(String txt) {
        if (TextUtils.isEmpty(txt)) return;
        txt = txt.replaceAll(" ", "");
        doSuExec(new String[]{"input text " + txt});
    }

    private void sendComment(String txt) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        click(301, 1847);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
        inputText(txt);
    }


    private boolean openSearchApp(String searchTxt, String curAct) {
        if (debug)
            Log.i("SKY", "openSearchApp(),  searchTxt = " + searchTxt + ", curAct = " + curAct);
        AccessibilityNodeInfo eventSource = getRootInActiveWindow();
        if (null == eventSource) return false;
        List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByText(searchTxt);
        if (debug)
            Log.i("SKY", "openSearchApp(), nodes has : " + (null == nodes ? "null" : nodes.size()) + ", currentActivityName = " + currentActivityName);
        if (null != currentActivityName && null != nodes && !nodes.isEmpty() && currentActivityName.contains(curAct)) {
            for (AccessibilityNodeInfo nodeToClick : nodes) {
                if (debug)
                    Log.i("SKY", "openSearchApp(), nodeToClick is null ? : " + (null == nodeToClick));
                if (nodeToClick == null) return false;
                if (debug)
                    Log.i("SKY", "openSearchApp(), cls Name is " + nodeToClick.getClassName());
                boolean res = false;
                if ("android.widget.TextView".equals(nodeToClick.getClassName())) {
                    AccessibilityNodeInfo info = nodeToClick;
                    int i = 0;
                    while (!res && i < 5 && null != info) {
                        i++;
                        info = info.getParent();
                        if (null != info)
                            res = info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
//                    AccessibilityNodeInfo info = nodeToClick.getParent();
//                    if (debug)
//                        Log.i("SKY", "openSearchApp(),textView parent is " + info.getClassName());
//                    if ("android.widget.RelativeLayout".equals(info.getClassName())) {
//                        res = info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        if (debug)
//                            Log.i("SKY", "openSearchApp(),RelativeLayout content changed, res is : " + res);
//                    } else if ("android.widget.LinearLayout".equals(info.getClassName())) {
//                        AccessibilityNodeInfo itemLayout = info.getParent();
//                        if (debug)
//                            Log.i("SKY", "openSearchApp(),LinearLayout parent is " + itemLayout.getClassName());
//                        res = itemLayout.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        if (debug)
//                            Log.i("SKY", "openSearchApp(),LinearLayout content changed, res is : " + res);
//                    } else {
//                        res = info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        if (debug)
//                            Log.i("SKY", "openSearchApp(),other content changed, res is : " + res);
//                    }
                } else if (FrameLayout.class.getName().equals(nodeToClick.getClassName())) {
                    res = nodeToClick.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (debug)
                        Log.i("SKY", "openSearchApp(),FrameLayout content changed, res is : " + res);
                } else if (Button.class.getName().equals(nodeToClick.getClassName())) {
                    res = nodeToClick.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (debug)
                        Log.i("SKY", "openSearchApp(),FrameLayout content changed, res is : " + res);
                }
                if (res) {
                    return true;
                }
            }
        }
        return false;
    }

    private void closeWx() {
//        String[] cmd = {"am force-stop com.tencent.mm"};
//        WeChatService.doSuExec(cmd);
        doBackBtn();
        home();
    }

    private void home() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
        intent.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(intent);
    }

    private void doBackBtn() {
        doSuExec(new String[]{"input keyevent 4"});
    }
}
