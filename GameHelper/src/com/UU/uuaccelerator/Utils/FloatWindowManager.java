package com.UU.uuaccelerator.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.UU.uuaccelerator.view.FloatWindowBigView;
import com.UU.uuaccelerator.view.FloatWindowSmallView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FloatWindowManager {
	public static final int FLAG_SMALL_VIEW=0x101;
	public static final int  FLAG_BIG_VIEW=0x102;
	
	private static FloatWindowManager floatWindowManager;

	private static int viewFlag=FLAG_BIG_VIEW;
	
	/**
	 * 小悬浮窗View的实例
	 */
	private   View smallWindow;

	static Context mContext;
	
	/**
	 * 大悬浮窗View的实例
	 */
	private   FloatWindowBigView bigWindow;

	/**
	 * 小悬浮窗View的参数
	 */
	private   LayoutParams smallWindowParams;

	/**
	 * 大悬浮窗View的参数
	 */
	private   LayoutParams bigWindowParams;

	/**
	 * 用于控制在屏幕上添加或移除悬浮窗
	 */
	private static   WindowManager mWindowManager;


	/**
	 * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	@SuppressLint("NewApi")
	private  void addSmallWindow() {
			if (smallWindow == null) {
				smallWindow = new FloatWindowSmallView(mContext);
				if (smallWindowParams == null) {
					smallWindowParams = new LayoutParams();
					smallWindowParams.type = LayoutParams.TYPE_PHONE;
					smallWindowParams.format = PixelFormat.RGBA_8888;
					smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
							| LayoutParams.FLAG_NOT_FOCUSABLE;
					smallWindowParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
					smallWindowParams.width = LayoutParams.WRAP_CONTENT;
					smallWindowParams.height = LayoutParams.WRAP_CONTENT;
//					smallWindowParams.x = screenWidth;
//					smallWindowParams.y = screenHeight / 2;
				}
				smallWindow.setLayoutParams(smallWindowParams);
			
				mWindowManager.addView(smallWindow, smallWindowParams);
			}
			
			
		
	}

	/**
	 * 将小悬浮窗从屏幕上移除。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	private  void removeSmallWindow() {
		if (smallWindow != null) {
			mWindowManager.removeView(smallWindow);
		smallWindow=null;
		}
	}

	/**
	 * 创建一个大悬浮窗。位置为屏幕正中间。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	@SuppressLint("NewApi")
	private  void addBigWindow() {
		if (bigWindow == null) {
			bigWindow = new FloatWindowBigView(mContext);
			WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			int screenWidth = windowManager.getDefaultDisplay().getWidth();
			int screenHeight = windowManager.getDefaultDisplay().getHeight();
			if (bigWindowParams == null) {
				bigWindowParams = new LayoutParams();
				bigWindowParams.type = LayoutParams.TYPE_PHONE;
				bigWindowParams.format = PixelFormat.RGBA_8888;
				bigWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				bigWindowParams.gravity = Gravity.TOP;
				bigWindowParams.width = LayoutParams.MATCH_PARENT;
				bigWindowParams.height = LayoutParams.MATCH_PARENT;
			
				

			}
			bigWindow.setLayoutParams(bigWindowParams);
			mWindowManager.addView(bigWindow, bigWindowParams);
	}
	}
public static  FloatWindowManager getInstance(Context context) {
	mContext=context;
	viewFlag=FLAG_BIG_VIEW;
	if (floatWindowManager==null) {
		floatWindowManager=new FloatWindowManager();
	}
	if (mWindowManager == null) {
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
	}
	
	return floatWindowManager;
	
}
public static FloatWindowManager getInstance() {
	if (floatWindowManager==null) {
		floatWindowManager=new FloatWindowManager();
	}
	return floatWindowManager;
}
private  FloatWindowManager() {
	
}
	/**
	 * 将大悬浮窗从屏幕上移除。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
private  void removeBigWindow() {
		if (bigWindow != null) {
			mWindowManager.removeView(bigWindow);
			bigWindow=null;
		}
	}


public  void showFloaltWindowView() {
	
	switch (viewFlag) {
	case FLAG_SMALL_VIEW:
		removeBigWindow();
		addSmallWindow();
		
		
		break;
	case FLAG_BIG_VIEW:
		removeSmallWindow();
		addBigWindow();
		
		break;

	default:
		break;
	}
	
}
public  void showFloaltWindowView(int flag) {
	viewFlag=flag;
	switch (viewFlag) {
	case FLAG_SMALL_VIEW:
		removeBigWindow();
		addSmallWindow();
		
		
		break;
	case FLAG_BIG_VIEW:
		removeSmallWindow();
		addBigWindow();
		
		break;
		
	default:
		break;
	}
	
}

public void cancleFloaltWindowView() {
	// TODO Auto-generated method stub
	removeBigWindow();
	removeSmallWindow();
}




	/**
	 * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 */
	public  void updateUsedPercent() {
		if (smallWindow != null) {
//			TextView percentView = (TextView) smallWindow.findViewById(R.id.percent);
//			percentView.setText(getUsedPercentValue(context));
		}
	}


	/**
	 * 按宽/高缩放图片到指定大小并进行裁剪得到中间部分图片
	 * 
	 * @param bitmap
	 *            源bitmap
	 * @param w
	 *            缩放后指定的宽度
	 * @param h
	 *            缩放后指定的高度
	 * @return 缩放后的中间部分图片
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleWidht, scaleHeight, x = 0, y = 0;
		Bitmap newbmp;
		Matrix matrix = new Matrix();
		
		scaleWidht = ((float)  w/ width);
		scaleHeight = ((float) h / height);
		if (scaleWidht>=1||scaleHeight>=1) {
			
		if (scaleWidht>scaleHeight) {
			
			matrix.postScale(scaleWidht, scaleWidht);
				
				x=0;
				y=(height*scaleWidht-h)/(2*scaleWidht);
			}else {
               matrix.postScale(scaleHeight, scaleHeight);
				
				x=(width*scaleHeight-w)/(2*scaleHeight);
				y=0;
			}
			
		}else {
			
			if (scaleWidht>scaleHeight) {
				
				matrix.postScale(scaleWidht, scaleWidht);
					
					x=0;
					y=(height*scaleWidht-h)/scaleWidht;
				}else {
	               matrix.postScale(scaleHeight, scaleHeight);
					
					x=(width*scaleHeight-h)/(2*scaleHeight);
					y=0;
				}
			
			
			
		}
		
//		if (width > height) {
//			scaleWidht = ((float) h / height);
//			scaleHeight = ((float) h / height);
//			x = (width - w * height / h) / 2;// 获取bitmap源文件中x做表需要偏移的像数大小
//			y = 0;
//		} else if (width < height) {
//			scaleWidht = ((float) w / width);
//			scaleHeight = ((float) w / width);
//			x = 0;
//			y = (height - h * width / w) / 2;// 获取bitmap源文件中y做表需要偏移的像数大小
//		} else {
//			scaleWidht = ((float) w / width);
//			scaleHeight = ((float) w / width);
//			x = 0;
//			y = 0;
//		}
//		matrix.postScale(scaleWidht, scaleHeight);
		try {
			newbmp = Bitmap.createBitmap(bitmap, (int) x, (int) y,
					(int) (width - x), (int) (height - y), matrix, true);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
		} catch (Exception e) {
			e.printStackTrace();
			return bitmap;
		}
		return newbmp;
	}
	/**
	 * 按宽/高缩放图片到指定大小并进行裁剪得到中间部分图片
	 * 
	 * @param bitmap
	 *            源bitmap
	 * @param w
	 *            缩放后指定的宽度

	 * @return 缩放后的中间部分图片
	 */
	public static Bitmap zoomBitmapOnWidth(Bitmap bitmap, int w) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleWidht = 1,scaleHeight=1 ;
		Bitmap newbmp;
		Matrix matrix = new Matrix();
		if (width > 0) {
			scaleWidht = ((float) w / width);
		}
		matrix.postScale(scaleWidht, scaleWidht);
		try {
			newbmp = Bitmap.createBitmap(bitmap, 0, 0,
					width, (int) height, matrix, true);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newbmp;
	}



}
