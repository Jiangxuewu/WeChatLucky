package com.UU.uuaccelerator.Impl;

import com.UU.uuaccelerator.view.HexagonView;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("NewApi")
public class LoopUpdateListaner implements AnimatorUpdateListener {
    
	HexagonView hexagonView;
	
	public LoopUpdateListaner(HexagonView hexagonView) {
		super();
		this.hexagonView = hexagonView;
	}

	@Override
	public void onAnimationUpdate(ValueAnimator value) {
		// TODO Auto-generated method stub
//Log.e("onAnimationUpdate=", "onAnimationUpdate="+value.getAnimatedValue());
hexagonView.changeHexagon(((Float) value.getAnimatedValue()).floatValue());
hexagonView.postInvalidate();

	}

}
