package com.ljs.customview.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ljs.customview.R;
import com.ljs.customview.listener.Listener;


/**
 * Created by PC on 2018/3/28.
 */

public class DragCardView extends CardView {
    private static final String TAG = "信息";
    private int lastX;
    private int lastY;

    private int width;

    /**
     * 判断是否是首次触摸记录初始位置
     */
    private boolean isFrist = true;
    /**
     * 判断是否可以左滑
     */
    private boolean isScoll = false;
    /**
     * 记录初始left位置
     */
    int initLEFT;
    /**
     * 记录初始right位置
     */
    int initRIGHT;
    /**
     * 记录触摸点X轴位置
     */
    int initXX;
    int lastXX;
    private Animation animation;

    public DragCardView(Context context) {
        this(context,null);
    }

    public DragCardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        width = Public.getScreenWidthPixels(context);
        width = 1080;
        animation = AnimationUtils.loadAnimation(context, R.anim.scale_anim);
    }


    // 视图坐标方式
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (isFrist) {//记录原始位置
            initLEFT = getLeft();
            initRIGHT = getRight();
            initXX = (int) event.getX();
            isFrist = false;

        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸点坐标
                lastX = (int) event.getX();
                lastY = y;
                dragListener.onCallBack(MotionEvent.ACTION_DOWN, MotionEvent.ACTION_DOWN);
                break;
            case MotionEvent.ACTION_UP:
                int s = lastXX - initXX;//初始位置和最后停留位置的偏移量
                isScoll = false;
                Log.e("信息", (getRight() < (width * 0.65)) + "" + getRight() + "--" + (width * 0.65));
                if (getRight() < (width * 0.65)) {//左下角到父布局位置小于width * 0.65直接复位
                    layout(initLEFT,
                            getTop(),
                            initRIGHT,
                            getBottom());
                } else {
                    //手指离开后返回原来位置
                    postDelayed(new Runnable() {//延迟放大图片后进入播放页面 然后该控件复位
                        @Override
                        public void run() {
                            layout(initLEFT,
                                    getTop(),
                                    initRIGHT,
                                    getBottom());
                        }
                    }, 2000);
                    dragListener.onCallBack(MotionEvent.ACTION_UP, getRight());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量
                int offsetX = (int) (event.getX() - lastX);
                lastXX = (int) event.getX();
                if (offsetX > 0) {//左往右移动
                    Log.e("信息", width - 60 + "---->>>>" + getRight());
                    if (getRight() < width - 60) {
                        layout(getLeft() + offsetX,
                                getTop(),
                                getRight() + offsetX,
                                getBottom());
                    }
                    isScoll = true;
                    dragListener.onCallBack(MotionEvent.ACTION_MOVE, MotionEvent.ACTION_MOVE);
                } else {
                    if (isScoll) {
                        if (getLeft() > initLEFT)
                            layout(getLeft() + offsetX,
                                    getTop(),
                                    getRight() + offsetX,
                                    getBottom());
                    }
                }
                break;
        }
        return true;
    }

    private Listener<Integer, Integer> dragListener;

    public void setOnDragViewListener(Listener<Integer, Integer> dragListener) {
        this.dragListener = dragListener;

    }

}
