package com.UU.uuaccelerator.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;

import com.UU.myaccessibility.FloatWindowService;
import com.UU.myaccessibility.UUAccessibilityService;
import com.UU.uuaccelerator.common.Constant;

import java.util.List;

public class ComponentHandler {

    private static ComponentHandler componentUnits;

    private static Context mContext;

    private ComponentHandler() {

    }

    public static ComponentHandler getInstance(Context context) {

        if (componentUnits == null) {
            componentUnits = new ComponentHandler();
        }

        if (context != null) {
            mContext = context;
        }
        return componentUnits;

    }

    public static ComponentHandler getInstance() {

        return getInstance(null);

    }

    public void startActivity(Class activity) {

        Intent intent = new Intent();
        intent.setClass(mContext.getApplicationContext(), activity);
        mContext.getApplicationContext().startActivity(intent);

    }

    public void startAccessibility() {

        if (mContext == null) {
            new Throwable("Context is null");
            return;
        }

        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        UUAccessibilityService.isFirst = true;
        if (!SystemUtils.isMIUI()) {

            ((Activity) mContext).startActivityForResult(intent, 1000);

        } else {
            Bundle localBundle = new Bundle();
            if ((SystemUtils
                    .hasClassInApk(
                            mContext,
                            "com.android.settings",
                            "com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment"))) {
                intent.putExtra(
                        ":android:show_fragment",
                        "com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment");
                intent.setComponent(new ComponentName("com.android.settings",
                        "com.android.settings.Settings"));
                intent.putExtra(":android:show_fragment_short_title", 0);
                intent.putExtra(":android:show_fragment_args", 0);
                intent.putExtra(":android:show_fragment_title", 0);
                intent.putExtra(":android:no_headers", true);
                intent.putExtra("setting:ui_options", 1);

//				localBundle.putString("summary", mContext.getResources()
//						.getString(R.string.setting_service_description));
                localBundle.putString("title", "游戏加速器");
                localBundle.putString("preference_key",
                        mContext.getPackageName() + "/"
                                + UUAccessibilityService.class.getName());
                localBundle.putParcelable("component_name",
                        new ComponentName(mContext.getPackageName(),
                                UUAccessibilityService.class.getName()));
                localBundle.putBoolean("checked", false);

            }

            intent.putExtra(":android:show_fragment_args", localBundle);

            intent.addFlags(Constant.FLAG_ACCESSIBILITY);
            if (canHandleIntent(mContext, intent)) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).startActivityForResult(intent, 1000);
                } else {
                    mContext.startActivity(intent);
                }
            } else {
                intent.setAction("android.settings.SETTINGS");
                if (mContext instanceof Activity) {
                    ((Activity) mContext).startActivityForResult(intent, 1000);
                } else {
                    mContext.startActivity(intent);
                }
            }
        }
        Intent intentServer = new Intent();
        intentServer.setClass(mContext, FloatWindowService.class);
        mContext.startService(intentServer);
    }

    // TODO Auto-generated method stub
    public boolean goHuaWeiSetting(Context context) {
        try {
            Intent intent = new Intent("com.UU.myaccessibility.SpeedUpActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager",
                    "com.huawei.permissionmanager.ui.MainActivity");// 华为权限管理，跳转到本app的权限管理页面,这个需要华为接口权限，未解决
            intent.setComponent(comp);
            context.startActivity(intent);
            return true;
        } catch (Exception e1) {
            /**
             * 手机管家版本较低 HUAWEI SC-UL10
             */
            try {
                Intent intent = new Intent(
                        "com.UU.myaccessibility.SpeedUpActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName comp = new ComponentName("com.android.settings",
                        "com.android.settings.permission.TabItem");// 权限管理页面
                // android4.4
                intent.setComponent(comp);
                context.startActivity(intent);
                return true;
            } catch (Exception e2) {
                // 抛出异常时提示信息
                return false;
            }
        }

    }

    public boolean validateIntent(PackageManager manager, Intent intent) {

        return manager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES).size() > 0;

    }

    public boolean canHandleIntent(Context paramContext, Intent paramIntent) {
        return !queryIntentActivities(paramContext, paramIntent).isEmpty();
    }

    public List<ResolveInfo> queryIntentActivities(Context paramContext,
                                                   Intent paramIntent) {
        return paramContext.getPackageManager().queryIntentActivities(
                paramIntent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    public static int getIdByName(Context context, String cName, String name) {
        String pName = context.getPackageName();
        Class cls = null;
        int id = 0;
        try {
            cls = Class.forName(pName + ".R");
            Class[] classes = cls.getClasses();
            Class c = null;
            for (int i = 0; i < classes.length; i++) {
                if (classes[i].getName().split("\\$")[1].equals(cName)) {
                    c = classes[i];
                    break;
                }
            }

            if (c != null) {
                id = c.getField(name).getInt(cName);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return id;

    }
}
