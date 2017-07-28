package com.UU.myaccessibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.UU.uuaccelerator.Utils.ComponentHandler;
import com.UU.uuaccelerator.Utils.FloatWindowManager;
import com.UU.uuaccelerator.Utils.SystemUtils;
import com.UU.uuaccelerator.common.Constant;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class FloatWindowService extends Service {
	
			

	/**
	 * 用于在线程中创建或移除悬浮窗。
	 */
	private Handler handler = new Handler();

	/**
	 * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
	 */
	public  Timer timer;
	private	FloatWindowManager manager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 开启定时器，每隔0.5秒刷新一次
		
		return super.onStartCommand(intent, flags, startId);
	}

@Override
public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	manager=FloatWindowManager.getInstance(getApplicationContext());
	if (timer == null) {
		timer = new Timer();
		timer.scheduleAtFixedRate(new RefreshTask(), 1000, 500);
	}
}
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Service被终止的同时也停止定时器继续运行
		if (timer!=null) {
			
			timer.cancel();
			timer = null;
		}
	}

	class RefreshTask extends TimerTask {

		@Override
		public void run() {
		if(	SystemUtils.isAccessibilitySettingOn(getApplicationContext())) {
			if (timer!=null) {
				
				timer.cancel();
				timer = null;
			}
			return;
		}
		// 当前界面是，且没有悬浮窗显示，则创建悬浮窗。
			if (isOwnAccesibility()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						manager.showFloaltWindowView();
					}
				});
			}
			// 当前界面不是，且有悬浮窗显示，则移除悬浮窗。
			else  {
				handler.post(new Runnable() {
					@Override
					public void run() {
						manager.cancleFloaltWindowView();
					}
				});
			}
		}

	}

	/**
	 * 判断当前界面是否是本应用调转的辅助功能
	 */
	private boolean isOwnAccesibility() {
		Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
		intent.addFlags(Constant.FLAG_ACCESSIBILITY);
		return ComponentHandler.getInstance().validateIntent(getPackageManager(), intent)&&isTopActivity("AccessibilitySettingsActivity");
	}
	/**
	 * 判断intent是否是前台
	 */
	private boolean isTopActivity(String  intent  ) {
		ActivityManager localActivityManager = (ActivityManager)getSystemService("activity");
	    List<RunningTaskInfo> localList = localActivityManager.getRunningTasks(1);
	ComponentName componentName=   localList.get(0).topActivity;
	String name=componentName.getClassName();
	if (name.contains(intent)) {
		return true;
	}

		return false;
	}

	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes() {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}
}
