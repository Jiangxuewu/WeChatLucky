package com.bb_sz.autonotify;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements AccessibilityManager.AccessibilityStateChangeListener {


    private static final String TAG = "skyAutoNotify";
    private AccessibilityManager accessibilityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_hello).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                system(v);
                return false;
            }
        });

        checkAccessible();
    }

    private void checkAccessible() {
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
    }

    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            Log.i(TAG, "isServiceEnabled(), id is " + info.getId());
            if (info.getId().equals(getPackageName() + "/.AccessibilityService")) {
                return true;
            }
        }
        return false;
    }

    public void system(View view) {
        try {
            String tmp = " /data/local/tmp/autonotify.apk ";
            String local = " /system/app/autonotify.apk ";
            ApplicationInfo info = getApplicationInfo();
            Process proc = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
            os.writeBytes("mount -o remount,rw /system\n");
            os.writeBytes("cp " + info.sourceDir + tmp + " \n");
            os.writeBytes("chmod 777 " + tmp + "\n");
            os.writeBytes("cp " + tmp + local + " \n");
            os.writeBytes("rm -rf " + tmp + "\n");
            os.writeBytes("chmod 777 " + local + "\n");
            os.writeBytes("reboot\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {

    }
}
