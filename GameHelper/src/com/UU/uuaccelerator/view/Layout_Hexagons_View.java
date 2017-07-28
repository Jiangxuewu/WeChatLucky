package com.UU.uuaccelerator.view;

import java.util.HashMap;

import com.UU.uuaccelerator.Utils.SystemUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Layout_Hexagons_View extends RelativeLayout {
	
	
	HexagonView hexagonView;
	HexagonContenView hexagonContenView;
	
	Context context;

	public Layout_Hexagons_View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		int pad=SystemUtils.Dp2Px(context, 5);
		setPadding(pad, pad, pad, pad);
		setBackgroundColor(Color.parseColor("#0064b2"));
		init();
		
	}


	private void init() {
		// TODO Auto-generated method stub
		hexagonView=new HexagonView(context);
		hexagonContenView=new HexagonContenView(context);
		LayoutParams layoutParams1=new LayoutParams(SystemUtils.Dp2Px(context, 300),SystemUtils.Dp2Px(context, 400));
		LayoutParams layoutParams2=new LayoutParams(SystemUtils.Dp2Px(context, 182),SystemUtils.Dp2Px(context, 210));
		layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
		layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		hexagonView.setLayoutParams(layoutParams1);
		hexagonContenView.setLayoutParams(layoutParams2);
		
		addView(hexagonView);
		addView(hexagonContenView);
	}


	public HexagonView getHexagonView() {
		return hexagonView;
	}

	public HexagonContenView getHexagonContenView() {
		return hexagonContenView;
	}


	public void  startAnim(){
		if (hexagonContenView!=null) {
			
			hexagonContenView.startAnim(HexagonContenView.ACTION_SCAN);
		}
		if (hexagonView!=null) {
			
			hexagonView.startAnim(HexagonView.ACTION_SCAN_VIBRATE);
		}
	}

}
