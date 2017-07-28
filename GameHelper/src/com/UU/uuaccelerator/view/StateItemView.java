package com.UU.uuaccelerator.view;

import java.io.IOException;

import com.UU.uuaccelerator.Utils.SystemUtils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StateItemView extends LinearLayout {
	
	Context context;
	ImageView img_ok;
	TextView mTextView;
	String text;

	public StateItemView(Context context,String text) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.text=text;
		setOrientation(LinearLayout.HORIZONTAL);
		init();
	}


	private void init() {
		// TODO Auto-generated method stub
		addImgView();
		 addTextView();
	}


	private void addImgView() {
		// TODO Auto-generated method stub
		img_ok=new ImageView(context);
		
		LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 0, 0, SystemUtils.Dp2Px(context, 5));
//		layoutParams.weight=3;
		img_ok.setLayoutParams(layoutParams);
		Bitmap bitmap=null;
		try {
			 bitmap=BitmapFactory.decodeStream(context.getAssets().open("gamespeedup/speedup_gou.png"));
			 img_ok.setImageBitmap(bitmap);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addView(img_ok);
	}
	
	
	private void addTextView() {
		// TODO Auto-generated method stub
		mTextView=new TextView(context);
		
		LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(SystemUtils.Dp2Px(context, 5), 0, 0, 0);
//		layoutParams.weight=2;
		mTextView.setLayoutParams(layoutParams);
		mTextView.setGravity(Gravity.CENTER_VERTICAL);
		mTextView.setTextColor(Color.BLACK);
		mTextView.setText(text);
		addView(mTextView);
	}
	
	public void setText(String text){
		mTextView.setText(text);
	}
	

}
