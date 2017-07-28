package com.UU.uuaccelerator.view;


import com.UU.uuaccelerator.Utils.SystemUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Linear_game_speed extends LinearLayout {
	Context context;
	
	TextView tv_speed_up;
	TextView  tv_game_check;

	public Linear_game_speed(Context context) {
		super(context);
		this.context=context;
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
						
		tv_speed_up=new TextView(context);
		LayoutParams speedParams=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	
		tv_speed_up.setLayoutParams(speedParams);
		tv_speed_up.setText("加速游戏");
		tv_speed_up.setBackgroundColor(Color.parseColor("#4f9de0"));
		tv_speed_up.setPadding(SystemUtils.Dp2Px(context, 30), SystemUtils.Dp2Px(context, 10), SystemUtils.Dp2Px(context, 30), SystemUtils.Dp2Px(context, 10));
		tv_speed_up.setGravity(Gravity.CENTER);
		tv_speed_up.setTextColor(Color.WHITE);
		tv_speed_up.setTextSize(18);
		
		tv_game_check=new TextView(context);
		LayoutParams checkParams=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		speedParams.gravity=Gravity.CENTER;
		tv_game_check.setLayoutParams(checkParams);
		tv_game_check.setText("游戏使用环境检测中...");
		tv_game_check.setPadding(SystemUtils.Dp2Px(context, 10), SystemUtils.Dp2Px(context, 10), SystemUtils.Dp2Px(context, 10), SystemUtils.Dp2Px(context, 10));
		tv_game_check.setGravity(Gravity.CENTER);
		tv_game_check.setTextColor(Color.WHITE);
		tv_game_check.setTextSize(16);
		
		addView(tv_speed_up);
		addView(tv_game_check);
		tv_speed_up.setVisibility(View.GONE);
		
	}
	public TextView getTv_speed_up() {
		return tv_speed_up;
	}
	public TextView getTv_game_check() {
		return tv_game_check;
	}



}
