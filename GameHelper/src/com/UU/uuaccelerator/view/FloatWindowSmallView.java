package com.UU.uuaccelerator.view;

import java.io.IOException;
import java.lang.reflect.Field;

import com.UU.uuaccelerator.Utils.FloatWindowManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class FloatWindowSmallView extends LinearLayout {

	ImageView view;
	android.view.ViewGroup.LayoutParams params;
	Context mContext;
	/**
	 * 记录系统状态栏的高度
	 */
	private static int statusBarHeight;



	public FloatWindowSmallView(Context context) {
		super(context);
//		LayoutInflater.from(context).inflate(R.layout.accesbility_guide_setting,
//				this);
		mContext=context;
		view=new ImageView(context);
         initView();
		
//		setAnimation(animation)
	}
	private void initView() {
		params=	new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(params);
	
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(mContext.getAssets().open("gamespeedup/accessibility_float_img.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WindowManager manager=	(WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		
		view.setImageBitmap(FloatWindowManager.zoomBitmapOnWidth(bitmap,manager.getDefaultDisplay().getWidth()/4 ));
		addView(view);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
		FloatWindowManager.getInstance().showFloaltWindowView(FloatWindowManager.FLAG_BIG_VIEW);
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}



	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}

}
