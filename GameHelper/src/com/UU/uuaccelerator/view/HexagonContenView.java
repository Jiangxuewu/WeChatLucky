package com.UU.uuaccelerator.view;

import java.io.IOException;

import com.UU.uuaccelerator.Impl.AnimStopListener;
import com.UU.uuaccelerator.Utils.FloatWindowManager;
import com.UU.uuaccelerator.Utils.SystemUtils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

@SuppressLint("NewApi")
public class HexagonContenView extends View {

	public static final int ACTION_SCAN = 1;
	public static final int ACTION_ACCELERATE = 3;

	private int action_flag = ACTION_SCAN;

	private double radian60 = Math.toRadians(60);

	private float vCenterX;
	private float vCenterY;

	private float rCount = 1;
	private float hexDist = 50f;

	private float hexSide;
	private float hexUnitDist = 0.0f;
	private float lineWidth = 10f;
	private float lineMaxLength = 40f;
	private float lineMinLength = 15f;
	private float lineDist = 25f;
	private float LoopChangeValue;
	private float inChangeValue;
	private float outChangeValue;
	private float scanChangeValue;
	private float interval;

	private boolean isAnimIn = false;
	private boolean isAnimRunning = false;
	private boolean isAnimStop = true;
	private boolean isAnimOut = false;
	private boolean isCloudSHow = false;
	private boolean isAnimScan = false;
	private boolean isGameAccelerate = false;
	private boolean isdrawOKText = false;
	

	private Bitmap bitmapRocket;
	private Bitmap bitmapCloud;
	private Bitmap bitmapExc;
	private Bitmap bitmap_zhezhao;
	private Bitmap bitmap_dianlu;

	private Rect rectRocketB;
	private RectF rectRocketD;
	private Rect rectExcB;
	private RectF rectExcD;
	private Paint paint_line;
	private Paint paint_dianlu;

	private AnimStopListener  animStopListener;
	private Context mContext;
	private ShapeDrawable shapeDrawable;
	private Path path;


	public void setIsdrawOKText(boolean isdrawOKText) {
		this.isdrawOKText = isdrawOKText;
		isCloudSHow=false;
		postInvalidate();
	}


	public void setAnimStopListener(AnimStopListener animStopListener) {
		this.animStopListener = animStopListener;
	}

	private ValueAnimator loopAnimator = ValueAnimator.ofFloat(new float[] {
			0.0f, 1.0f });
	private ValueAnimator inAnimator = ValueAnimator.ofFloat(new float[] {
			0.0f, 1.0f });
	private ValueAnimator outAnimator = ValueAnimator.ofFloat(new float[] {
			0.0f, 1.0f });
	private ValueAnimator animatorScan = ValueAnimator.ofFloat(new float[] {
			0.0f, 1.0f });

	public HexagonContenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init(context);
	}


	public HexagonContenView(Context context) {
		super(context);
		mContext = context;
		init(context);
	}


	private void init(Context context) {
		hexDist = SystemUtils.Dp2Px(context, 15.0f);
		lineWidth = SystemUtils.Dp2Px(context, 4f);
		interval = SystemUtils.Dp2Px(context, 5f);
		lineMaxLength = SystemUtils.Dp2Px(context, 20f);
		lineMinLength = SystemUtils.Dp2Px(context, 10f);
		lineDist = lineMaxLength - lineMinLength;
		hexSide = SystemUtils.Dp2Px(context, 105);

		initLoopAnim();
		initInAnim();
		initOutAnim();
		initScanAnim();
		try {
			bitmapRocket = BitmapFactory.decodeStream(context.getAssets().open("gamespeedup/setting_rocket.png"));
			bitmapCloud = BitmapFactory.decodeStream(context.getAssets().open("gamespeedup/setting_cloud.png"));
		bitmapExc = BitmapFactory.decodeStream(context.getAssets().open("gamespeedup/setting_exclamation_mark.png"));
		bitmap_zhezhao = BitmapFactory.decodeStream(context.getAssets().open("gamespeedup/setting_zhezhao.png"));
		bitmap_dianlu = BitmapFactory.decodeStream(context.getAssets().open("gamespeedup/setting_dianlu.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rectRocketB = new Rect(0, 0, bitmapRocket.getWidth(),
				bitmapRocket.getHeight());
		rectExcB = new Rect(0, 0, bitmapExc.getWidth(), bitmapExc.getHeight());
		paint_line = new Paint();
		paint_line.setAntiAlias(true);
		paint_line.setStrokeWidth(lineWidth);
		paint_line.setStyle(Paint.Style.STROKE);
		paint_line.setStrokeCap(Cap.ROUND);
		paint_line.setColor(Color.WHITE);

		paint_dianlu = new Paint();
		paint_dianlu.setStyle(Paint.Style.STROKE);
		paint_dianlu.setColor(-1);
		paint_dianlu.setAntiAlias(true);
	}

	private void initScanAnim() {
		animatorScan.setDuration(1350L);
		animatorScan.setInterpolator(new DecelerateInterpolator());
		animatorScan.setRepeatCount(Integer.MAX_VALUE);
		animatorScan.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				isAnimStop=false;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				isAnimScan = false;
			
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				isAnimScan = false;
			}
		});
		animatorScan.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				scanChangeValue = ((Float) animation.getAnimatedValue())
						.floatValue();

				postInvalidate();
			}
		});
	}

	private void initInAnim() {
		inAnimator.setDuration(800L);
		inAnimator.setInterpolator(new LinearInterpolator());
		// inAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		inAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				isAnimIn = true;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				isAnimIn = false;
				if (!isAnimOut) {

					loopAnimator.start();

				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				isAnimIn = false;
			}
		});
		inAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				inChangeValue = ((Float) animation.getAnimatedValue())
						.floatValue();

				postInvalidate();
			}
		});
	}

	private void initOutAnim() {
		outAnimator.setDuration(675L);
		outAnimator.setInterpolator(new LinearInterpolator());
		// inAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		outAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
//				hexagonView.stopAnim();
				isAnimRunning = false;
				isAnimOut = true;
				isAnimStop = false;
				isCloudSHow = true;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub

				isAnimOut = false;
				isAnimStop=true;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				isAnimOut = false;
				isAnimStop=true;
			}
		});
		outAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				outChangeValue = ((Float) animation.getAnimatedValue())
						.floatValue();
				postInvalidate();
			}
		});
	}

	private void initLoopAnim() {
		loopAnimator.setDuration(675L);
		loopAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		loopAnimator.setRepeatCount(Integer.MAX_VALUE);
		loopAnimator.setRepeatMode(ValueAnimator.REVERSE);
		loopAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				LoopChangeValue = ((Float) animation.getAnimatedValue())
						.floatValue();
				hexUnitDist = LoopChangeValue * hexDist;
				postInvalidate();
			}
		});
		loopAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				isAnimIn = false;
				isAnimRunning = true;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				isAnimRunning = false;
				if (!isAnimStop) {

					outAnimator.start();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				isAnimRunning = false;
				// outAnimator.start();
			}
		});
	}

	public void startAnim(int action) {
		action_flag = action;
		if (!isAnimStop) {
			stopAnim();
		}
		isAnimStop = false;
		isCloudSHow = false;
		inChangeValue = 0f;
		LoopChangeValue = 0f;
		outChangeValue = 0f;
		inAnimator.cancel();
		loopAnimator.cancel();
		animatorScan.cancel();
		// inAnimator.start();
		// animatorScan.start();

		switch (action_flag) {
		case ACTION_SCAN:
			isAnimScan = true;
			isGameAccelerate = false;
			isCloudSHow = false;
			animatorScan.start();
//			hexagonView.startAnim(HexagonView.ACTION_SCAN);
			break;
		case ACTION_ACCELERATE:
			isAnimScan = false;
			isCloudSHow = true;
			isGameAccelerate = true;
			inAnimator.start();
//			hexagonView.startAnim(HexagonView.ACTION_VIBRATE);
			break;

		default:
			break;
		}
	}

	public void stopAnim() {
		// TODO Auto-generated method stub
		
		
		
		
		if (!isAnimStop && !isAnimOut) {

			isAnimRunning = false;
			isCloudSHow=false;
			outChangeValue = 0f;
				LoopChangeValue = 0f;
				hexUnitDist = 0f;
			loopAnimator.cancel();
			inAnimator.cancel();
			if (isAnimScan) {
				
				animatorScan.cancel();
				isAnimStop=true;
				
			}else {
				isAnimOut = true;
				outAnimator.start();
			}
	
			
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		vCenterX = w / 2;
		vCenterY = h / 2;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		path = getHexagonPath(hexSide);
		canvas.clipPath(path);
	

		if (isCloudSHow) {
			PathShape s = new PathShape(path, getWidth(), getHeight());
			shapeDrawable = new ShapeDrawable(s);

			Shader shader = new BitmapShader(FloatWindowManager.zoomBitmap(bitmapCloud,
					getWidth(), getHeight()), Shader.TileMode.MIRROR,
					Shader.TileMode.MIRROR);
			shapeDrawable.getPaint().setShader(shader);
			shapeDrawable.setBounds(0, 0, getWidth(), getHeight());
			shapeDrawable.draw(canvas);
		}
		if (isAnimScan) {
			int i = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
					Canvas.ALL_SAVE_FLAG);
			Bitmap bitmapD = FloatWindowManager.zoomBitmap(bitmap_dianlu, getWidth(), getHeight());
			paint_dianlu.setAlpha(76);
			canvas.drawBitmap(bitmapD, vCenterX - bitmapD.getWidth() / 2,
					vCenterY - bitmapD.getHeight() / 2, paint_dianlu);
			paint_dianlu.setAlpha(255);
			
			Bitmap bitmapZ = FloatWindowManager.zoomBitmap(bitmap_zhezhao, getWidth(), getHeight());
			canvas.drawBitmap(bitmapZ, vCenterX - bitmapZ.getWidth() / 2,
					(getHeight() + bitmapZ.getHeight())
							* scanChangeValue - bitmapZ.getHeight(),
					paint_dianlu);
			paint_dianlu.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmapD, vCenterX - bitmapD.getWidth() / 2,
					vCenterY - bitmapD.getHeight() / 2, paint_dianlu);
			paint_dianlu.setXfermode(null);
			canvas.restoreToCount(i);

		} else {

			if (isAnimIn) {
				drawInView(canvas, inChangeValue);
				return;

			}
			if (isAnimOut) {
				drawOutView(canvas, outChangeValue);
				return;
			}
			if (isAnimRunning) {

				drawLoopView(canvas, LoopChangeValue);
				return;
			}
			if (isdrawOKText) {
				Paint paintText=new Paint();
				paintText.setAntiAlias(true);
				paintText.setTextSize(SystemUtils.Dp2Px(mContext, 120));
				paintText.setColor(Color.WHITE);
				
				canvas.drawText("√", vCenterX-SystemUtils.Dp2Px(mContext, 40), vCenterY+SystemUtils.Dp2Px(mContext, 40), paintText);
				if (animStopListener!=null) {
					animStopListener.onAnimationStop();
				}
				return;
//				canvas.drawText("完成加速", vCenterX, vCenterX, paintText);
			}
			rectExcD = new RectF(vCenterX - bitmapRocket.getWidth() / 2,
					vCenterY - bitmapRocket.getHeight() / 2, vCenterX
							+ bitmapRocket.getWidth() / 2, vCenterY
							+ bitmapRocket.getHeight() / 2);
			canvas.drawBitmap(bitmapExc, rectExcB, rectExcD, new Paint());
		}
	}

	private void drawInView(Canvas canvas, float animValue) {
		rectRocketD = new RectF(vCenterX - bitmapRocket.getWidth() / 2,
				vCenterY * 2 - (vCenterY + bitmapRocket.getHeight() / 2)
						* animValue, vCenterX + bitmapRocket.getWidth() / 2,
				vCenterY * 2 - (vCenterY + bitmapRocket.getHeight() / 2)
						* animValue + bitmapRocket.getHeight());
		canvas.drawBitmap(bitmapRocket, rectRocketB, rectRocketD, new Paint());
	}

	void drawLoopView(Canvas canvas, float animValue) {

		rectRocketD = new RectF(vCenterX - bitmapRocket.getWidth() / 2,
				vCenterY - hexUnitDist - bitmapRocket.getHeight() / 2, vCenterX
						+ bitmapRocket.getWidth() / 2, vCenterY - hexUnitDist
						+ bitmapRocket.getHeight() / 2);
		canvas.drawBitmap(bitmapRocket, rectRocketB, rectRocketD, new Paint());

		float startX1 = vCenterX;
		float startY1 = vCenterY + bitmapRocket.getHeight() / 2 - hexUnitDist
				+ interval;
		float stopX1 = vCenterX;
		float stopY1 = vCenterY + bitmapRocket.getHeight() / 2 + lineMaxLength
				- hexUnitDist + interval - lineDist * animValue;

		float startX2 = vCenterX + 30;
		float startY2 = startY1;
		float stopX2 = vCenterX + 30;
		float stopY2 = vCenterY + bitmapRocket.getHeight() / 2 + interval
				+ lineMaxLength - hexUnitDist - lineDist * (1 - animValue);

		canvas.drawLine(startX1, startY1, stopX1, stopY1, paint_line);
		canvas.drawLine(startX2, startY2, stopX2, stopY2, paint_line);
		canvas.drawLine(vCenterX - 30, startY2, vCenterX - 30, stopY2,
				paint_line);

	}

	private void drawOutView(Canvas canvas, float animValue) {

		rectRocketD = new RectF(vCenterX - bitmapRocket.getWidth() / 2,
				vCenterY - hexUnitDist - bitmapRocket.getHeight() / 2
						- vCenterY * 2 * animValue, vCenterX
						+ bitmapRocket.getWidth() / 2, vCenterY - hexUnitDist
						+ bitmapRocket.getHeight() / 2 - vCenterY * 2
						* animValue);

		canvas.drawBitmap(bitmapRocket, rectRocketB, rectRocketD, new Paint());

		float startX1 = vCenterX;
		float startY1 = vCenterY + bitmapRocket.getHeight() / 2 - hexUnitDist
				+ interval - vCenterY * 2 * animValue;
		float stopX1 = vCenterX;
		float stopY1 = vCenterY + bitmapRocket.getHeight() / 2 + lineMaxLength
				- hexUnitDist - (lineMaxLength - lineMinLength)
				* LoopChangeValue - vCenterY * 2 * animValue;

		float startX2 = vCenterX + 30;
		float startY2 = startY1;
		float stopX2 = vCenterX + 30;
		float stopY2 = vCenterY + bitmapRocket.getHeight() / 2 + lineMaxLength
				+ interval - hexUnitDist - (lineMaxLength - lineMinLength)
				* (1 - LoopChangeValue) - vCenterY * 2 * animValue;

		canvas.drawLine(startX1, startY1, stopX1, stopY1, paint_line);
		canvas.drawLine(startX2, startY2, stopX2, stopY2, paint_line);
		canvas.drawLine(vCenterX - 30, startY2, vCenterX - 30, stopY2,
				paint_line);
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

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// TODO Auto-generated method stub
//		
////		setMeasuredDimension(MeasureSpec.UNSPECIFIED+SystemUtils.Dp2Px(mContext, 182),MeasureSpec.UNSPECIFIED+SystemUtils.Dp2Px(mContext, 210));
//		
//		
//		
//	}
}
