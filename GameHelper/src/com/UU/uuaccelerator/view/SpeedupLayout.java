package com.UU.uuaccelerator.view;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class SpeedupLayout extends LinearLayout {
	Layout_Hexagons_View layout_Hexagons_View;
	Context context;
	StateView stateView;
	
	Linear_game_speed linear_game_speed;
	

	public SpeedupLayout(Context context) {
		super(context);
		this.context=context;
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER_HORIZONTAL);
						
		
		LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,3);
		
		layout_Hexagons_View=new Layout_Hexagons_View(context);
		layout_Hexagons_View.setLayoutParams(layoutParams);
		
		
		stateView=new StateView(context);
		stateView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,5));
		
		linear_game_speed=new Linear_game_speed(context);
		
		linear_game_speed.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,5));
		    addView(layout_Hexagons_View);
		    addView(linear_game_speed);
			addView(stateView);
			
	}

	public Layout_Hexagons_View getLayout_Hexagons_View() {
		return layout_Hexagons_View;
	}

	public StateView getStateView() {
		return stateView;
	}

	public Linear_game_speed getLinear_Game_speed() {
		return linear_game_speed;
	}
	public void  startAnim(){
		if (layout_Hexagons_View!=null) {
			
			layout_Hexagons_View.startAnim();
		}
	}


}
