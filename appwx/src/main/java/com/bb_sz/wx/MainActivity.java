package com.bb_sz.wx;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import java.util.List;


public class MainActivity extends Activity implements AccessibilityManager.AccessibilityStateChangeListener {
    private static final String TAG = "Main";
    //AccessibilityService 管理
    private AccessibilityManager accessibilityManager;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);
        updateServiceStatus();
    }


    private void updateServiceStatus() {
        if (!isServiceEnabled()) {
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        }
        finish();
    }

    /**
     * 获取 Service 是否启用状态
     */
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.WeChatService")) {
                return true;
            }
        }
        return false;
    }

    public void onClickTest(View view) {
//        Intent i = new Intent(this, WebViewUI.class);
//        i.putExtra("url", "http://www.bb-sz.com");
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
//        finish();
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        Log.d(TAG, "enabled = " + enabled);
        if (enabled) finish();
    }
}
