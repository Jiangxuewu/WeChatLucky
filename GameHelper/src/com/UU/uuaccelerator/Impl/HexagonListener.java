package com.UU.uuaccelerator.Impl;

import com.UU.uuaccelerator.view.HexagonView;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.view.animation.Animation;

@SuppressLint("NewApi")
public class HexagonListener implements  AnimatorListener {
HexagonView hexagonView;
	
	
	public HexagonListener(HexagonView hexagonView) {
	super();
	this.hexagonView = hexagonView;
}


	@Override
	public void onAnimationStart(Animator animation) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAnimationEnd(Animator animation) {
		// TODO Auto-generated method stub
		hexagonView.stopAnim();
		hexagonView.postInvalidate();
	}


	@Override
	public void onAnimationCancel(Animator animation) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub
		
	}


}
