package com.ljs.customview.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * 作者：Administrator create on 2018/3/28
 * <p>
 * 内容：
 */
public class BorderScaleImageView extends ImageView implements Animator.AnimatorListener {

    private float defalutScale = 1f;
    private int mParentWidth;
    //记录父布局的左右距离
    private int mLeft, mRight;


    //手指最后的横向和纵向坐标
    private int lastX;
    private int mWidth;

    //放大倍数
    private float scale = 1.1f;

    //滑动时间等份
    private int seconds = 3;

    private boolean isFirst = true;
    //缩放的时间
    private int scaleTime = 1000;
    private Scroller mScroller;
    private int screenWidth = 1080;

    public BorderScaleImageView(Context context) {
        this(context, null);
    }

    public BorderScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {
            mWidth = getWidth();
            mLeft = getLeft();
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
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                break;
            case MotionEvent.ACTION_MOVE: {
                int offsetX = x - lastX;
                if (offsetX > 0 && event.getRawX() - event.getX() + mWidth + Math.abs(offsetX) <= screenWidth - mLeft)
                    ((View) getParent()).scrollBy(-offsetX, 0);
            }
            break;
            case MotionEvent.ACTION_UP: {
                if (event.getRawX() - event.getX() + mWidth/2 >= screenWidth /2) {
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(
                            ObjectAnimator.ofFloat(this, "scaleX", 1f, scale),
                            ObjectAnimator.ofFloat(this, "scaleY", 1f, scale)
                    );
                    set.setDuration(scaleTime);
                    set.setTarget(this);
                    set.addListener(this);
                    set.start();

                } else {
                    View viewGroup = (View) getParent();
                    mScroller.startScroll(viewGroup.getScrollX(), viewGroup.getScrollY(),
                            -viewGroup.getScrollX(), -viewGroup.getScrollY(), viewGroup.getScrollX() * seconds);
                    invalidate();
                }
            }
            break;
        }


        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private ValueAnimator.AnimatorUpdateListener scaleListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

        }
    };


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (listener != null) {
            listener.scaleComplete();
//            ViewGroup.LayoutParams layoutParams = getLayoutParams();
//            layoutParams.width = screenWidth;
//            layoutParams.height = 1920;
//
//            requestLayout();
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {

                setScaleX(defalutScale);
                setScaleY(defalutScale);
                View viewGroup = (View) getParent();
                mScroller.startScroll(viewGroup.getScrollX(), viewGroup.getScrollY(),
                        -viewGroup.getScrollX(), -viewGroup.getScrollY(), viewGroup.getScrollX() * seconds);
                invalidate();
            }
        }, 1000);

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public interface ScaleCompleteListener {
        void scaleComplete();
    }

    private ScaleCompleteListener listener;

    public void setScaleCompleteListener(ScaleCompleteListener listener) {
        this.listener = listener;
    }
}