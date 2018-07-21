package com.ljs.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ljs.customview.R;


/**
 * @author lujiasheng QQ:1060545627
 * @name weixiaobao
 * @class name：com.longma.weixiaobao.widget
 * @class 页面首次加载控件
 * @time 2018/7/20 15:58
 * @change
 * @chang time
 * @class describe
 */

public class FirstLoadingView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Paint loadPaint;
    private Paint textPaint;
    private int mark;
    private int eachAngle;
    private float radius;
    private RectF mRectF;
    private float strokWidth;
    private float corner;
    private int eachAlpha;
    private int value;
    private long duration;
    private boolean loading;
    private ValueAnimator loadAnimator;
    private Handler handler;
    private String message;
    private int textHeigth;

    public FirstLoadingView(Context context) {
        this(context, null);
    }

    public FirstLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FirstLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaint();
        initAnimatorListenter();
        initHandler();
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        mark = 12;
        eachAngle = 360 / mark;
        corner = 30;
        strokWidth = 10;
        eachAlpha = 255 / mark;
        duration = 1000;
        message = "努力加载中";
        textHeigth = 30;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.loading_background));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        loadPaint = new Paint();
        loadPaint.setColor(getResources().getColor(R.color.white));
        loadPaint.setAntiAlias(true);
        loadPaint.setStrokeWidth(strokWidth);
        loadPaint.setStrokeCap(Paint.Cap.ROUND);
        loadPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.white));
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(42);
    }

    private void initAnimatorListenter() {
        loadAnimator = ObjectAnimator.ofInt(0, mark).setDuration(duration);
        loadAnimator.setInterpolator(new LinearInterpolator());
        loadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        loadAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (loading) {
                    handler.sendEmptyMessage(0x10);
                }
            }
        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loadAnimator.start();
            }
        };
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        radius = mWidth / 5;
        mRectF = new RectF(0, 0, mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(mRectF, corner, corner, mPaint);
        if (!TextUtils.isEmpty(message)) {
            Rect rect = new Rect();
            textPaint.getTextBounds(message, 0, message.length(), rect);
            canvas.drawText(message, mWidth / 2 - rect.width() / 2, mHeight - rect.height()/2-radius/3, textPaint);
        }
        canvas.translate(mWidth / 2, mHeight / 2-radius/3);
        canvas.scale(1, -1);

        for (int i = 0; i < mark; i++) {
            loadPaint.setAlpha(255 - eachAlpha * (Math.abs(i + value) % mark));
            canvas.drawLine(0, radius / 3*2, 0, radius, loadPaint);
            canvas.rotate(eachAngle, 0, 0);
        }
        canvas.restore();
    }

    public void startLoading() {
        if (loading) {
            return;
        }
        loading = true;
        loadAnimator.start();
    }

    public void stopLoading() {
        loading = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (loadAnimator != null) {
            loadAnimator.cancel();
            loadAnimator.removeAllListeners();
        }
    }
}
