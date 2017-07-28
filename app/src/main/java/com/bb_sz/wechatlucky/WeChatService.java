package com.bb_sz.wechatlucky;

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

import com.bb_sz.qh360.Helper;

import java.util.List;


/**
 * Created by Administrator on 2017/2/5.
 */
public class WeChatService extends AccessibilityService {
    private static final String TAG = "SL360";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Helper.getInstance().watchList(this, event);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt,...");
    }

}
