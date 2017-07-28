package com.UU.uuaccelerator.Utils;



import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedUtils {

	private static final String  TRUST_APP="trust_app";
	private static final String PUBLIC_SHARED="public_shared";
	private static final String START_COUNT="start_count";

	private SharedUtils() {

	}
	
	public static void saveAppPower(Context context,boolean trust){
		if (context==null) {
			return;
		}
		SharedPreferences	sharedPreferences=context.getApplicationContext().getSharedPreferences(PUBLIC_SHARED, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putBoolean(TRUST_APP, trust);
		editor.commit();
		
		
	}
	public static boolean getAppPower(Context context){
		if (context==null) {
			return false;
		}
		SharedPreferences	sharedPreferences=context.getApplicationContext().getSharedPreferences(PUBLIC_SHARED, Context.MODE_PRIVATE);
		boolean b=sharedPreferences.getBoolean(TRUST_APP, false);
		
		return b;
	}
	public static int getStartAppCount(Context context){
		if (context==null) {
			return -1;
		}
		SharedPreferences	sharedPreferences=context.getApplicationContext().getSharedPreferences(START_COUNT, Context.MODE_PRIVATE);
		int b=sharedPreferences.getInt(START_COUNT,0);
		
		return b;
	}
	
	public static void saveStartAppCount(Context context){
		if (context==null) {
			return;
		}
		int c=getStartAppCount(context);
		SharedPreferences	sharedPreferences=context.getApplicationContext().getSharedPreferences(START_COUNT, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putInt(START_COUNT, c+1);
		editor.commit();
		
		
	}
}
