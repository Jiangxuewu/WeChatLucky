package com.UU.uuaccelerator.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.UU.myaccessibility.UUAccessibilityService;
import com.UU.uuaccelerator.model.BuildProperties;



public class SystemUtils
{
	private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
  public static int Dp2Px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	} 
	 
	public static int Px2Dp(Context context, float px) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (px / scale + 0.5f); 
}
	  public static final boolean hasClassInApk(Context paramContext, String packageName, String cName){
		  
		  boolean bool=false;
		  try {
			Context context=paramContext.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY|Context.CONTEXT_INCLUDE_CODE);
			if (context!=null) {
				Class cls=context.getClassLoader().loadClass(cName);
				if (cls!=null) {
					bool=true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
		  
		return bool;
		  
	  }
	  

	    /**
	     * 华为rom
	     * @return
	     */
	    public static boolean isEMUI() {
	        try {
	            final BuildProperties prop = BuildProperties.newInstance();
	            return prop.getProperty(KEY_EMUI_VERSION_CODE, null) != null;
	        } catch (final IOException e) {
	            return false;
	        }
	    }

	    /**
	     * 小米rom
	     * @return
	     */
	    public static boolean isMIUI() {
	        try {
	            final BuildProperties prop = BuildProperties.newInstance();
	            /*String rom = "" + prop.getProperty(KEY_MIUI_VERSION_CODE, null) + prop.getProperty(KEY_MIUI_VERSION_NAME, null)+prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null);
	            Log.d("Android_Rom", rom);*/
	            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
	                  || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
	                  || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
	        } catch (final IOException e) {
	            return false;
	        }
	    }

	    /**
	     * 魅族rom
	     * @return
	     */
	    public static boolean isFlyme() {
	        try {
	            final Method method = Build.class.getMethod("hasSmartBar");
	            return method != null;
	        } catch (final Exception e) {
	            return false;
	        }
	    }

	  
	  public static boolean isAccessibilitySettingOn(Context c){
		  
		  int accessibilityEnable=0;
		  final String service = c.getPackageName()+"/"+UUAccessibilityService.class.getCanonicalName();
//			Log.e("service===", service);
		  try {
			accessibilityEnable=Settings.Secure.getInt(c.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
		
		  
		  } catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		  TextUtils.SimpleStringSplitter mSimpleStringSplitter=new SimpleStringSplitter(":");
		  if (accessibilityEnable==1) {
			String settingValue=Settings.Secure.getString(c.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
			if (settingValue!=null) {
//				Log.e("settingValue===", settingValue);
				String[] services= settingValue.split(":");
				for (int i = 0; i < services.length; i++) {
					if (services[i].equals(service)) {
						return true;
					}
				}
			}
		}
		  
		return false;
		  
	  }
}

