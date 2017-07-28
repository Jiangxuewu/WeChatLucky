package com.UU.uuaccelerator.view;

import java.util.ArrayList;
import java.util.List;

import com.UU.uuaccelerator.Impl.HexagonListener;
import com.UU.uuaccelerator.Impl.HexagonUpdateListaner;
import com.UU.uuaccelerator.Impl.LoopUpdateListaner;
import com.UU.uuaccelerator.Utils.SystemUtils;
import com.UU.uuaccelerator.model.Spot;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

@SuppressLint("NewApi")
public class HexagonView extends View {

	public static final int ACTION_VIBRATE=1;
	public static final int ACTION_SCAN=2;
	public static final int ACTION_SCAN_VIBRATE=3;
	private double radian60 = Math.toRadians(60);

	private float vCenterX;
	private float vCenterY;

	private float lineWidthMax = 6.0f;
	private float lineWidth = 6.0f;
	private float minSide;
	private float side;
	private float side2;
	private float maxSide;
	private float animValue;
	private float animValue2;
	private float ValueScan;

	// private boolean isStartView1=false;
	private boolean isScan = false;
	private boolean isVibrate=false;
	private boolean isAnimRunning = false;
	private boolean isFirstDraw = true;//用作初始化
	private boolean isAnimFinish = false;

	private Paint paint1;
	private Paint paint2;
	private Paint paintCheck;
	private SweepGradient gradient;

	private Context context;
	Matrix matrix;
	Path path;
	List<Spot> arraySpots = new ArrayList<Spot>();

	private ValueAnimator animVibrate = ValueAnimator.ofFloat(new float[] { 0.0f,
			1.0f });
	private ValueAnimator animator2 = ValueAnimator.ofFloat(new float[] { 0.0f,
			1.0f });
	private ValueAnimator animatorScan = ValueAnimator.ofFloat(new float[] {
			0.0f, 1.0f });
	
	

	
	
	
	public HexagonView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		init(context);
		
	}

	public HexagonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context=context;
		init(context);

		// this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}

	private void init(Context context) {
		minSide = SystemUtils.Dp2Px(context, 105);
		maxSide = SystemUtils.Dp2Px(context, 160);
		lineWidthMax=SystemUtils.Dp2Px(context, 4);
		lineWidth=SystemUtils.Dp2Px(context, 2);
		animVibrate.setDuration(2700L);
		animVibrate.addUpdateListener(new LoopUpdateListaner(this));
		animVibrate.addListener(new HexagonListener(this));
		// animator.setInterpolator(new LinearInterpolator());
		animVibrate.setInterpolator(new DecelerateInterpolator());
		animVibrate.setRepeatCount(Integer.MAX_VALUE);
		animVibrate.setRepeatMode(ValueAnimator.RESTART);
		animator2.setDuration(2700L);
		animator2.addUpdateListener(new HexagonUpdateListaner(this));
		animator2.addListener(new HexagonListener(this));
		// animator.setInterpolator(new LinearInterpolator());
		animator2.setInterpolator(new DecelerateInterpolator());
		animator2.setRepeatCount(Integer.MAX_VALUE);
		animator2.setRepeatMode(ValueAnimator.RESTART);
		animatorScan.setDuration(2700L);
		animatorScan.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				ValueScan = ((Float) animation.getAnimatedValue())
						.floatValue();
				postInvalidate();
			}
		});
		animatorScan.addListener(new HexagonListener(this));
		// animator3.setInterpolator(new DecelerateInterpolator());
		animatorScan.setRepeatCount(Integer.MAX_VALUE);
		animatorScan.setRepeatMode(ValueAnimator.INFINITE);
	}

	private Path getHexagonPath(float sideF) {

		float tempX = (float) (sideF * Math.sin(radian60));
		float tempY = (float) (sideF * Math.cos(radian60));

		Path path = new Path();
		path.moveTo(vCenterX, vCenterY - 2 * tempY);// 第一点
		path.lineTo(vCenterX + tempX, vCenterY - tempY);// 第2点
		path.lineTo(vCenterX + tempX, vCenterY + tempY);// 第3点
		path.lineTo(vCenterX, vCenterY + 2 * tempY);// 第4点
		path.lineTo(vCenterX - tempX, vCenterY + tempY);// 第5点
		path.lineTo(vCenterX - tempX, vCenterY - tempY);// 第6点
		path.close();

		return path;

	}

	public void changeHexagon(float param) {
		// animValue=mathAnimValue(param);
		animValue = param;
		side = minSide + animValue * (maxSide - minSide)+5;
		if (animValue >= 0.25 && !animator2.isRunning()) {
			animator2.start();
			// isStartView2=true;
		}

	}

	public void changeHexagon2(float floatValue) {
		// TODO Auto-generated method stub
		animValue2 = floatValue;
		side2 = minSide + animValue2 * (maxSide - minSide)+5;
	}

	

	public void startAnim(int action) {

		if (isAnimRunning) {
			stopAnim();
		}
		isAnimFinish = false;
		isAnimRunning = true;
		switch (action) {
		case ACTION_SCAN:
			isScan=true;
			isVibrate=false;
			animatorScan.start();
			break;
		case ACTION_VIBRATE:
			isScan=false;
			isVibrate=true;
			animVibrate.start();
			break;
		case ACTION_SCAN_VIBRATE:
			isScan=true;
			isVibrate=true;
			animVibrate.start();
			animatorScan.start();
			break;

		default:
			break;
		}
		
		
		
//		animVibrate.start();
//		animatorScan.start();
		// isStartView1=true;
	}


	private float degrees = 270.0F;

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (isFirstDraw) {
			isFirstDraw = false;
			paint1 = new Paint();
			paint1.setAntiAlias(true);
			paint1.setStrokeWidth(lineWidth);
			paint1.setStyle(Paint.Style.STROKE);
			paint1.setColor(Color.WHITE);
			paint1.setStrokeCap(Paint.Cap.ROUND);

			vCenterX = getWidth() / 2;
			vCenterY = getHeight() / 2;
			side = minSide;
			side2 = minSide;
			CornerPathEffect cornerPathEffect = new CornerPathEffect(5.0F);
			paint1.setPathEffect(cornerPathEffect);

			paint2 = new Paint();
			paint2.setAntiAlias(true);
			paint2.setStrokeWidth(this.lineWidth);
			paint2.setStyle(Paint.Style.STROKE);
			paint2.setColor(Color.WHITE);
			paint2.setPathEffect(cornerPathEffect);
			paint2.setStrokeCap(Paint.Cap.ROUND);

			paintCheck = new Paint();
			paintCheck.setAntiAlias(true);
			paintCheck.setStrokeWidth(this.lineWidth );
			paintCheck.setStyle(Paint.Style.STROKE);
			paintCheck.setColor(Color.WHITE);
			paintCheck.setPathEffect(cornerPathEffect);
			paintCheck.setStrokeCap(Paint.Cap.ROUND);

			int[] colors = new int[2];
			colors[1] = Color.WHITE;
			matrix = new Matrix();
			matrix.setRotate(degrees, vCenterX, vCenterY);
			gradient = new SweepGradient(vCenterX, vCenterY, colors, null);
			gradient.setLocalMatrix(matrix);
			paintCheck.setShader(gradient);

		}

		if (isScan) {
			degrees = 270 + 360 * ValueScan;
			if (degrees >= 630) {
				degrees = 270f;
			}
			paintCheck.setStrokeWidth(lineWidthMax);
		
			matrix.reset();
			matrix.setRotate(degrees, vCenterX, vCenterY);
			gradient.setLocalMatrix(matrix);
			paintCheck.setShader(gradient);
			canvas.drawPath(getHexagonPath(minSide), paintCheck);

		}else {
			path = getHexagonPath(minSide);
			paint1.setAlpha(255);
			paint1.setStrokeWidth(lineWidthMax);
			canvas.drawPath(path, paint1);
		}
		if (isAnimFinish) {
			shadeAnmi(canvas);
			return;
		}
		if (isVibrate) {
		
			viewStartAnim(canvas);
		}
	}

	private void viewStartAnim(Canvas canvas) {
		
		
		paint1.setStrokeWidth(lineWidth);
		path = getHexagonPath(side);
		paint1.setAlpha((int) ((1 - animValue) * 255*0.6));
		canvas.drawPath(path, paint1);
		path = getHexagonPath(side2);
		paint2.setAlpha((int) ((1 - animValue2) * 255*0.6));
		canvas.drawPath(path, paint2);
	}

	private void shadeAnmi(Canvas canvas) {
		// TODO Auto-generated method stub
		paint1.setStrokeWidth(lineWidth);
		Path midPath = getHexagonPath(minSide + (maxSide - minSide) / 2);
		Path maxPath = getHexagonPath(maxSide);
		paint1.setAlpha((int) (0.4 * 255));
		canvas.drawPath(midPath, paint1);
		paint1.setAlpha((int) (0.1 * 255));
		canvas.drawPath(maxPath, paint1);
		if (animVibrate.isRunning()) {
			animVibrate.cancel();
		}

	}

	public void stopAnim() {
		// TODO Auto-generated method stub
		isAnimFinish = true;
		isAnimRunning = false;
		isScan=false;
		isVibrate=false;
		if (animVibrate.isRunning()) {
			animVibrate.cancel();
		}
		if (animator2.isRunning()) {
			animator2.cancel();
		}
	}
//@Override
//protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//	// TODO Auto-generated method stub
//	int mode=MeasureSpec.AT_MOST;
//	setMeasuredDimension(mode+SystemUtils.Dp2Px(context, 300),mode+SystemUtils.Dp2Px(context, 400));
//}
}
