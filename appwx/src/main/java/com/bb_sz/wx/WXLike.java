package com.bb_sz.wx;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bb_sz.FZHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class WXLike {
    private static final String ACT_LAUNCH_UI = "ui.LauncherUI";
    private static final String ACT_CONTACT_INFO_UI = ".plugin.profile.ui.ContactInfoUI";
    private static final String ACT_CHATING_UI = ".ui.chatting.En_5b8fbb1e";
    private static final String ACT_GROUP_UI = ".ChatroomContactUI";
    private static final String TAG = "WXLike";

    private static final int TYPE_LIKE = 0;
    private static final int TYPE_UNLIKE = 1;

    private static final int type = TYPE_LIKE;
    private static final int STATE_NONE = 0;
    private static final int STATE_RUN = 1;
    private static int like_state = STATE_NONE;
    private static int comment_state = STATE_NONE;
    private static boolean isOver = true;

    public static void comment(AccessibilityService service, String name) {
        if (comment_state != STATE_NONE || isOver) return;
        Log.e(TAG, "comment() name = " + name);
        if (null == name || null == service) return;
        comment_state = STATE_RUN;
        boolean res;
        if (name.contains(ACT_LAUNCH_UI)) {
//            res = FZHelper.viewClickFroTxt(service, "\u53d1\u73b0");
//            Log.e(TAG, "\u53d1\u73b0 res = " + res);
//            res = FZHelper.viewClickFroTxt(service, "\u670b\u53cb\u5708");
//            Log.e(TAG, "\u670b\u53cb\u5708 res = " + res);
//            comment_state = STATE_NONE;
        } else if (name.contains(".plugin.sns.ui.En_424b8e16")) {
            String title = FZHelper.viewTextForId(service, "com.tencent.mm:id/fl");
            Log.e(TAG, "title = " + title);
            if ("\u66f4\u6362\u76f8\u518c\u5c01\u9762".equals(title)) {
                FZHelper.systemEvent(service, AccessibilityService.GLOBAL_ACTION_BACK);
            }

            String nickName = null;
            //comment id
            List<AccessibilityNodeInfo> nickNameNodes = FZHelper.getNodesForId(service, "com.tencent.mm:id/aig");
            List<AccessibilityNodeInfo> commentNodes = FZHelper.getNodesForId(service, "com.tencent.mm:id/crp");
            int nickNameSize = (null == nickNameNodes ? 0 : nickNameNodes.size());
            int commentNodesSize = (null == commentNodes ? 0 : commentNodes.size());
            Log.e(TAG, "nick name nodes size is " + nickNameSize + ", comment nodes size is " + commentNodesSize);
            if (nickNameSize > 0) {
                for (int i = 1; i < nickNameSize; i++) {
                    AccessibilityNodeInfo item = nickNameNodes.get(i);
                    nickName = null == item ? "null" : item.getText().toString();
                    Log.i(TAG, "nickName = " + nickName + ", i = " + i);
                    if (nickName.contains("SkyCar") && i < commentNodesSize) {
                        AccessibilityNodeInfo commentItem = commentNodes.get(i);
                        res = commentItem.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Log.i(TAG, "res ... = " + res + ", i = " + i);
                        //comment btn
                        res = FZHelper.viewClickForId(service, "com.tencent.mm:id/cqu");
                        Log.i(TAG, "res comment = " + res + ", i = " + i);
                        //input
                        res = FZHelper.edittextInput(service, "com.tencent.mm:id/cry", "http://www.bb-sz.com " + new Date(System.currentTimeMillis()).toLocaleString());
                        Log.i(TAG, "res input = " + res + ", i = " + i);
                        //send btn
                        res = FZHelper.viewClickForId(service, "com.tencent.mm:id/cs0");
                        Log.i(TAG, "res send = " + res + ", i = " + i);
                        res = FZHelper.viewActionForId(service, "com.tencent.mm:id/cve", AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                        Log.e(TAG, "cve res = " + res);
//                        isOver = true;
                        break;
                    }
                }
            }
        }
        comment_state = STATE_NONE;
    }

    public static void like(AccessibilityService service, String name) {
        if (like_state == STATE_RUN) return;
        Log.e(TAG, "like() name = " + name);
        if (null == name || null == service) return;
        boolean res;
        like_state = STATE_RUN;
        if (name.contains(ACT_LAUNCH_UI)) {
            res = FZHelper.viewClickFroTxt(service, "\u53d1\u73b0");
            Log.e(TAG, "\u53d1\u73b0 res = " + res);
            res = FZHelper.viewClickFroTxt(service, "\u670b\u53cb\u5708");
            Log.e(TAG, "\u670b\u53cb\u5708 res = " + res);
        } else if (name.contains(".plugin.sns.ui.En_424b8e16")) {
            List<AccessibilityNodeInfo> nodes = FZHelper.getNodesForId(service, "com.tencent.mm:id/crp");
            String title = FZHelper.viewTextForId(service, "com.tencent.mm:id/fl");
            Log.e(TAG, "title = " + title);
            if ("\u66f4\u6362\u76f8\u518c\u5c01\u9762".equals(title)) {
                FZHelper.systemEvent(service, AccessibilityService.GLOBAL_ACTION_BACK);
            }
            if (null != nodes) {
                for (AccessibilityNodeInfo item : nodes) {
                    res = item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.e(TAG, "crp res = " + res);
                    String txt = FZHelper.viewTextForId(service, "com.tencent.mm:id/cqt");
                    Log.e(TAG, "cqt txt = " + txt);
                    if (type == TYPE_LIKE && "\u8d5e".equals(txt)) {
                        res = FZHelper.viewClickForId(service, "com.tencent.mm:id/cqr");
                        Log.e(TAG, "cqr res = " + res);
                    }
                    if (type == TYPE_UNLIKE && "\u53d6\u6d88".equals(txt)) {
                        res = FZHelper.viewClickForId(service, "com.tencent.mm:id/cqr");
                        Log.e(TAG, "cqr res = " + res);
                    }
                }
                res = FZHelper.viewActionForId(service, "com.tencent.mm:id/cve", AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                Log.e(TAG, "cve res = " + res);
            }
        }
        like_state = STATE_NONE;
    }
}
