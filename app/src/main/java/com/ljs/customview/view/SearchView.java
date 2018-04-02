package com.ljs.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ljs.customview.R;

/**
 * 作者：Administrator create on 2018/3/28
 * <p/>
 * 内容：搜索控件
 */
public class SearchView extends View {
    private static final int NONE = 0;
    private static final int STARTING = 1;
    private static final int SERCHING = 2;
    private static final int END = 3;
    //画笔
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private Path circle_path;
    private Path search_path;
    private int search_color;
    private Animator.AnimatorListener animatorListener;
    private ValueAnimator.AnimatorUpdateListener updateListener;
    private float animatedValue;
    private int state = NONE;
    private boolean isOverSearch;
    private int duration = 2000;
    private ValueAnimator startAnimator;
    private ValueAnimator searchAnimator;
    private ValueAnimator endAnimator;
    private Handler handler;
    private float circle_radius;
    private float magnifier_width;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
        search_color = array.getColor(R.styleable.SearchView_search_color, Color.BLUE);
        circle_radius = array.getDimension(R.styleable.SearchView_circle_radius, 100);
        magnifier_width = array.getDimension(R.styleable.SearchView_magnifier_width, 10);
        array.recycle();
        init();
    }

    private void init() {
        initPaint();
        initPath();
        initListener();
        initHandler();
        initAnimator();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(search_color);
        mPaint.setStrokeWidth(magnifier_width);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    private void initPath() {
        search_path = new Path();
        circle_path = new Path();

        mPathMeasure = new PathMeasure();

        RectF rect = new RectF(-circle_radius/2, -circle_radius/2, circle_radius/2, circle_radius/2);
        search_path.addArc(rect, 45f, 359.9f);

        RectF rect1 = new RectF(-circle_radius, -circle_radius, circle_radius, circle_radius);
        circle_path.addArc(rect1, 45f, 359.9f);

        float[] pos = new float[2];

        mPathMeasure.setPath(circle_path, false);
        mPathMeasure.getPosTan(0, pos, null);

        search_path.lineTo(pos[0], pos[1]);


    }

    private void initListener() {
        updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };
        animatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                handler.sendEmptyMessage(0x10);
            }
        };
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (state) {

                    case STARTING:
                        state = SERCHING;
                        isOverSearch = false;
                        searchAnimator.start();
                        break;
                    case SERCHING:
                        if (isOverSearch) {
                            state = END;
                            isOverSearch = false;
                            endAnimator.start();
                        } else {
                            searchAnimator.start();
                        }
                        break;
                    case END:
                        state = NONE;
                        break;
                }
            }
        };
    }

    private void initAnimator() {
        startAnimator = ValueAnimator.ofFloat(0, 1).setDuration(duration);
        searchAnimator = ValueAnimator.ofFloat(0, 1).setDuration(duration);
        endAnimator = ValueAnimator.ofFloat(1, 0).setDuration(duration);

        startAnimator.addUpdateListener(updateListener);
        searchAnimator.addUpdateListener(updateListener);
        endAnimator.addUpdateListener(updateListener);

        startAnimator.addListener(animatorListener);
        searchAnimator.addListener(animatorListener);
        endAnimator.addListener(animatorListener);
    }

    public void startSearch() {
        state = STARTING;
        startAnimator.start();
    }

    public void endSearch() {
        isOverSearch = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        switch (state) {
            case NONE:
                canvas.drawPath(search_path, mPaint);
                break;
            case STARTING:
                mPathMeasure.setPath(search_path, false);
                Path dst = new Path();
                mPathMeasure.getSegment(animatedValue * mPathMeasure.getLength(), mPathMeasure.getLength(), dst, true);
                canvas.drawPath(dst, mPaint);
                break;
            case SERCHING:
                mPathMeasure.setPath(circle_path, false);
                Path dst2 = new Path();
                float stop = animatedValue * mPathMeasure.getLength();
                float start = (float) (stop - ((0.5 - Math.abs(animatedValue - 0.5)) * 2*circle_radius));
                mPathMeasure.getSegment(start, stop, dst2, true);
                canvas.drawPath(dst2, mPaint);
                break;
            case END:
                mPathMeasure.setPath(search_path, false);
                Path dst1 = new Path();
                mPathMeasure.getSegment(animatedValue * mPathMeasure.getLength(), mPathMeasure.getLength(), dst1, true);
                canvas.drawPath(dst1, mPaint);
                break;
        }
    }

}