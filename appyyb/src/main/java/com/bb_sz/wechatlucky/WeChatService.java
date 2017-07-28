package com.bb_sz.wechatlucky;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Administrator on 2017/2/5.
 */

public class WeChatService extends AccessibilityService {

    private static final String SKYYYB = "SKYYYB";
    private String currentActivityName;
    private static final boolean debug = true;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (debug)
            Log.v(SKYYYB, "onAccessibilityEvent,...EventType is " + (null == event ? "null" : event.getEventType()));
        //监视聊天列表
        watchList(event);
    }

    @Override
    public void onInterrupt() {
        Log.d(SKYYYB, "onInterrupt,...");
    }

    //监视聊天列表
    private boolean watchList(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED://32
                if (debug)
                    Log.d(SKYYYB, "setCurrentActivityName,...PackageName is " + event.getPackageName().toString());
                if (debug)
                    Log.d(SKYYYB, "setCurrentActivityName,...ClassName is " + event.getClassName().toString());
                ComponentName componentName = new ComponentName(event.getPackageName().toString(), event.getClassName().toString());
                try {
                    getPackageManager().getActivityInfo(componentName, 0);
                    currentActivityName = componentName.flattenToShortString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (debug)
                    Log.d(SKYYYB, "setCurrentActivityName,...currentActivityName is " + currentActivityName);//com.qihoo.appstore/.search.SearchActivity
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED://2048
                if (!TextUtils.isEmpty(CoreReceiver.input) && searchTxt(CoreReceiver.input)) {
                    CoreReceiver.input = null;
                }
                if (!TextUtils.isEmpty(CoreReceiver.txt) && !TextUtils.isEmpty(CoreReceiver.act)) {
                    openSearchApp(CoreReceiver.txt, CoreReceiver.act);
                }

                //start click down
//                if (!TextUtils.isEmpty(CoreReceiver.downKey) && !TextUtils.isEmpty(CoreReceiver.down) && openSearchApp(CoreReceiver.downKey, CoreReceiver.down)) {
//                    CoreReceiver.downKey = null;
//                }

                break;
            default:
                break;
        }
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
            return false;

        return false;
    }


    private boolean searchTxt(String txt) {
        AccessibilityNodeInfo eventSource = getRootInActiveWindow();
        if (null == eventSource) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && null != currentActivityName && currentActivityName.contains(CoreReceiver.act)) {
            List<AccessibilityNodeInfo> searchEditNodes = eventSource.findAccessibilityNodeInfosByViewId("com.tencent.android.qqdownloader:id/yv");//com.tencent.nucleus.search.SearchEditText
            if (debug)
                Log.d(SKYYYB, "searchTxt(), edittext nodes has : " + (null == searchEditNodes ? "null" : searchEditNodes.size()) + ", currentActivityName = " + currentActivityName);
            if (null == searchEditNodes || searchEditNodes.isEmpty()) return false;
            AccessibilityNodeInfo nodeInfo = searchEditNodes.get(0);
            if (null != nodeInfo) {
                String cls = (String) nodeInfo.getClassName();
                if (debug)
                    Log.d(SKYYYB, "searchTxt(), edittext nodes cls = " + cls + ", sdk = " + Build.VERSION.SDK_INT);
                if (EditText.class.getName().equals(cls)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, txt);
                        boolean res = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                        if (debug)
                            Log.d(SKYYYB, "searchText(), edittext input " + txt + " res is " + res);
                        if (!res) return false;
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            ClipboardManager clipboard = (ClipboardManager) getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("", txt);
                            clipboard.setPrimaryClip(clip);
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                        }
                    }

//                    if (1 == 1 ) return true;

                    List<AccessibilityNodeInfo> searchBtnNodes = eventSource.findAccessibilityNodeInfosByViewId("com.tencent.android.qqdownloader:id/a5t");
                    if (debug)
                        Log.e(SKYYYB, "searchTxt(), search btn nodes has : " + (null == searchBtnNodes ? "null" : searchBtnNodes.size()) + ", currentActivityName = " + currentActivityName);
                    if (null == searchBtnNodes || searchBtnNodes.size() <= 0) return false;
                    boolean res = false;
                    for (AccessibilityNodeInfo item : searchBtnNodes) {
                        if (null != item) {
                            cls = (String) item.getClassName();
                            if (debug) Log.i(SKYYYB, "searchTxt(),search btn cls = " + cls);
                            if (ImageView.class.getName().equals(cls)) {
                                res = item.performAction(AccessibilityNodeInfo.ACTION_CLICK);//开始搜索
                                if (debug)
                                    Log.i(SKYYYB, "searchTxt(),  search btn ImageView, res = " + res);
                                return true;
                            } else if (TextView.class.getName().equals(cls)) {
                                res = item.performAction(AccessibilityNodeInfo.ACTION_CLICK);//开始搜索
                                if (debug)
                                    Log.i(SKYYYB, "searchTxt(),  search btn TextView, res = " + res);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean openSearchApp(String searchTxt, String curAct) {
        if (debug)
            Log.i(SKYYYB, "openSearchApp(),  searchTxt = " + searchTxt + ", curAct = " + curAct);
        AccessibilityNodeInfo eventSource = getRootInActiveWindow();
        if (null == eventSource) return false;
        List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByText(searchTxt);
        if (debug)
            Log.i(SKYYYB, "openSearchApp(),, nodes has : " + (null == nodes ? "null" : nodes.size()) + ", currentActivityName = " + currentActivityName);
        if (null != currentActivityName && null != nodes && !nodes.isEmpty() && currentActivityName.contains(curAct)) {
            for (AccessibilityNodeInfo nodeToClick : nodes) {
                if (debug)
                    Log.i(SKYYYB, "openSearchApp(),, nodeToClick is null ? : " + (null == nodeToClick));
                if (nodeToClick == null) continue;
                String cls = nodeToClick.getClassName().toString();
                if (debug) Log.i(SKYYYB, "openSearchApp(),  cls Name is " + cls);
                if ("android.widget.TextView".equals(cls)) {
                    AccessibilityNodeInfo info = nodeToClick.getParent();
                    if (debug) Log.i(SKYYYB, "openSearchApp(), nodeToClick.getParent cls Name is " + info.getClassName());
                    if ("android.widget.RelativeLayout".equals(info.getClassName())) {
                        boolean res = info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        if (debug) Log.i(SKYYYB, "openSearchApp(),, res is : " + res);
                        if (res) return true;
                    }
                } else if (Button.class.getName().equals(cls)) {
                    boolean res = nodeToClick.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (debug) Log.i(SKYYYB, "openSearchApp(), btn res is : " + res);
                    if (res) return true;
                }
            }
        }
        return false;
    }
}
