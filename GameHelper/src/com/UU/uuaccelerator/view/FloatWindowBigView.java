package com.UU.uuaccelerator.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;

import com.UU.uuaccelerator.Utils.FloatWindowManager;

import java.io.IOException;

public class FloatWindowBigView extends ImageView {

	ImageView view;
	android.view.ViewGroup.LayoutParams params;
	Context mContext;
	public FloatWindowBigView(final Context context) {
		super(context);
//		LayoutInflater.from(context).inflate(R.layout.accesbility_guide_detail ,this);
		mContext=context;
//		view=new ImageView(context);
         initView();
	}

	private void initView() {
		params=	new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);
		setScaleType(ScaleType.FIT_START);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(mContext.getAssets().open("gamespeedup/accesbility_guide_slide_img.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	WindowManager manager=	(WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
	
//		view.setImageBitmap(bitmap);
		setImageBitmap(FloatWindowManager.zoomBitmapOnWidth(bitmap,manager.getDefaultDisplay().getWidth() ));
//		addView(view);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
		FloatWindowManager.getInstance().showFloaltWindowView(FloatWindowManager.FLAG_SMALL_VIEW);
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
