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
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
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

    private ValueAnimator coorStart;
    private ValueAnimator xpointStart;
    private ValueAnimator ypointStart;
    private ValueAnimator dataStart;
    private Paint curvePaint;
    private PathMeasure mPathMeasure;

    public TempCurveView(Context context) {
        this(context, null);
    }

    public TempCurveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //坐标宽高
    private float coorWidth;
    private float coorHeight;
    private float curveValue;

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

    private Path XcoorPath;
    private Path YcoorPath;
    private Path pointPath;
    private Path curvePath;

    private float coorValue;
    private int pointHeight = 5;
    private int Xindex;
    private int Yindex;
    //数据
    private List<Float> dataX;
    private List<Float> dataY;
    /**
     * 即将要穿越的点集合
     */
    private List<PointF> mPoints = new ArrayList<>();
    /**
     * 中点集合
     */
    private List<PointF> mMidPoints = new ArrayList<>();
    /**
     * 中点的中点集合
     */
    private List<PointF> mMidMidPoints = new ArrayList<>();
    /**
     * 移动后的点集合(控制点)
     */
    private List<PointF> mControlPoints = new ArrayList<>();

    public TempCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textSize = ActivityUtils.dip2px(textSize, context);
        margin = ActivityUtils.dip2px(margin, context);
        pointHeight = ActivityUtils.dip2px(pointHeight, context);
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

        curvePaint = new Paint(linePaint);
        curvePaint.setStrokeWidth(2);
    }

    private void initPath() {
        XcoorPath = new Path();
        YcoorPath = new Path();
        pointPath = new Path();
        curvePath = new Path();
        mPathMeasure = new PathMeasure();


    }

    private void initListener() {
        curveAnimation();
        xyCoorAnimation();
        coorStart = ObjectAnimator.ofFloat(0, 1);
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
        coorStart.setDuration(SECONDS);
    }

    private void curveAnimation() {
        dataStart = ObjectAnimator.ofFloat(0, 1);
        dataStart.setDuration(SECONDS);
        dataStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curveValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    private void xyCoorAnimation() {
        if (textX != null && textY != null) {
            xpointStart = ObjectAnimator.ofInt(0, textX.size());
            xpointStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Xindex = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });

            ypointStart = ObjectAnimator.ofInt(0, textY.size());
            ypointStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Yindex = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            xpointStart.setDuration(SECONDS);
            ypointStart.setDuration(SECONDS);
        }
    }

    public void start() {
        if (showCoor) {
            if (coorStart != null) {
                coorStart.start();
            }
            if (xpointStart != null && ypointStart != null) {
                xpointStart.start();
                ypointStart.start();
            }
        } else {
            if (dataStart != null) {
                dataStart.start();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        coorWidth = mWidth - 3 * margin;
        coorHeight = mHeight - 3 * margin;
        XcoorPath.moveTo(0, 0);
        XcoorPath.lineTo(mWidth - 2 * margin, 0);
        YcoorPath.moveTo(0, 0);
        YcoorPath.lineTo(0, mHeight - 2 * margin);
        initPoints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor(canvasColor));
        canvas.translate(margin, mHeight - margin);
        canvas.scale(1, -1);
        if (showCoor) {
            drawCoor(canvas);
            drawPoints(canvas);
        }
        drawCurve(canvas);
    }

    /**
     * 绘制X，Y轴
     *
     * @param canvas
     */
    private void drawCoor(Canvas canvas) {
        mPathMeasure.setPath(XcoorPath, false);
        Path dst = new Path();
        mPathMeasure.getSegment(0, coorValue * mPathMeasure.getLength(), dst, true);
        canvas.drawPath(dst, linePaint);
        mPathMeasure.setPath(YcoorPath, false);
        Path dst1 = new Path();
        mPathMeasure.getSegment(0, coorValue * mPathMeasure.getLength(), dst1, true);
        canvas.drawPath(dst1, linePaint);
    }

    /**
     * 绘制坐标点和值
     *
     * @param canvas
     */
    private void drawPoints(Canvas canvas) {
        pointPath.reset();
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

    /**
     * 初始化曲线各个点
     */
    private void initPoints() {

        if (dataX != null && dataY != null) {
            //初始化连接点
            for (int i = 0; i < dataX.size(); i++) {
                PointF point = new PointF(dataX.get(i) * coorWidth / textX.get(textX.size() - 1),
                        dataY.get(i) * coorHeight / textY.get(textY.size() - 1));
                mPoints.add(point);
            }
            //初始化中点
            for (int i = 0; i < mPoints.size(); i++) {
                if (i == mPoints.size() - 1) {
                    continue;
                } else {
                    PointF pointF = new PointF((mPoints.get(i).x + mPoints.get(i + 1).x) / 2, (mPoints.get(i).y + mPoints.get(i + 1).y) / 2);
                    mMidPoints.add(pointF);
                }
            }
            //初始化中点的中点
            for (int i = 0; i < mMidPoints.size(); i++) {

                if (i == mMidPoints.size() - 1) {
                    continue;
                } else {
                    PointF pointF = new PointF((mMidPoints.get(i).x + mMidPoints.get(i + 1).x) / 2, (mMidPoints.get(i).y + mMidPoints.get(i + 1).y) / 2);
                    mMidMidPoints.add(pointF);
                }
            }
            //初始化控制点
            for (int i = 0; i < mPoints.size(); i++) {
                if (i == 0 || i == mPoints.size() - 1) {
                    continue;
                } else {
                    PointF before = new PointF();
                    PointF after = new PointF();
                    before.x = mPoints.get(i).x - mMidMidPoints.get(i - 1).x + mMidPoints.get(i - 1).x;
                    before.y = mPoints.get(i).y - mMidMidPoints.get(i - 1).y + mMidPoints.get(i - 1).y;
                    after.x = mPoints.get(i).x - mMidMidPoints.get(i - 1).x + mMidPoints.get(i).x;
                    after.y = mPoints.get(i).y - mMidMidPoints.get(i - 1).y + mMidPoints.get(i).y;
                    mControlPoints.add(before);
                    mControlPoints.add(after);
                }
            }
            for (int i = 0; i < mPoints.size(); i++) {
                if (i == 0) {
                    curvePath.moveTo(mPoints.get(i).x, mPoints.get(i).y);
                    curvePath.quadTo(mControlPoints.get(i).x, mControlPoints.get(i).y,
                            mPoints.get(i + 1).x, mPoints.get(i + 1).y);
                } else if (i < mPoints.size() - 2) {
                    curvePath.cubicTo(mControlPoints.get(2 * i - 1).x, mControlPoints.get(2 * i - 1).y,
                            mControlPoints.get(2 * i).x, mControlPoints.get(2 * i).y,
                            mPoints.get(i + 1).x, mPoints.get(i + 1).y);
                } else if (i == mPoints.size() - 2) {
                    curvePath.quadTo(mControlPoints.get(mControlPoints.size() - 1).x, mControlPoints.get(mControlPoints.size() - 1).y,
                            mPoints.get(i + 1).x, mPoints.get(i + 1).y);
                }
            }
        }

    }

    /**
     * 绘制曲线
     *
     * @param canvas
     */
    private void drawCurve(Canvas canvas) {
        mPathMeasure.setPath(curvePath, false);
        Path dst = new Path();
        mPathMeasure.getSegment(0, curveValue * mPathMeasure.getLength(), dst, true);
        canvas.drawPath(dst, curvePaint);

    }


    public void setShowCoor(boolean showCoor) {
        this.showCoor = showCoor;
    }

    public void setCoor(List<Integer> textX, List<Integer> textY) {
        this.textX = textX;
        this.textY = textY;
        xyCoorAnimation();
    }


    public void setCanvasColor(String canvasColor) {
        this.canvasColor = canvasColor;
    }

    public void setUnitX(String unitX, String unitY) {
        this.unitX = unitX;
        this.unitY = unitY;
    }


    public void setColor(String color) {
        this.color = color;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public void setData(List<Float> dataX, List<Float> dataY) {
        mPoints.clear();
        mMidPoints.clear();
        mMidMidPoints.clear();
        mControlPoints.clear();
        this.dataX = dataX;
        this.dataY = dataY;

    }

    public void startCurveAnimation() {
        if (dataStart != null) {
            dataStart.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}