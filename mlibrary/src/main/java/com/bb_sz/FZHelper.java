package com.bb_sz;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class FZHelper {

    public static final String TAG = FZHelper.class.getSimpleName();

    public static List<AccessibilityNodeInfo> getNodesForTxt(AccessibilityService service, String txt) {
        if (null == service || TextUtils.isEmpty(txt)) return null;
        AccessibilityNodeInfo eventSource = service.getRootInActiveWindow();
        if (null == eventSource) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return eventSource.findAccessibilityNodeInfosByText(txt);
        }
        return null;
    }

    public static List<AccessibilityNodeInfo> getNodesForId(AccessibilityService service, String id) {
        if (null == service || TextUtils.isEmpty(id)) return null;
        AccessibilityNodeInfo eventSource = service.getRootInActiveWindow();
        if (null == eventSource) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return eventSource.findAccessibilityNodeInfosByViewId(id);
        }
        return null;
    }

    public static boolean edittextInput(AccessibilityService service, String id, String txt) {
        Log.d(TAG, "edittextInput(), id = " + id + ", txt = " + txt);
        List<AccessibilityNodeInfo> nodes = getNodesForId(service, id);
        if (null == nodes || nodes.isEmpty()) return false;
        int size = nodes.size();
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo nodeInfo = nodes.get(i);
            if (null != nodeInfo) {
                String cls = (String) nodeInfo.getClassName();
                if (EditText.class.getName().equals(cls)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, txt);
                        return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            ClipboardManager clipboard = (ClipboardManager) service.getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("", txt);
                            clipboard.setPrimaryClip(clip);
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean buttonClick(AccessibilityService service, String id) {
        Log.d(TAG, "buttonClick(), id = " + id);
        List<AccessibilityNodeInfo> nodes = getNodesForId(service, id);
        if (null == nodes || nodes.isEmpty()) return false;
        int size = nodes.size();
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo nodeInfo = nodes.get(i);
            if (null != nodeInfo) {
                String cls = (String) nodeInfo.getClassName();
                if (Button.class.getName().equals(cls)) {
                    boolean res = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.i(TAG, "buttonClick(), cls = " + cls + ", res = " + res);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean viewClickForId(AccessibilityService service, String id) {
        Log.d(TAG, "viewClickForId(), id = " + id);
        List<AccessibilityNodeInfo> nodes = getNodesForId(service, id);
        if (null == nodes || nodes.isEmpty()) return false;
        int size = nodes.size();
        boolean res = false;
        Log.d(TAG, "viewClickForId(), size = " + size);
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo nodeInfo = nodes.get(i);
            String cls = nodeInfo.getClassName().toString();
            Log.d(TAG, "viewClickForId(), cls = " + cls);
            int count = 0;
            while (!res && count++ < 5 && null != nodeInfo) {
                res = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                nodeInfo = nodeInfo.getParent();
            }
            if (res) return true;
        }
        return false;
    }
    public static boolean viewChildClickForId(AccessibilityService service, String id) {
        Log.d(TAG, "viewClickForId(), id = " + id);
        List<AccessibilityNodeInfo> nodes = getNodesForId(service, id);
        if (null == nodes || nodes.isEmpty()) return false;
        int size = nodes.size();
        boolean res = false;
        Log.d(TAG, "viewClickForId(), size = " + size);
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo nodeInfo = nodes.get(i);
            String cls = nodeInfo.getClassName().toString();
            int childCount = nodeInfo.getChildCount();
            Log.d(TAG, "viewClickForId(), cls = " + cls + ", childCount = " + childCount);
            int count = 0;
            while (!res && count++ < 5 && null != nodeInfo) {
                res = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);

                nodeInfo = nodeInfo.getParent();
            }
            if (res) return true;
        }
        return false;
    }

    public static boolean viewClickFroTxt(AccessibilityService service, String txt) {
        Log.d(TAG, "viewClickFroTxt(), txt = " + txt);
        List<AccessibilityNodeInfo> nodes = getNodesForTxt(service, txt);
        if (null == nodes || nodes.isEmpty()) return false;
        int size = nodes.size();
        boolean res = false;
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo nodeInfo = nodes.get(i);
            int count = 0;
            while (!res && count++ < 5 && null != nodeInfo) {
                res = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                nodeInfo = nodeInfo.getParent();
            }
            if (res) return true;
        }
        return false;
    }


    public static boolean systemEvent(AccessibilityService service, int action) {
        if (null == service) return false;
        return service.performGlobalAction(action);
    }

    public static boolean viewActionForId(AccessibilityService service, String id, int action) {
        List<AccessibilityNodeInfo> nodes = getNodesForId(service, id);
        if (null == nodes || nodes.isEmpty()) return false;
        Log.e(TAG, "viewActionForId(), size = " + nodes.size());
        for (AccessibilityNodeInfo item : nodes) {
            if (item.performAction(action)) {
                return true;
            }
        }
        return false;
    }

    public static String viewTextForId(AccessibilityService service, String id){
        List<AccessibilityNodeInfo> nodes = getNodesForId(service, id);
        if (null == nodes || nodes.isEmpty()) return null;
        Log.e(TAG, "viewTextForId(), size = " + nodes.size());
        for (AccessibilityNodeInfo item : nodes) {
            if (!TextUtils.isEmpty(item.getText().toString())) {
                return item.getText().toString();
            }
        }
        return null;
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
    public static void doExec(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
