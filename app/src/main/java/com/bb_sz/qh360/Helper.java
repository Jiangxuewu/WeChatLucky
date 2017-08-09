package com.bb_sz.qh360;


import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bb_sz.FZHelper;
import com.bb_sz.wechatlucky.CoreReceiver;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/14.
 */

public class Helper {
    private static final String TAG = "360Helper";
    private static final boolean debug = true;
    private static long globalBackTime;

    public static void handlerEvent(AccessibilityService service, String name) {
        if (debug) Log.i(TAG, "handlerEvent()");
        boolean res = false;
        String txt = FZHelper.viewTextForId(service, "com.qihoo.appstore:id/retry_text_view");
        if (null != txt && txt.contains("再试一试")) {
            res = FZHelper.viewClickForId(service, "com.qihoo.appstore:id/retry_text_view");
        }

        txt = FZHelper.viewTextForId(service, "com.android.vpndialogs:id/warning");
        if (null != txt && txt.contains("VPN连接")) {
            res = FZHelper.viewClickForId(service, "android:id/button2");
        }

        if (name.contains("com.qihoo.personPortrait.PersonPortraitGuideActivity")) {
            //选择性别
            if (new Random().nextInt(2) == 1) {
                FZHelper.viewClickForId(service, "com.qihoo.appstore:id/btn_sel_male_icon");
            } else {
                FZHelper.viewClickForId(service, "com.qihoo.appstore:id/btn_sel_female_icon");
            }
            //选择年龄
            List<AccessibilityNodeInfo> nodes = FZHelper.getNodesForId(service, "com.qihoo.appstore:id/item" + (new Random().nextInt(4) + 1));
            if (null != nodes && nodes.size() > 0) {
                AccessibilityNodeInfo item = nodes.get(new Random().nextInt(nodes.size()));
                assert null != item;
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

            //进入首页
            FZHelper.viewClickForId(service, "com.qihoo.appstore:id/set_btn");
        } else if (name.contains(".home.MainActivity")) {
            // TODO: 2017/8/1
//                res = FZHelper.viewChildClickForId(service, "com.qihoo.appstore:id/btn_search");
//            res = FZHelper.viewChildClickForId(service, "com.qihoo.appstore:id/search_title");
//            Log.i(TAG, "res = " + res);
//                FZHelper.doSuExec(new String[]{"input tap 338 100"});
        } else if (name.contains(".search.GenericWordCategoryActivity")) {
            if (/*CoreReceiver.type == 1 && */!TextUtils.isEmpty(CoreReceiver.txt) && TextUtils.isEmpty(CoreReceiver.input)) {//放在输入热词前面判断，，，
                res = FZHelper.viewClickFroTxt(service, CoreReceiver.txt);
                if (debug) Log.i(TAG, "find txt res = " + res);
                if (res) {
                    CoreReceiver.txt = null;
                } else {
                    res = FZHelper.viewActionForId(service, "com.qihoo.appstore:id/search_result_app_list", AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                }
            }
        } else if (name.contains(".search.SearchActivity")) {
            if (CoreReceiver.type == 0 && !TextUtils.isEmpty(CoreReceiver.txt) && TextUtils.isEmpty(CoreReceiver.input)) {//放在输入热词前面判断，，，
                res = FZHelper.viewClickFroTxt(service, CoreReceiver.txt);
                if (res) {
                    CoreReceiver.txt = null;
                } else {
                    res = FZHelper.viewActionForId(service, "com.qihoo.appstore:id/search_result_app_list", AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                }
            }

            if (CoreReceiver.type == 1 && !TextUtils.isEmpty(CoreReceiver.key)) {
                List<AccessibilityNodeInfo> nodes = FZHelper.getNodesForTxt(service, CoreReceiver.key);
                if (null != nodes && nodes.size() > 0 && nodes.size() >= CoreReceiver.index) {
                    AccessibilityNodeInfo nodeToClick = nodes.get(CoreReceiver.index);
                    Log.i(TAG, "handlerEvent(), nodeToClick is null ? : " + (null == nodeToClick));
                    if (nodeToClick == null) return;
                    Log.i(TAG, "handlerEvent(), cls Name is " + nodeToClick.getClassName());
                    if ("android.widget.TextView".equals(nodeToClick.getClassName())) {
                        res = nodeToClick.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Log.i(TAG, "handlerEvent(), res is : " + res);
                        if (res) {
                            CoreReceiver.key = null;
                        }
                    }
                }
            }

            if (!TextUtils.isEmpty(CoreReceiver.input)) {
                res = FZHelper.edittextInput(service, "com.qihoo.appstore:id/search_edit", CoreReceiver.input);
                if (res) {
                    CoreReceiver.input = null;
                    res = FZHelper.viewClickForId(service, "com.qihoo.appstore:id/common_list_download_proxy");
                }
            }
        } else if (name.contains(".appinfopage.AppInfoActivity")) {
            res = FZHelper.viewClickForId(service, "com.qihoo.appstore:id/download_btn");//真的下载id

            //使用流量下载
            res = FZHelper.viewClickForId(service, "com.qihoo.appstore:id/common_dialog_negative_btn");

        } else if (name.contains(".install.NormalInstallTransferActivity")) {

        } else if (name.contains(".webview.WebViewActivity")) {
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
}
