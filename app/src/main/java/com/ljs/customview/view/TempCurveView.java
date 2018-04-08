package com.ljs.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.ljs.customview.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator create on 2018/4/7
 * <p/>
 * 内容：温度曲线控件
 */
public class TempCurveView extends View {

    public TempCurveView(Context context) {
        this(context, null);
    }

    public TempCurveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //坐标宽高
    private float coorWidth;
    private float coorHeight;
    private int dataIndex;

    private static final int SECONDS = 2000;
    //画布颜色
    private String canvasColor = "#55acef";
    //画笔颜色
    private String color = "#ffffff";
    //文字大小
    private int textSize = 8;
    //是否显示坐标轴
    private boolean showCoor = true;
    //X,Y轴的值
    private List<Integer> textX;
    private List<Integer> textY;
    //X,Y轴单位
    private String unitX = "h";
    private String unitY = "℃";

    private Paint mPaint;
    private Paint linePaint;
    //宽高
    private float mWidth;
    private float mHeight;
    //外边距
    private float margin = 15;

    private Path coorPath;
    private Path pointPath;
    private Path curvePath;

    private float coorValue;
    private int pointHeight = 5;
    private int Xindex;
    private int Yindex;
    //数据
    private List<Float> dataX;
    private List<Float> dataY;

    public TempCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textSize = ActivityUtils.dip2px(textSize, context);
        margin = ActivityUtils.dip2px(margin, context);
        pointHeight = ActivityUtils.dip2px(pointHeight, context);

        textX = new ArrayList<>();
        textY = new ArrayList<>();
        dataX = new ArrayList<>();
        dataY = new ArrayList<>();

        textX.add(2);
        textX.add(4);
        textX.add(6);
        textX.add(8);
        textX.add(10);
        textX.add(12);
        textX.add(14);
        textX.add(16);
        textX.add(18);
        textX.add(20);
        textX.add(22);
        textX.add(24);
        textY.add(5);
        textY.add(10);
        textY.add(15);
        textY.add(20);
        textY.add(25);
        textY.add(30);
        textY.add(35);
        textY.add(40);

        dataX.add(0f);
        dataX.add(3f);
        dataX.add(6f);
        dataX.add(9f);
        dataX.add(12f);
        dataX.add(15f);
        dataX.add(18f);
        dataX.add(21f);
        dataX.add(24f);

        dataY.add(5f);
        dataY.add(37f);
        dataY.add(13f);
        dataY.add(15f);
        dataY.add(32f);
        dataY.add(27f);
        dataY.add(34f);
        dataY.add(4f);
        dataY.add(14f);

        initPaint();
        initPath();
        initListener();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor(color));
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(textSize);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.parseColor(color));
    }

    private void initPath() {
        coorPath = new Path();
        pointPath = new Path();
        curvePath = new Path();

    }

    private void initListener() {

        final ValueAnimator XpointStart = ObjectAnimator.ofInt(0, textX.size());
        XpointStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Xindex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });


        final ValueAnimator YpointStart = ObjectAnimator.ofInt(0, textY.size());
        YpointStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Yindex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        final ValueAnimator dataStart = ObjectAnimator.ofInt(0, textY.size());
        dataStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dataIndex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator coorStart = ObjectAnimator.ofFloat(0, 1);
        coorStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        coorStart.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dataStart.start();
            }
        });
        coorStart.setDuration(SECONDS).start();
        XpointStart.setDuration(SECONDS).start();
        YpointStart.setDuration(SECONDS).start();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        coorWidth = mWidth - 3 * margin;
        coorHeight = mHeight - 3 * margin;
        canvas.drawColor(Color.parseColor(canvasColor));
        canvas.translate(margin, mHeight - margin);
        canvas.scale(1, -1);
        if (showCoor) {
            drawCoor(canvas);
            drawPoints(canvas);
        }
    }

    /**
     * 绘制X，Y轴
     *
     * @param canvas
     */
    private void drawCoor(Canvas canvas) {

        coorPath.moveTo(0, 0);
        coorPath.lineTo(coorValue * (mWidth - 2 * margin), 0);
        coorPath.moveTo(0, 0);
        coorPath.lineTo(0, coorValue * (mHeight - 2 * margin));
        canvas.drawPath(coorPath, linePaint);
        drawCurve(canvas);
    }

    /**
     * 绘制坐标点和值
     *
     * @param canvas
     */
    private void drawPoints(Canvas canvas) {
        if (textX != null && textY != null) {
            float gridHor = (coorWidth) / textX.size();
            float gridVer = (coorHeight) / textY.size();
            for (int i = 1; i <= Xindex; i++) {
                pointPath.moveTo(i * gridHor, 0);
                pointPath.lineTo(i * gridHor, pointHeight);
            }
            for (int i = 1; i <= Yindex; i++) {
                pointPath.moveTo(0, i * gridVer);
                pointPath.lineTo(pointHeight, i * gridVer);
            }
            canvas.drawPath(pointPath, linePaint);
            canvas.scale(1, -1);

            Rect rect = new Rect();
            mPaint.getTextBounds("0", 0, "0".length(), rect);
            canvas.drawText("0", -rect.width() / 2, margin - rect.height() / 2, mPaint);
            for (int i = 1; i <= Xindex; i++) {
                mPaint.getTextBounds(textX.get(i - 1) + "", 0, (textX.get(i - 1) + "").length(), rect);
                canvas.drawText(textX.get(i - 1) + "", i * gridHor, margin - rect.height() / 2, mPaint);
                if (i == textX.size()) {
                    canvas.drawText(unitX, mWidth - 2 * margin, margin - rect.height() / 2, mPaint);
                }
            }
            for (int i = 1; i <= Yindex; i++) {
                mPaint.getTextBounds(textY.get(i - 1) + "", 0, (textY.get(i - 1) + "").length(), rect);
                canvas.drawText(textY.get(i - 1) + "", -margin + rect.width() / 2, -i * gridVer + rect.height() / 2, mPaint);
                if (i == textY.size()) {
                    canvas.drawText(unitY, -margin + rect.width() / 2, -mHeight + 2 * margin + rect.height() / 2, mPaint);
                }
            }
            canvas.scale(1, -1);
        }
    }

    private void drawCurve(Canvas canvas) {
        if (dataX != null && dataY != null) {
            for (int i = 1; i <= dataIndex; i++) {
                curvePath.moveTo(dataX.get(i - 1) / textX.get(textX.size() - 1) * coorWidth,
                        dataY.get(i - 1) / textY.get(textY.size() - 1) * coorHeight);
                if (i%2==0) {
                    curvePath.quadTo(dataX.get(i) / textX.get(textX.size() - 1) * coorWidth,
                            dataY.get(i - 1) / textY.get(textY.size() - 1) * coorHeight,
                            dataX.get(i) / textX.get(textX.size() - 1) * coorWidth,
                            dataY.get(i) / textY.get(textY.size() - 1) * coorHeight
                    );
                }else {
                    curvePath.quadTo(dataX.get(i-1) / textX.get(textX.size() - 1) * coorWidth,
                            dataY.get(i) / textY.get(textY.size() - 1) * coorHeight,
                            dataX.get(i) / textX.get(textX.size() - 1) * coorWidth,
                            dataY.get(i) / textY.get(textY.size() - 1) * coorHeight
                    );
                }
            }
            canvas.drawPath(curvePath, linePaint);
        }
    }

    public void setShowCoor(boolean showCoor) {
        this.showCoor = showCoor;
    }
}