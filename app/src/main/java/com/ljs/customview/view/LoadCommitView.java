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
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.ljs.customview.R;

/**
 * Created by Administrator on 2018/6/14.
 * 提交与进度
 */

public class LoadCommitView extends View {


    public LoadCommitView(Context context) {
        this(context, null);
    }

    public LoadCommitView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadCommitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //文字画笔
    private Paint textPaint;
    //按钮画笔
    private Paint mPaint;
    //加载进度画笔
    private Paint progressPaint;
    //成功画笔
    private Paint successPaint;
    //失败画笔
    private Paint failPaint;
    //普通状态
    private static final int NORMAL = 0;
    //点击状态
    private static final int PRESS = 1;
    //开始状态
    private static final int START = 2;
    //加载状态
    private static final int LOADING = 3;
    //成功状态
    private static final int SUCCESS = 4;
    //失败状态
    private static final int FAIL = 5;
    //重置状态
    private static final int RESET = 6;
    //状态颜色
    private int statusColor;
    //文字
    private String text;
    //文字大小
    private float textSize;
    //文字颜色
    private int textColor;
    //圆的半径
    private float radius;

    private Path buttonPath;

    private Path progressPath;

    private Path successPath;

    private Path failPath;

    private Path shrinkPath;

    private int mWidth;

    private int mHeight;

    private PathMeasure pathMeasure;
    /**
     * 动画状态
     */
    private int status = 0;

    private ValueAnimator startAnimator;
    private ValueAnimator loadAnimator;
    private ValueAnimator successAnimator;
    private ValueAnimator failAnimator;
    private ValueAnimator resetAnimator;
    private AnimatorListenerAdapter animatorListener;
    private ValueAnimator.AnimatorUpdateListener updateListener;
    private float animatedValue;
    private Handler handler;

    /**
     * 动画时间
     */
    private int duration;

    private float min;
    /**
     * 等待进度条的宽度
     */
    private float progressWidth;
    /**
     * 成功的宽度
     */
    private float successWidth;
    /**
     * 失败的宽度
     */
    private float failWidth;
    /**
     * 等待进度条的颜色
     */
    private int progressColor;
    /**
     * 成功的颜色
     */
    private int successColor;
    /**
     * 失败的颜色
     */
    private int failColor;
    /**
     * 是否开始
     */
    private boolean isStart;
    /**
     * 停止加载
     */
    private boolean stopLoading;
    /**
     * 是否失败
     */
    private boolean isFail;

    /**
     * 是否显示成功或者失败的背景
     */
    private boolean show_finish_bg;

    /**
     * 初始化画笔和属性
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        textPaint = new Paint();
        mPaint = new Paint();
        progressPaint = new Paint();
        successPaint = new Paint();
        failPaint = new Paint();

        pathMeasure = new PathMeasure();

        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPaint.setAntiAlias(true);
        mPaint.setColor(statusColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);

        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStyle(Paint.Style.STROKE);

        successPaint.setAntiAlias(true);
        successPaint.setColor(successColor);
        successPaint.setStrokeWidth(successWidth);
        successPaint.setStrokeCap(Paint.Cap.ROUND);
        successPaint.setStyle(Paint.Style.STROKE);

        failPaint.setAntiAlias(true);
        failPaint.setColor(failColor);
        failPaint.setStrokeWidth(failWidth);
        failPaint.setStrokeCap(Paint.Cap.ROUND);
        failPaint.setStyle(Paint.Style.STROKE);

        buttonPath = new Path();
        progressPath = new Path();
        successPath = new Path();
        failPath = new Path();
        shrinkPath = new Path();


    }

    /**
     * 初始化属性值
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadCommitView);
        text = typedArray.getString(R.styleable.LoadCommitView_text);
        if (TextUtils.isEmpty(text)) {
            text = "确定";
        }
        textSize = typedArray.getDimension(R.styleable.LoadCommitView_textSize, 20);
        textColor = typedArray.getColor(R.styleable.LoadCommitView_textColor, Color.WHITE);
        statusColor = typedArray.getColor(R.styleable.LoadCommitView_statusColor, context.getResources().getColor(R.color.colorPrimary));
        progressColor = typedArray.getColor(R.styleable.LoadCommitView_progressColor, Color.WHITE);
        successColor = typedArray.getColor(R.styleable.LoadCommitView_successColor, Color.GREEN);
        failColor = typedArray.getColor(R.styleable.LoadCommitView_failColor, Color.RED);
        duration = typedArray.getInt(R.styleable.LoadCommitView_duration, 2000);
        progressWidth = typedArray.getDimension(R.styleable.LoadCommitView_progressWidth, 10);
        successWidth = typedArray.getDimension(R.styleable.LoadCommitView_successWidth, 10);
        failWidth = typedArray.getDimension(R.styleable.LoadCommitView_failWidth, 10);
        show_finish_bg = typedArray.getBoolean(R.styleable.LoadCommitView_show_finish_bg, false);
        typedArray.recycle();
    }

    /**
     * 初始化路径
     */
    private void initPath() {

        RectF rectF = new RectF(-mWidth / 2, -mHeight / 2, mWidth / 2, mHeight / 2);
        buttonPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);

        RectF arc = new RectF(-radius / 4 * 3, -radius / 4 * 3, radius / 4 * 3, radius / 4 * 3);
        progressPath.addArc(arc, 90f, 359.9f);

        successPath.addPath(progressPath);
        successPath.moveTo(-radius / 3,0);
        successPath.lineTo(0, -radius / 4);
        successPath.lineTo(radius / 3, radius / 3);

        failPath.addPath(progressPath);
        failPath.moveTo(-radius / 3, radius / 3);
        failPath.lineTo(radius / 3, -radius / 3);
        failPath.moveTo(radius / 3, radius / 3);
        failPath.lineTo(-radius / 3, -radius / 3);
    }

    /**
     * 文字绘制
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        canvas.scale(1, -1);
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, -rect.width() / 2, rect.height() / 2, textPaint);
        canvas.scale(1, -1);
    }

    /**
     * 初始化动画
     */
    private void initAnimator() {
        startAnimator = ValueAnimator.ofFloat(1, min).setDuration(duration);
        loadAnimator = ValueAnimator.ofFloat(1, 0).setDuration(duration);
        successAnimator = ValueAnimator.ofFloat(0, 1).setDuration(duration);
        failAnimator = ValueAnimator.ofFloat(0, 1).setDuration(duration);
        resetAnimator = ValueAnimator.ofFloat(min, 1).setDuration(duration);

        startAnimator.addUpdateListener(updateListener);
        loadAnimator.addUpdateListener(updateListener);
        successAnimator.addUpdateListener(updateListener);
        failAnimator.addUpdateListener(updateListener);
        resetAnimator.addUpdateListener(updateListener);

        startAnimator.addListener(animatorListener);
        loadAnimator.addListener(animatorListener);
        successAnimator.addListener(animatorListener);
        failAnimator.addListener(animatorListener);
        resetAnimator.addListener(animatorListener);

    }

    /**
     * 初始化动画监听
     */
    private void initAnimatorListener() {
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

    /**
     * 初始化监听事件更新UI
     */
    private void initHandler() {
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (status) {
                    case START:
                        shrinkPath.reset();
                        RectF rectF = new RectF(-radius, -mHeight / 2, radius, mHeight / 2);
                        shrinkPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);
                        status = LOADING;
                        loadAnimator.start();
                        break;
                    case LOADING:
                        if (stopLoading) {
                            stopLoading = false;
                            if (isFail) {
                                status = FAIL;
                                failAnimator.start();
                            } else {
                                status = SUCCESS;
                                successAnimator.start();
                            }
                        } else {
                            loadAnimator.start();
                        }
                        break;
                    case SUCCESS:
                    case FAIL:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                status = RESET;
                                resetAnimator.start();
                            }
                        }, 1000);

                        break;
                    case RESET:
                        isStart = false;
                        status = NORMAL;
                        invalidate();
                        break;
                }

            }
        };
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        radius = mHeight / 2;
        min = radius * 2 / mWidth;
        initPath();
        initHandler();
        initAnimatorListener();
        initAnimator();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(1, -1);
        status(canvas);
    }

    /**
     * 各个状态绘制
     *
     * @param canvas
     */
    private void status(Canvas canvas) {
        switch (status) {
            case NORMAL:
                canvas.drawPath(buttonPath, mPaint);
                drawText(canvas);
                break;
            case START:
                shrink(canvas);
                drawText(canvas);
                break;
            case LOADING:
                if (show_finish_bg)
                    canvas.drawPath(shrinkPath, mPaint);
                Path dst = new Path();
                pathMeasure.setPath(progressPath, false);
                float stop = animatedValue * pathMeasure.getLength();
                float start = (float) (stop - ((0.5 - Math.abs(animatedValue - 0.5)) * 2 * radius));
                pathMeasure.getSegment(start, stop, dst, true);
                canvas.drawPath(dst, progressPaint);
                break;
            case SUCCESS:
                commit(canvas, successPath, successPaint);
                break;
            case FAIL:
                commit(canvas, failPath, failPaint);
                break;
            case RESET:
                shrink(canvas);
                drawText(canvas);
                break;
        }
    }

    /**
     * 提交结果绘制
     *
     * @param canvas
     * @param path
     * @param paint
     */
    private void commit(Canvas canvas, Path path, Paint paint) {
        if (show_finish_bg)
            canvas.drawPath(shrinkPath, mPaint);
        Path dst = new Path();
        pathMeasure.setPath(path, false);
        float length = 0f;
        while (pathMeasure.nextContour()) {
            length += pathMeasure.getLength();
            pathMeasure.getSegment(0, animatedValue * length, dst, true);
        }
        canvas.drawPath(dst, paint);
    }

    /**
     * 按钮收缩成圆形
     *
     * @param canvas
     */
    private void shrink(Canvas canvas) {
        shrinkPath.reset();
        RectF rectF = new RectF(-animatedValue * mWidth / 2, -mHeight / 2, animatedValue * mWidth / 2, mHeight / 2);
        shrinkPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);
        canvas.drawPath(shrinkPath, mPaint);
    }

    /**
     * 开始提交
     */
    public void start() {
        if (isStart) return;
        isStart = true;
        stopLoading = false;
        status = START;
        startAnimator.start();
    }

    /**
     * 成功
     */
    public void success() {
        if (stopLoading) return;
        stopLoading = true;
        isFail = false;
    }

    /**
     * 失败
     */
    public void fail() {
        if (stopLoading) return;
        stopLoading = true;
        isFail = true;
    }
}
