package com.ljs.customview.view;

import com.ljs.customview.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

public class RouteRoundView extends View {

	public RouteRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		pos = new float[2];
		tan = new float[2];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize=2;
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher, options);
		matrix = new Matrix();
		mpaint = new Paint();
		mpaint.setStrokeWidth(1);
		mpaint.setStyle(Style.STROKE);
		mpaint.setColor(Color.BLACK);
		mpaint.setAntiAlias(true);
	}

	public RouteRoundView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	private float[] pos;
	private float[] tan;
	private float currentValue = 0;
	private Paint mpaint;
	private Matrix matrix;
	private Bitmap bitmap;
	private float centerX;
	private float centerY;

	public RouteRoundView(Context context) {
		this(context,null);
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		centerX = w/2;
		centerY = h/2;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.translate(centerX, centerY);
		canvas.scale(1, -1);
		
		Path path = new Path();
		path.addCircle(0, 0, 200, Direction.CCW);
		
		PathMeasure measure = new PathMeasure(path,false);
		 currentValue+=0.005;
		 if(currentValue>=1){
			 currentValue = 0;
		 }
		 
		measure.getPosTan(measure.getLength()*currentValue, pos, tan);
		matrix.reset();
		float degree = (float) (Math.atan2(tan[1], tan[0])*180.0/Math.PI);
		
		matrix.postRotate(degree, bitmap.getWidth()/2, bitmap.getHeight()/2);
		matrix.postTranslate(pos[0]-bitmap.getWidth()/2, pos[1]-bitmap.getHeight()/2);
		
		canvas.drawPath(path, mpaint);
		canvas.drawBitmap(bitmap, matrix, mpaint);
		
		invalidate();
		
		
	}
	
}
