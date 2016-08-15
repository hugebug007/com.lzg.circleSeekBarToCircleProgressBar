package com.lzg.circleseekbar.widget;

/**
 * @author lzg
 * 2014/4/13
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lzg.circleseekbar.R;

/**
 * The Class CircularSeekBar.
 */
public class MCircleSeekBar extends View {
	/** The context */
	private Context mContext;
	/** seekbar变化监听 */
	private OnSeekChangeListener mListener;
	/** 画圆环的paint */
	private Paint circleColor;
	/** 画内圆的paint */
	private Paint innerColor;
	/** 圆环的Paint，其实是画弧形 */
	private Paint circleRing;
	/** 移动之后的角度 */
	private int angle = 0;
	/** 12点位置的开始角度 */
	private int startAngle = 270;
	/** 圆环宽度 */
	private int barWidth = 30;
	/** view的宽 */
	private int width;
	/** view的高 */
	private int height;
	/** seekbar总值 */
	private int maxProgress = 100;
	/** 当前的百分值 */
	private int progress;
	/** 百分值 */
	private int progressPercent;
	/** 内圆半径 */
	private float innerRadius;
	/** 外圆半径 */
	private float outerRadius;
	/** 圆心X坐标 */
	private float cx;
	/** 圆心Y坐标 */
	private float cy;
	/** 画圆图层左边边距 */
	private float left;
	/** 画圆图层右边边距 */
	private float right;
	/** T画圆图层顶端边距 */
	private float top;
	/** 画圆图层底边边距 */
	private float bottom;
	/** 背景图左边边距 */
	private float bgLeft;
	/** 背景图右边边距 */
	private float bgRight;
	/** 背景图顶端边距 */
	private float bgTop;
	/** 背景图底边边距 */
	private float bgBottom;
	/** progressMark X坐标 */
	private float dx;
	/** progressMark Y坐标 */
	private float dy;
	/** 12点钟位置的X坐标 */
	private float startPointX;
	/** 12点钟位置的Y坐标 */
	private float startPointY;
	/**
	 * 标记的seekbar当前位置的X坐标，预设置值为12点位置
	 */
	private float markPointX;
	/**
	 * T标记的seekbar当前位置的Y坐标，预设置值为12点位置
	 */
	private float markPointY;
	/**
	 * 进度调整指数
	 */
	private float adjustmentFactor = 100;
	/** 正常状态下的进度条点 */
	private Bitmap progressMark;
	/** 手指按下后的进度条点 */
	private Bitmap progressMarkPressed;
	/**
	 * 中间背景图
	 */
	private Bitmap progressBg;

	/** 是否手指按下的标志位 */
	private boolean IS_PRESSED = false;
	/**
	 * 绘制圆中间的背景图的Paint
	 */
	private Paint paint = null;
	private Canvas canvas = new Canvas();
	/** 画圆图层. */
	private RectF rect = new RectF();
	/** 背景图层 */
	RectF rectBg = new RectF();
	/** 是否显示progressBar */
	private boolean showProgressBar = true;
	/**
	 * 是否显示内圆，不显示就做成了圆形progressBar，显示就是圆环形progressBar
	 */
	private boolean isRingMode = true;
	/**
	 * 是否可以move
	 */
	private boolean canMove = true;

	{
		mListener = new OnSeekChangeListener() {
			@Override
			public void onProgressChange(MCircleSeekBar view, int newProgress) {
			}
		};
		/*
		 * Paint.ANTI_ALIAS_FLAG为抗锯齿
		 */
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circleColor = new Paint();
		innerColor = new Paint();
		circleRing = new Paint();
		/*
		 * 设置外圆的颜色为蓝色
		 */
		circleColor.setColor(Color.GRAY);// Set default background color to Gray
		/*
		 * 设置内圆的颜色
		 */
		innerColor.setColor(Color.parseColor("#313131"));
		/*
		 * 设置圆环的颜色
		 */
		circleRing.setColor(Color.parseColor("#ff33b5e5"));
		/*
		 * Paint.ANTI_ALIAS_FLAG为抗锯齿
		 */
		circleColor.setAntiAlias(true);
		innerColor.setAntiAlias(true);
		circleRing.setAntiAlias(true);
		circleColor.setStrokeWidth(5);
		innerColor.setStrokeWidth(5);
		circleRing.setStrokeWidth(5);
		circleRing.setStyle(Paint.Style.FILL);
	}

	/**
	 * MCircleSeekBar的构造方法.
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MCircleSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initDrawable();
	}

	/**
	 * MCircleSeekBar的构造方法.
	 * 
	 * @param context
	 * @param attrs
	 */
	public MCircleSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initDrawable();
	}

	/**
	 * MCircleSeekBar的构造方法.
	 * 
	 * @param context
	 */
	public MCircleSeekBar(Context context) {
		super(context);
		mContext = context;
		initDrawable();
	}

	/**
	 * 初始化图片
	 */
	public void initDrawable() {
		progressMark = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.seek_bar);
		progressMarkPressed = BitmapFactory.decodeResource(
				mContext.getResources(), R.drawable.seek_bar1);
		progressBg = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.open_perecent_bg);
	}

	/*
	 * 重写view的计算方法
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getWidth(); // Get View Width
		height = getHeight();// Get View Height
		int size = (width > height) ? height : width; // Choose the smaller
		cx = width / 2; // Center X for circle
		cy = height / 2; // Center Y for circle
		outerRadius = size / 2 * 80 / 100; // Radius of the outer circle
		innerRadius = outerRadius - barWidth; // Radius of the inner circle
		left = cx - outerRadius; // Calculate left bound of our rect
		right = cx + outerRadius;// Calculate right bound of our rect
		top = cy - outerRadius;// Calculate top bound of our rect
		bottom = cy + outerRadius;// Calculate bottom bound of our rect
		bgLeft = cx - innerRadius;
		bgRight = cx + innerRadius;
		bgTop = cy - innerRadius;
		bgBottom = cy + innerRadius;

		startPointX = getXByProgress(progress);
		startPointY = getYByProgress(progress);
		markPointX = startPointX;// Initial locatino of the marker X coordinate
		markPointY = startPointY;// Initial locatino of the marker Y coordinate
		rect.set(left, top, right, bottom); // assign size to rect
		rectBg.set(bgLeft, bgTop, bgRight, bgBottom);
	}

	/**
	 * 
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		/*
		 * 画外圆
		 */
		canvas.drawCircle(cx, cy, outerRadius, circleColor);
		/*
		 * 画圆弧
		 */
		canvas.drawArc(rect, startAngle, angle, true, circleRing);

		if (isRingMode) {
			/*
			 * 画内圆
			 */
			canvas.drawCircle(cx, cy, innerRadius, innerColor);

			/*
			 * 画seek点
			 */
			canvas.drawBitmap(progressBg, null, rectBg, paint);
		}
		/*
		 * 判断是否显示seekBar圆点。
		 */
		if (showProgressBar) {
			/*
			 * 获取seek点X坐标
			 */
			dx = getXFromAngle();
			/*
			 * 获取seek点Y坐标
			 */
			dy = getYFromAngle();
			/*
			 * 绘制seek点的位置
			 */
			drawMarkerAtProgress(canvas);
		}

		super.onDraw(canvas);
	}

	/**
	 * 画progressMark(seek)在圆环上的位置.
	 * 
	 * @param canvas
	 */
	public void drawMarkerAtProgress(Canvas canvas) {
		if (IS_PRESSED) {
			canvas.drawBitmap(progressMarkPressed, dx, dy, null);
		} else {
			canvas.drawBitmap(progressMark, dx, dy, null);
		}
	}

	/**
	 * 获取seek点X坐标
	 * 
	 * @return
	 */
	public float getXFromAngle() {
		int size1 = progressMark.getWidth();
		int size2 = progressMarkPressed.getWidth();
		int adjust = (size1 > size2) ? size1 : size2;
		float x = markPointX - (adjust / 2);
		return x;
	}

	/**
	 * 获取seek点Y坐标
	 * 
	 * @return
	 */
	public float getYFromAngle() {
		int size1 = progressMark.getHeight();
		int size2 = progressMarkPressed.getHeight();
		int adjust = (size1 > size2) ? size1 : size2;
		float y = markPointY - (adjust / 2);
		return y;
	}

	/**
	 * 获取圆弧角度
	 * 
	 * @return the angle
	 */
	public int getAngle() {
		return angle;
	}

	/**
	 * 设置圆弧的角度
	 * 
	 * @param angle
	 */
	public void setAngle(int angle) {
		this.angle = angle;
		float donePercent = (((float) this.angle) / 360) * 100;
		float progress = (donePercent / 100) * getMaxProgress();
		setProgressPercent(Math.round(donePercent));
		if (IS_PRESSED) {
			setProgress(Math.round(progress));
		}
	}

	/**
	 * 设置seekbar变化的监听
	 * 
	 * @param listener
	 */
	public void setSeekBarChangeListener(OnSeekChangeListener listener) {
		mListener = listener;
	}

	/**
	 * 获取seekbar变化的监听
	 * 
	 * @return the seek bar change listener
	 */
	public OnSeekChangeListener getSeekBarChangeListener() {
		return mListener;
	}

	/**
	 * 获得bar宽度
	 * 
	 * @return the bar width
	 */
	public int getBarWidth() {
		return barWidth;
	}

	/**
	 * 设置bar宽度
	 * 
	 * @param barWidth
	 */
	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	/**
	 * 定义seekbar变化监听接口
	 * 
	 * @see OnSeekChangeEvent
	 */
	public interface OnSeekChangeListener {
		/**
		 * On progress change.
		 * 
		 * @param view
		 * @param newProgress
		 */
		public void onProgressChange(MCircleSeekBar view, int newProgress);
	}

	/**
	 * Gets the max progress.
	 * 
	 * @return the max progress
	 */
	public int getMaxProgress() {
		return maxProgress;
	}

	/**
	 * 
	 * @param maxProgress
	 * 
	 */
	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	/**
	 * 
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * 
	 * @param progress
	 * 
	 */
	public void setProgress(int progress) {
		this.progress = progress;
		if (!IS_PRESSED) {
			int newPercent = (this.progress * 100) / this.maxProgress;
			int newAngle = (newPercent * 360) / 100;
			this.setAngle(newAngle);
			this.setProgressPercent(newPercent);
			this.angle = newAngle;
		}
		invalidate();
		mListener.onProgressChange(this, this.getProgress());
	}

	/**
	 * 
	 * @return
	 */
	public int getProgressPercent() {
		return progressPercent;
	}

	/**
	 * 
	 * @param progressPercent
	 */
	public void setProgressPercent(int progressPercent) {
		this.progressPercent = progressPercent;
	}

	/**
	 * 
	 * @param color
	 */
	public void setRingBackgroundColor(int color) {
		circleRing.setColor(color);
	}

	/**
	 * 
	 * @param color
	 */
	public void setBackGroundColor(int color) {
		innerColor.setColor(color);
	}

	/**
	 * 
	 * @param color
	 */
	public void setProgressColor(int color) {
		circleRing.setColor(color);
	}

	/**
	 * 重写onTouch方法
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (canMove) {// 是否可以手动move
			float x = event.getX();
			float y = event.getY();
			boolean up = false;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				moved(x, y, up);
				break;
			case MotionEvent.ACTION_MOVE:
				moved(x, y, up);
				break;
			case MotionEvent.ACTION_UP:
				up = true;
				moved(x, y, up);
				break;
			}
		}
		return true;
	}

	/**
	 * seek点移动方法
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param up
	 *            the up
	 */
	private void moved(float x, float y, boolean up) {
		float distance = (float) Math.sqrt(Math.pow((x - cx), 2)
				+ Math.pow((y - cy), 2));
		if (distance < outerRadius + adjustmentFactor
				&& distance > innerRadius - adjustmentFactor && !up) {
			IS_PRESSED = true;

			/*
			 * 根据三角函数整切定理计算得到X.Y的坐标
			 */
			markPointX = (float) (cx + outerRadius
					* Math.cos(Math.atan2(x - cx, cy - y) - (Math.PI / 2)));
			markPointY = (float) (cy + outerRadius
					* Math.sin(Math.atan2(x - cx, cy - y) - (Math.PI / 2)));
			float degrees = (float) ((float) ((Math.toDegrees(Math.atan2(
					x - cx, cy - y)) + 360.0)) % 360.0);
			if (degrees < 0) {
				degrees += 2 * Math.PI;
			}
			setAngle(Math.round(degrees));
			invalidate();
		} else {
			IS_PRESSED = false;
			invalidate();
		}
	}

	public float getXByProgress(int progress) {
		float x = 0;
		float angle = (float) (2 * progress * Math.PI / 100);
		x = (float) (cx + outerRadius * Math.cos(angle - Math.PI / 2));
		return x;
	}

	public float getYByProgress(int progress) {
		float y = 0;
		float angle = (float) (2 * progress * Math.PI / 100);
		y = (float) (cy + outerRadius * Math.sin(angle - Math.PI / 2));
		return y;
	}

	public void setMarkPointXY(int progress) {
		this.progress = progress;
	}

	/**
	 * 
	 * @return
	 */
	public float getAdjustmentFactor() {
		return adjustmentFactor;
	}

	/**
	 * 
	 * @param adjustmentFactor
	 */
	public void setAdjustmentFactor(float adjustmentFactor) {
		this.adjustmentFactor = adjustmentFactor;
	}

	public boolean isShowProgressBar() {
		return showProgressBar;
	}

	/**
	 * 是否显示ProgressBar
	 * 
	 * @param showProgressBar
	 */
	public void setShowProgressBar(boolean showProgressBar) {
		this.showProgressBar = showProgressBar;
	}

	public boolean isRingMode() {
		return isRingMode;
	}

	/**
	 * 设置是否圆环模式或圆模式
	 * 
	 * @param showInCircle
	 */
	public void setRingMode(boolean isRingMode) {
		this.isRingMode = isRingMode;

	}

	public boolean isCanMove() {
		return canMove;
	}

	/**
	 * 是否可以通过手指移动
	 * 
	 * @param canMove
	 */
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

}
