package com.ljs.customview.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 作者：Administrator create on 2018/3/28
 * <p/>
 * 内容：
 */
public class BorderScaleImageView extends ImageView {

    private float defalutScale = 1f;
    private int mParentWidth;
    //记录父布局的左右距离
    private int mLeft, mRight;

    private static final int SLIDE_LEFT = 0;
    private static final int SLIDE_RIGTH = 1;
    //滑动方向
    private int slideState = 0;

    //手指最后的横向和纵向坐标
    private int lastX;
    private int mWidth;

    //放大倍数
    private float scale = 1.3f;

    //滑动时间等份
    private int seconds = 3;

    private boolean isFirst = true;
    //缩放的时间
    private int scaleTime;
    private ValueAnimator valueAnimator;
    private int mHeight;
    private int mTop;

    public BorderScaleImageView(Context context) {
        this(context, null);
    }

    public BorderScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        valueAnimator = ObjectAnimator.ofFloat(defalutScale, 1.3f);
        valueAnimator.setDuration(scaleTime);
        valueAnimator.setRepeatCount(1);
        valueAnimator.setRepeatMode(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(scaleListener);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {
            mWidth = getWidth();
            mHeight = getHeight();
            mLeft = getLeft();
            mTop = getTop();
            mRight = mParentWidth - mLeft;
            isFirst = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取父布局的宽度
        ViewGroup group = (ViewGroup) getParent();
        if (null != group) {
            mParentWidth = group.getWidth();

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getLeft() < mParentWidth / 2) {
            slideState = SLIDE_RIGTH;
        } else if (getRight() > mParentWidth / 2) {
            slideState = SLIDE_LEFT;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
//                Log.d("BorderScaleImageView", "getLeft():" + getLeft());
//                Log.d("BorderScaleImageView", "getRight():" + getRight());
//                Log.d("BorderScaleImageView", "mParentWidth:" + mParentWidth);
//                Log.d("BorderScaleImageView", "mLeft:" + mLeft);
//                Log.d("BorderScaleImageView", "mRight:" + mRight);
                if (getRight() + offsetX <= (mParentWidth - mLeft) && getLeft() + offsetX >= mLeft && (getLeft() >= mLeft && getRight() <= mRight)) {
                    offsetLeftAndRight(offsetX);
                }
                break;
            case MotionEvent.ACTION_UP:

                if (getLeft() <= (mParentWidth - mWidth) / 2) {
                    ObjectAnimator translationX = ObjectAnimator.ofFloat(this, "translationX", mLeft - getLeft());
                    translationX.addListener(TranslationListener);
                    translationX.setDuration(Math.abs(getLeft() - mLeft) * seconds).start();

                } else {
                    ObjectAnimator translationX = ObjectAnimator.ofFloat(this, "translationX", mRight - getRight());
                    translationX.addListener(TranslationListener);
                    translationX.setDuration(Math.abs(mRight - getRight()) * seconds).start();
                }
//
                break;
        }


        return true;
    }

    private AnimatorListener TranslationListener = new AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
//            if (getLeft() <= (mParentWidth - mWidth) / 2) {
//                setLeft(mLeft);
//            } else {
//                setLeft(mParentWidth - mWidth - mLeft);
//            }
            valueAnimator.start();
            Log.d("BorderScaleImageView", "getLeft():" + getLeft());
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
    private ValueAnimator.AnimatorUpdateListener scaleListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            defalutScale = (Float) animation.getAnimatedValue();
            postInvalidate();
        }
    };
}