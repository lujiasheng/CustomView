package com.ljs.customview.view;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
/**
 * 自由缩放移动图片控件
 * @author ljs
 *
 */
public class PhotoZoomView extends ImageView implements OnGlobalLayoutListener{

	public PhotoZoomView(Context context) {
		this(context, null);
	}

	public PhotoZoomView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PhotoZoomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private boolean monce = false;
	
	private float mInitScale ;
	
	private float mMidScale;
	
	private float mMaxScale;
	
	private Matrix matrix;
	
	
	private void init() {
		matrix = new Matrix();
	}
	
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}
	
	@SuppressLint("NewApi") @Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeOnGlobalLayoutListener(this);
	}

	/**
	 * 获取Imageview图片的大小
	 */
	@Override
	public void onGlobalLayout() {
		if(!monce){
			
			int width = getWidth();
			int height = getHeight();
			
			Drawable d = getDrawable();
			
			if (d==null) {
				return;
				
			}
			int dw = d.getIntrinsicWidth();
			int dh = d.getIntrinsicHeight();
			
			float scale = 1.0f;
			
			
			if(dw>width&&dh<height){
				scale = width*1.0f/dw;
			}else if(dh>height&&dw<width){
				scale  = height*1.0f/dh;
			}else if((dw>width&&dh>height) ||(dw<width&&dh<height)){
				scale = Math.min(width*1.0f/dw, height*1.0f/dh);
			}
			mInitScale = scale;
			mMidScale = scale * 2;
			mMaxScale = scale *4;
			
			
			int dx  = getWidth()/2 -dw/2;
			int dy = getHeight()/2 - dh/2;
			
			matrix.postTranslate(dx, dy);
			matrix.postScale(mInitScale, mInitScale, width/2, height/2);
			setImageMatrix(matrix);
			
			monce = true;
		}
	}
	
}
