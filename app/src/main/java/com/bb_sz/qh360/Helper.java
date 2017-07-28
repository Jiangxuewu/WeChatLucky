package com.bb_sz.qh360;


import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;

import com.bb_sz.wechatlucky.CoreReceiver;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class Helper {
    private static final String TAG = "H360";
    private static Helper mInstance;
    private String currentActivityName;

    public static Helper getInstance() {
        synchronized (TAG) {
            if (null == mInstance) {
                mInstance = new Helper();
            }
            return mInstance;
        }
    }

    private Helper() {
    }


    public boolean watchList(AccessibilityService service, AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED://32
                Log.d(TAG, "setCurrentActivityName,...PackageName is " + event.getPackageName().toString());
                Log.d(TAG, "setCurrentActivityName,...ClassName is " + event.getClassName().toString());
                ComponentName componentName = new ComponentName(event.getPackageName().toString(), event.getClassName().toString());
                try {
                    service.getPackageManager().getActivityInfo(componentName, 0);
                    currentActivityName = componentName.flattenToShortString();
                } catch (Exception ignored) {
                }
                Log.d(TAG, "setCurrentActivityName,...currentActivityName is " + currentActivityName);//com.qihoo.appstore/.search.SearchActivity
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED://2048
                //输入搜索关键字
                if (!TextUtils.isEmpty(CoreReceiver.input) && searchTxt(service, CoreReceiver.input)) {
                    CoreReceiver.input = null;
                }
                //打开搜索列表
                if (CoreReceiver.type == 1 && !TextUtils.isEmpty(CoreReceiver.key) && openSearchType(service, CoreReceiver.key, CoreReceiver.act, CoreReceiver.index)) {
                    CoreReceiver.key = null;
                }
                //打开需要搜索下载的app
                if (CoreReceiver.type == 1) {
                    if (!TextUtils.isEmpty(CoreReceiver.txt) && !TextUtils.isEmpty(CoreReceiver.act1) && openSearchApp(service, CoreReceiver.txt, CoreReceiver.act1)) {
                        CoreReceiver.txt = null;
                    }
                } else {
                    if (!TextUtils.isEmpty(CoreReceiver.txt) && !TextUtils.isEmpty(CoreReceiver.act) && openSearchApp(service, CoreReceiver.txt, CoreReceiver.act)) {
                        CoreReceiver.txt = null;
                    }
                }

                tryAgain(service, "再试一试");
                break;
            default:
                break;
        }
        return false;
    }

    private boolean searchTxt(AccessibilityService service, String txt) {
        AccessibilityNodeInfo eventSource = service.getRootInActiveWindow();
        if (null == eventSource) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && null != currentActivityName && currentActivityName.contains(CoreReceiver.act)) {
            List<AccessibilityNodeInfo> searchEditNodes = eventSource.findAccessibilityNodeInfosByViewId("com.qihoo.appstore:id/search_edit");
            if (null == searchEditNodes || searchEditNodes.isEmpty()) return false;
            AccessibilityNodeInfo nodeInfo = searchEditNodes.get(0);
            if (null != nodeInfo) {
                String cls = (String) nodeInfo.getClassName();
                if (EditText.class.getName().equals(cls)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, txt);
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            ClipboardManager clipboard = (ClipboardManager) service.getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("", txt);
                            clipboard.setPrimaryClip(clip);
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                        }
                    }
                    // 点击搜索按钮
//                    List<AccessibilityNodeInfo> searchBtnNodes = eventSource.findAccessibilityNodeInfosByText("搜索");
                    List<AccessibilityNodeInfo> searchBtnNodes = eventSource.findAccessibilityNodeInfosByViewId("com.qihoo.appstore:id/common_list_download_proxy");
                    Log.e(TAG, "content changed, nodes has : " + (null == searchBtnNodes ? "null" : searchBtnNodes.size()) + ", currentActivityName = " + currentActivityName);
                    if (null == searchBtnNodes || searchBtnNodes.size() <= 0) return false;
                    for (AccessibilityNodeInfo item : searchBtnNodes) {
                        if (null != item) {
                            cls = (String) item.getClassName();
                            if (Button.class.getName().equals(cls)) {
                                boolean res = item.performAction(AccessibilityNodeInfo.ACTION_CLICK);//开始搜索....
                                Log.i(TAG, "cls = " + cls + ", res = " + res);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean openSearchApp(AccessibilityService service, String searchTxt, String curAct) {
        AccessibilityNodeInfo eventSource = service.getRootInActiveWindow();
        if (null == eventSource) return false;
        List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByText(searchTxt);
        Log.i(TAG, "openSearchApp(), nodes has : " + (null == nodes ? "null" : nodes.size()) + ", currentActivityName = " + currentActivityName);
        if (null != currentActivityName && null != nodes && !nodes.isEmpty() && currentActivityName.contains(curAct)) {
            AccessibilityNodeInfo nodeToClick = nodes.get(0);
            Log.i(TAG, "openSearchApp(), nodeToClick is null ? : " + (null == nodeToClick));
            if (nodeToClick == null) return false;
//            int count = nodeToClick.getChildCount();
//            Log.i(TAG, "content changed, count = : " + count + ", cls Name is " + nodeToClick.getClassName());
            if ("android.widget.TextView".equals(nodeToClick.getClassName())) {
                AccessibilityNodeInfo info = nodeToClick.getParent();
                if ("android.widget.RelativeLayout".equals(info.getClassName())) {
                    boolean res = info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.i(TAG, "openSearchApp(), res is : " + res);
                    return res;
                }
            }
        }
        return false;
    }

    private boolean openSearchType(AccessibilityService service, String searchTxt, String curAct, int index) {
        Log.i(TAG, "openSearchType(), searchTxt = " + searchTxt + ", curAct = " + curAct + ", index = " + index);
        AccessibilityNodeInfo eventSource = service.getRootInActiveWindow();
        if (null == eventSource) return false;
        List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByText(searchTxt);
        Log.i(TAG, "openSearchType(), nodes has : " + (null == nodes ? "null" : nodes.size()) + ", currentActivityName = " + currentActivityName);
        if (null != currentActivityName && null != nodes && !nodes.isEmpty() && currentActivityName.contains(curAct)) {

            for (int i = 0; i < nodes.size(); i++) {
                if (i == index) {
                    AccessibilityNodeInfo nodeToClick = nodes.get(i);
                    Log.i(TAG, "openSearchType(), nodeToClick is null ? : " + (null == nodeToClick));
                    if (nodeToClick == null) return false;
                    Log.i(TAG, "openSearchType(), cls Name is " + nodeToClick.getClassName());
                    if ("android.widget.TextView".equals(nodeToClick.getClassName())) {
                        boolean res = nodeToClick.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Log.i(TAG, "openSearchType(), res is : " + res);
                        return res;
                    }
                }
            }
        }
        return false;
    }

    private void tryAgain(AccessibilityService service, String test) {
        AccessibilityNodeInfo eventSource = service.getRootInActiveWindow();
        if (null == eventSource) return;
        List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByText(test);
        Log.i(TAG, "tryAgain(), nodes has : " + (null == nodes ? "null" : nodes.size()));
        for (AccessibilityNodeInfo nodeToClick : nodes) {
            Log.i(TAG, "tryAgain(), nodeToClick is null ? : " + (null == nodeToClick));
            if (nodeToClick == null) return;
            Log.i(TAG, "tryAgain(), nodeToClick.getClassName() : " + nodeToClick.getClassName());
            if ("android.widget.TextView".equals(nodeToClick.getClassName())) {
                AccessibilityNodeInfo info = nodeToClick.getParent();
                Log.i(TAG, "tryAgain(), info.getClassName() : " + nodeToClick.getClassName());
                boolean res = info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.i(TAG, "tryAgain(), res is : " + res);
            }
        }
        return;
    }
}
