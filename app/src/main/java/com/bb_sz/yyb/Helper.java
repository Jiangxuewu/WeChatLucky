package com.bb_sz.yyb;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.bb_sz.FZHelper;
import com.bb_sz.wechatlucky.CoreReceiver;
import com.bb_sz.wechatlucky.WeChatService;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public class Helper {
    private static final String TAG = "YYBHelper";
    private static final boolean debug = true;
    private static long moreAppTime;
    private static long globalBackTime;
    private static long searchTime;

    public static void handlerEvent(WeChatService service, String name) {
        if (debug) Log.i(TAG, "handlerEvent(), name = " + name);
        boolean res = FZHelper.viewClickForId(service, "com.tencent.android.qqdownloader:id/a30");
        if (debug) Log.i(TAG, "handlerEvent(), a30 res = " + res);

        String txt = FZHelper.viewTextForId(service, "com.android.vpndialogs:id/warning");
        if (null != txt && txt.contains("VPN连接")) {
            res = FZHelper.viewClickForId(service, "android:id/button2");
        }

        if (name.contains(".activity.SplashImplActivity")) {
        } else if (name.contains(".assistantv2.activity.MainActivity")) {
            //点击搜索框
            res = FZHelper.viewClickForId(service, "com.tencent.android.qqdownloader:id/awt");
            if (debug) Log.i(TAG, "handlerEvent(), awt res = " + res);
            //6.0 位置授权
            res = FZHelper.viewClickForId(service, "com.tencent.android.qqdownloader:id/a31");
            if (debug) Log.i(TAG, "handlerEvent(), a31 res = " + res);
        } else if (name.contains(".nucleus.search.SearchActivity")) {

            //输入热词
            if (!TextUtils.isEmpty(CoreReceiver.input) && (searchTime == 0 || System.currentTimeMillis() - searchTime > 1000 * 20)) {
                res = FZHelper.edittextInput(service, "com.tencent.android.qqdownloader:id/yv", CoreReceiver.input);
                if (debug) Log.e(TAG, "handlerEvent(), 输入热词 res = " + res);
                if (res) {
                    CoreReceiver.input = null;
                    //搜索
                    res = FZHelper.viewClickForId(service, "com.tencent.android.qqdownloader:id/a5t");
                    if (res) {
                        searchTime = System.currentTimeMillis();
                    }
                    if (debug) Log.e(TAG, "handlerEvent(), 搜索 res = " + res);
                }
            }

            //找到app
            if (!TextUtils.isEmpty(CoreReceiver.txt)) {
                res = FZHelper.viewClickFroTxt(service, CoreReceiver.txt);
                if (res) {
                    CoreReceiver.txt = null;
                    if (debug) Log.e(TAG, "handlerEvent(), 找到app res = " + res);
                }
            }
            if (!TextUtils.isEmpty(CoreReceiver.moreApp)) {
                res = FZHelper.viewClickFroTxt(service, CoreReceiver.moreApp);
                if (res) {
                    CoreReceiver.moreApp = null;
                    if (debug) Log.e(TAG, "handlerEvent(), 点击更多应用 res = " + res);
                }
                moreAppTime = System.currentTimeMillis();
            }

            if (null == CoreReceiver.moreApp && null == CoreReceiver.input && !TextUtils.isEmpty(CoreReceiver.txt)) {
                List<AccessibilityNodeInfo> nodes = FZHelper.getNodesForId(service, "com.tencent.android.qqdownloader:id/a78");
                if (debug)
                    Log.i(TAG, "find ListView  nodes size is " + (null == nodes ? "null" : nodes.size()));
                txt = FZHelper.viewTextForId(service, "com.tencent.android.qqdownloader:id/ahk");
                if (debug) Log.i(TAG, " \u4e0a\u62c9\u52a0\u8f7d txt = " + txt);
                if (null != txt && txt.equals("\u4e0a\u62c9\u52a0\u8f7d")){
                    List<AccessibilityNodeInfo> pullNodes = FZHelper.getNodesForId(service, "com.tencent.android.qqdownloader:id/ahk");
                    if (null != pullNodes && pullNodes.size() > 0){
                        AccessibilityNodeInfo tmp = pullNodes.get(0).getParent();//.getParent();
                        if (debug) Log.i(TAG, " \u4e0a\u62c9\u52a0\u8f7d cls = " + tmp.getClassName().toString());
                        if (FrameLayout.class.getName().equals(tmp.getClassName().toString())){
                            res = tmp.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            if (debug) Log.i(TAG, " \u4e0a\u62c9\u52a0\u8f7d res = " + res);
                        }
                    }
                }

                if (null != nodes && nodes.size() > 0) {
                    for (AccessibilityNodeInfo item : nodes) {
                        printNodeChild(item);
                    }
                }
            }
        } else if (name.contains("pangu.activity.AppDetailActivityV5")) {
            txt = FZHelper.viewTextForId(service, "com.tencent.android.qqdownloader:id/r5");
            Log.i(TAG, "yyb txt = " + txt);
            if (null != txt && txt.contains("下载")) {
                //下载
                res = FZHelper.viewClickForId(service, "com.tencent.android.qqdownloader:id/qy");
            } else if (null != txt && txt.contains("安装")) {
                //安装
                res = FZHelper.viewClickForId(service, "com.tencent.android.qqdownloader:id/qy");
            } else if (null != txt && txt.contains("打开")) {

            } else if (null != txt && txt.contains("继续")) {
                //继续
                res = FZHelper.viewClickForId(service, "com.tencent.android.qqdownloader:id/qy");
            }
        } else if (name.contains(".pangu.welcome.WelcomeActivity")
                || name.contains(".pangu.necessary.NecessaryListActivity")
                || name.contains(".pangu.necessary.NewPhoneActivity")
                || name.contains(".permission.ui.GrantPermissionsActivity")
                ) {
            doBackBtn(service);
        } else {
            doBackBtn(service);
        }
    }

    private static void doBackBtn(AccessibilityService service) {
        if (System.currentTimeMillis() - globalBackTime > 1000 * 5) {
            boolean res = FZHelper.systemEvent(service, AccessibilityService.GLOBAL_ACTION_BACK);
            globalBackTime = System.currentTimeMillis();
            if (debug) Log.i(TAG, "handlerEvent(), global back res = " + res);
        }
    }

    private static boolean isOver = false;

    private static void printNodeChild(AccessibilityNodeInfo item) {
        if (null == item || isOver) return;
        int count = item.getChildCount();
        String item1Cls = item.getClassName().toString();
        Log.d(TAG, "printNodeChild(), item1Cls = " + item1Cls + ", count = " + count);
        if (ListView.class.getName().equals(item1Cls)) {
            boolean res = item.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            Log.i(TAG, "scroll forward 1 res = " + res);
            if (System.currentTimeMillis() - moreAppTime < 1000 * 5) {
                res = item.performAction(res ? AccessibilityNodeInfo.ACTION_SCROLL_FORWARD : AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                Log.i(TAG, "scroll forward 2 res = " + res);
            }
        }
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                printNodeChild(item.getChild(i));
            }
        }
    }
}
