package com.ljs.customview.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator create on 2018/4/16
 * <p/>
 * 内容：圆形的波浪进度控件
 * 1.共四条波浪线
 * 2.由上往下，波浪线的颜色变深
 * 3.波浪线的高度是固定的
 * 4.底部有文字，颜色为白色
 * 5.顶部有文字颜色为黑色
 * 6.底部为白色
 */
public class WaveView extends View {

    private Paint textPaint;
    private Paint wavePaint1;
    private Paint wavePaint2;
    private Paint circlePaint;
    private Path circlePath;
    private Path wavePath1;
    private Path wavePath2;

    private int mWidth;
    private int mHeight;
    private float radius;
    private String oneColor = "#9555acef";
    private String twoColor = "#ac55acef";
    private String circleColor = "#ffffff";
    private int size;
    private List<PointF> mPoints1 = new ArrayList<>();
    private List<PointF> mPoints2 = new ArrayList<>();
    private List<PointF> mControlPoints1 = new ArrayList<>();
    private List<PointF> mControlPoints2 = new ArrayList<>();
    private float dx;
    private float dx2;
    private ValueAnimator waveAnimator;
    private ValueAnimator waveAnimator2;
    private ValueAnimator waveAnimator3;
    private float max = 100;
    private float progress = 0;
    private float progressHeight;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
        initPath();

    }

    private void initAnimator() {
        waveAnimator = ObjectAnimator.ofFloat(0, size);
        waveAnimator.setDuration(1000);
        waveAnimator.setRepeatMode(ValueAnimator.INFINITE);
        waveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        waveAnimator.setInterpolator(new LinearInterpolator());
        waveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        waveAnimator2 = ObjectAnimator.ofFloat(-radius / 2, radius * 3 / 2);
        waveAnimator2.setDuration(1000);
        waveAnimator2.setRepeatMode(ValueAnimator.INFINITE);
        waveAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        waveAnimator2.setInterpolator(new LinearInterpolator());
        waveAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx2 = (float) animation.getAnimatedValue();
                invalidate();
            }
        });


        waveAnimator3 = ObjectAnimator.ofFloat(0, size);
        waveAnimator3.setDuration(20000);
        waveAnimator3.setRepeatMode(ValueAnimator.INFINITE);
        waveAnimator3.setRepeatCount(ValueAnimator.INFINITE);
        waveAnimator3.setInterpolator(new LinearInterpolator());
        waveAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressHeight = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        waveAnimator3.start();
        waveAnimator.start();
        waveAnimator2.start();
    }


    private void initPath() {
        circlePath = new Path();
        wavePath1 = new Path();
        wavePath2 = new Path();

    }

    private void initPaint() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.parseColor(circleColor));
        circlePaint.setAntiAlias(true);

        wavePaint1 = new Paint();
        wavePaint1.setStyle(Paint.Style.FILL);
        wavePaint1.setColor(Color.parseColor(oneColor));
        wavePaint1.setAntiAlias(true);

        wavePaint2 = new Paint();
        wavePaint2.set(wavePaint1);
        wavePaint2.setColor(Color.parseColor(twoColor));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        size = Math.min(mWidth, mHeight);
        radius = size / 2;
        setMeasuredDimension(size, size);
        progressHeight = progress * progressHeight / max;
        circlePath.addCircle(radius, radius, radius, Path.Direction.CW);
        initAnimator();
    }

    private void initPoints() {
        mPoints1.clear();
        mControlPoints1.clear();

        mPoints1.add(new PointF(-radius * 2, progressHeight));
        mPoints1.add(new PointF(-radius, progressHeight));
        mPoints1.add(new PointF(0, progressHeight));
        mPoints1.add(new PointF(radius, progressHeight));
        mPoints1.add(new PointF(radius * 2, progressHeight));
        mControlPoints1.add(new PointF(-radius * 3 / 2, radius / 5 + progressHeight));
        mControlPoints1.add(new PointF(-radius / 2, -radius / 5 + progressHeight));
        mControlPoints1.add(new PointF(radius / 2, radius / 5 + progressHeight));
        mControlPoints1.add(new PointF(radius * 3 / 2, -radius / 5 + progressHeight));

        mPoints2.clear();
        mControlPoints2.clear();

        mPoints2.add(new PointF(-radius / 2, progressHeight));
        mPoints2.add(new PointF(radius / 2, progressHeight));
        mPoints2.add(new PointF(radius * 3 / 2, progressHeight));
        mPoints2.add(new PointF(radius * 5 / 2, progressHeight));
        mPoints2.add(new PointF(radius * 7 / 2, progressHeight));
        mControlPoints2.add(new PointF(0, -radius / 8 + progressHeight));
        mControlPoints2.add(new PointF(radius, radius / 8 + progressHeight));
        mControlPoints2.add(new PointF(radius * 2, -radius / 8 + progressHeight));
        mControlPoints2.add(new PointF(radius * 3, radius / 8 + progressHeight));
    }

    private void initWavePath() {
        wavePath1.reset();
        for (int i = 0; i < mPoints1.size(); i++) {
            if (i == 0) {
                wavePath1.moveTo(mPoints1.get(i).x, mPoints1.get(i).y);
            } else {
                wavePath1.quadTo(mControlPoints1.get(i - 1).x, mControlPoints1.get(i - 1).y, mPoints1.get(i).x, mPoints1.get(i).y);
            }
        }
        wavePath1.lineTo(mPoints1.get(mPoints2.size() - 1).x, 0);
        wavePath1.lineTo(mPoints1.get(0).x, 0);
        wavePath1.close();

        Matrix matrix = new Matrix();
        matrix.preTranslate(dx, 0);
        wavePath1.transform(matrix);

        wavePath2.reset();
        for (int i = 0; i < mPoints2.size(); i++) {
            if (i == 0) {
                wavePath2.moveTo(mPoints2.get(i).x, mPoints2.get(i).y);
            } else {
                wavePath2.quadTo(mControlPoints2.get(i - 1).x, mControlPoints2.get(i - 1).y, mPoints2.get(i).x, mPoints2.get(i).y);
            }
        }
        wavePath2.lineTo(mPoints2.get(mPoints2.size() - 1).x, 0);
        wavePath2.lineTo(mPoints2.get(0).x, 0);
        wavePath2.close();
        Matrix matrix1 = new Matrix();
        matrix1.preTranslate(-dx2, 0);
        wavePath2.transform(matrix1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(1, -1);
        canvas.translate(0, -2 * radius);
        canvas.clipPath(circlePath);
        canvas.drawPath(circlePath, circlePaint);
        if (progressHeight != 0) {
            initPoints();
            initWavePath();
            canvas.drawPath(wavePath1, wavePaint1);
            canvas.drawPath(wavePath2, wavePaint2);
            canvas.clipPath(wavePath1, Region.Op.INTERSECT);
            canvas.clipPath(wavePath2, Region.Op.INTERSECT);
        }
    }
}