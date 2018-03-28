package com.ljs.customview.view;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.view.View.OnTouchListener;

/**
 *
 * 
 * @author ljs
 * 
 */
public class PhotoZoomView extends ImageView implements OnGlobalLayoutListener,
		OnScaleGestureListener, OnTouchListener {

	public PhotoZoomView(Context context) {
		this(context, null);
	}

	public PhotoZoomView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PhotoZoomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private boolean monce = false;

	private float mInitScale;

	private float mMidScale;

	private float mMaxScale;

	private Matrix matrix;

	private ScaleGestureDetector mScaleGestureDetector;

	private void init(Context context) {
		matrix = new Matrix();
		setScaleType(ScaleType.MATRIX);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		setOnTouchListener(this);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeOnGlobalLayoutListener(this);
	}

	/**
	 * ��ȡImageviewͼƬ�Ĵ�С
	 */
	@Override
	public void onGlobalLayout() {
		if (!monce) {

			int width = getWidth();
			int height = getHeight();

			Drawable d = getDrawable();

			if (d == null) {
				return;

			}
			float dw = d.getIntrinsicWidth();
			float dh = d.getIntrinsicHeight();

			float scale = 1.0f;

			if (dw > width && dh < height) {
				scale = width * 1.0f / dw;
			} else if (dh > height && dw < width) {
				scale = height * 1.0f / dh;
			} else if ((dw > width && dh > height)
					|| (dw < width && dh < height)) {
				scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
			}
			mInitScale = scale;
			mMidScale = scale * 2;
			mMaxScale = scale * 4;

			float dx = getWidth() / 2 - dw / 2;
			float dy = getHeight() / 2 - dh / 2;

			matrix.postTranslate(dx, dy);
			matrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
			setImageMatrix(matrix);

			monce = true;
		}
	}

	private float getScale() {
		float[] values = new float[9];
		matrix.getValues(values);
		return values[Matrix.MSCALE_X];
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {

		float scale = getScale();

		float scaleFactor = detector.getScaleFactor();

		if (getDrawable() == null)
			return true;

		if ((scale < mMaxScale && scaleFactor > 1.0f)
				|| (scale > mInitScale && scaleFactor < 1.0f)) {
			if (scale * scaleFactor < mInitScale) {
				scaleFactor = mInitScale / scale;
			}
			if (scale * scaleFactor > mMaxScale) {
				scale = mMaxScale / scale;
			}

			matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(),
					detector.getFocusY());
			checkBorderAndCenterWhenScale();
			setImageMatrix(matrix);
		}

		return true;
	}

	private RectF getMatrixRectF() {
		Matrix matrix = this.matrix;
		RectF rectF = new RectF();

		Drawable d = getDrawable();
		if (d != null) {
			rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(rectF);
		}

		return rectF;
	}

	private void checkBorderAndCenterWhenScale() {
		RectF rectF = getMatrixRectF();

		float deltaX = 0;
		float deltaY = 0;

		float width = getWidth();
		float height = getHeight();

		if (rectF.width() >= width) {
			if (rectF.left > 0) {
				deltaX = -rectF.left;
			}
			if (rectF.right < width) {
				deltaX = width - rectF.right;
			}
		}
		if(rectF.height()>=height){
			if (rectF.top>0) {
			deltaY = -rectF.top;	
			}
			if (rectF.bottom<height) {
				deltaY = height - rectF.bottom;
			}
			
		}
		
		
		if (rectF.width()< width) {
			deltaX = width/2 -rectF.right+rectF.width()/2;
		}
		if (rectF.height()<height) {
			deltaY = height/2-rectF.bottom+rectF.height()/2;
		}
		
		matrix.postTranslate(deltaX, deltaY);
		
		
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mScaleGestureDetector.onTouchEvent(event);
		return true;
	}

}
