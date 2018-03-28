package com.ljs.customview.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.ljs.customview.imp.Listener;


/**
 * Created by PC on 2018/3/28.
 */

public class DragCardView extends CardView {
    private static final String TAG = "信息";
    private int lastX;
    private int lastY;
    private int w;
    private int h;

    public DragCardView(Context context) {
        super(context);
    }

    public DragCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    private boolean isFrist = true;
    int initLEFT;
    int initRIGHT;
    int initXX;
    int lastXX;

    // 视图坐标方式
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                int s = lastXX - initXX;

//手指离开后返回原来位置
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout(initLEFT,
                                getTop(),
                                initRIGHT,
                                getBottom());
                    }
                }, 2000);
                dragListener.onCallBack(MotionEvent.ACTION_UP, MotionEvent.ACTION_UP);
//                }

                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量
                int offsetX = (int) (event.getX() - lastX);
                lastXX = (int) event.getX();
                Log.e("信息", "ACTION_MOVE" + lastXX);
                if (offsetX > 0) {//左往右移动
                    layout(getLeft() + offsetX,
                            getTop(),
                            getRight() + offsetX,
                            getBottom());
                    dragListener.onCallBack(MotionEvent.ACTION_MOVE, MotionEvent.ACTION_MOVE);
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
