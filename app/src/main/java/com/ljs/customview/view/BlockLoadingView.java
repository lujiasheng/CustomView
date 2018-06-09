package com.ljs.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.ljs.customview.R;

/**
 * 作者：Administrator create on 2018/5/26
 * <p/>
 * 内容：
 */
public class BlockLoadingView extends View {


    // 固定方块 & 移动方块变量
    private FixedBlock[] mfixedBlocks;
    private MoveBlock mMoveBlock;

    // 方块属性（下面会详细介绍）
    private float half_BlockWidth;
    private float blockInterval;
    private Paint mPaint;
    private boolean isClock_Wise;
    private int initPosition;
    private int mCurrEmptyPosition;
    private int lineNumber;
    private int blockColor;

    // 方块的圆角半径
    private float moveBlock_Angle;
    private float fixBlock_Angle;

    // 动画属性
    private float mRotateDegree;
    private boolean mAllowRoll = false;
    private boolean isMoving = false;
    private int moveSpeed = 250;

    // 动画插值器（默认 = 线性）
    private Interpolator move_Interpolator;
    private AnimatorSet mAnimatorSet;

    public BlockLoadingView(Context context) {
        this(context, null);
    }

    public BlockLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        init();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BlockLoadingView);
        //方块行数量（至少三行）
        lineNumber = array.getInteger(R.styleable.BlockLoadingView_lineNumber, 3);
        if ((lineNumber < 3)) {
            lineNumber = 3;
        }
        //半个方块的宽度
        half_BlockWidth = array.getDimension(R.styleable.BlockLoadingView_half_BlockWidth, 30);
        //方块间间隔
        blockInterval = array.getDimension(R.styleable.BlockLoadingView_blockInterval, 10);
        //移动方块的圆角半径
        moveBlock_Angle = array.getDimension(R.styleable.BlockLoadingView_moveBlock_Angle, 20);
        //固定方块的圆角半径
        fixBlock_Angle = array.getDimension(R.styleable.BlockLoadingView_fixBlock_Angle, 10);

        //方块的颜色（使用十六进制,例如#333，#c0c0c0）
        int defaultColor = context.getResources().getColor(R.color.cardview_shadow_start_color);
        blockColor = array.getColor(R.styleable.BlockLoadingView_blockColor, defaultColor);

        //移动方块的初始位置（即空白位置）
        initPosition = array.getInteger(R.styleable.BlockLoadingView_initPosition, 0);

        // 由于移动方块只能是外部方块，所以这里需要判断方块是否属于外部方块
        if (isInsideTheRect(initPosition, lineNumber)) {
            initPosition = 0;
        }

        // 移动方块的移动速度
        // 注：不建议使用者将速度调得过快
        // 因为会导致ValueAnimator动画对象频繁重复的创建，存在内存抖动
        moveSpeed = array.getInteger(R.styleable.BlockLoadingView_moveSpeed, 250);

        // 设置移动方块动画的插值器
        int move_InterpolatorResId = array.getResourceId(R.styleable.BlockLoadingView_move_Interpolator,
                android.R.anim.linear_interpolator);
        move_Interpolator = AnimationUtils.loadInterpolator(context, move_InterpolatorResId);


        // 当方块移动后，需要实时更新的空白方块的位置
        mCurrEmptyPosition = initPosition;

        // 释放资源
        array.recycle();
    }

    private boolean isInsideTheRect(int position, int lineNumber) {
        //横
        if (position < lineNumber) {
            return false;
        } else if (position > (lineNumber * lineNumber - 1 - lineNumber)) {
            return false;
        } else if (position % lineNumber == 0) {
            return false;
        } else if ((position + 1) % lineNumber == 0) {
            return false;
        }

        return true;
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(blockColor);

        initBlocks(initPosition);
        initAnimator();
    }

    private void initBlocks(int initPosition) {
        //创建方块数量
        mfixedBlocks = new FixedBlock[lineNumber * lineNumber];
        //创建固定方块
        for (int i = 0; i < mfixedBlocks.length; i++) {
            mfixedBlocks[i] = new FixedBlock();

            mfixedBlocks[i].index = i;
            mfixedBlocks[i].isShow = initPosition == i ? false : true;
            mfixedBlocks[i].rectF = new RectF();
        }
        //创建移动方块
        mMoveBlock = new MoveBlock();
        mMoveBlock.isShow = false;
        mMoveBlock.rectF = new RectF();
        //关联外部方块的位置
        relateOuterBlock(mfixedBlocks, isClock_Wise);
    }

    private void relateOuterBlock(FixedBlock[] mfixedBlocks, boolean isClock_wise) {
        int lineCount = (int) Math.sqrt(mfixedBlocks.length);
        //横向第一行
        for (int i = 0; i < lineCount; i++) {
            //最左边
            if (i % lineCount == 0) {
                mfixedBlocks[i].next = isClock_wise ? mfixedBlocks[i + lineCount] : mfixedBlocks[i + 1];
                //最右边
            } else if ((i + 1) % lineCount == 0) {
                mfixedBlocks[i].next = isClock_wise ? mfixedBlocks[i - 1] : mfixedBlocks[i + lineCount];
            } else {
                mfixedBlocks[i].next = isClock_wise ? mfixedBlocks[i - 1] : mfixedBlocks[i + 1];
            }
        }
        //横向最后一行
        for (int i = (lineCount - 1) * lineCount; i < lineCount * lineCount; i++) {
            //最左边
            if (i % lineCount == 0) {
                mfixedBlocks[i].next = isClock_wise ? mfixedBlocks[i + 1] : mfixedBlocks[i - lineCount];
                //最右边
            } else if ((i + 1) % lineCount == 0) {
                mfixedBlocks[i].next = isClock_wise ? mfixedBlocks[i - 1] : mfixedBlocks[i - lineCount];
            } else {
                mfixedBlocks[i].next = isClock_wise ? mfixedBlocks[i - 1] : mfixedBlocks[i + 1];
            }
        }
        //纵向第一列
        for (int i = lineCount; i < (lineCount - 1) * lineCount; i += lineCount) {
            mfixedBlocks[i].next = isClock_wise ? mfixedBlocks[i + lineCount] : mfixedBlocks[i - lineCount];
        }
        //纵向最后一列
        for (int i = 2 * lineCount - 1; i < (lineCount - 1) * lineCount; i += lineCount) {
            mfixedBlocks[i].next = isClock_wise ? mfixedBlocks[i + lineCount] : mfixedBlocks[i - lineCount];
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        // 1. 设置移动方块的旋转中心坐标
        int cx = measuredWidth / 2;
        int cy = measuredHeight / 2;

        // 2. 设置固定方块的位置
        fixedBlockPosition(mfixedBlocks, cx, cy, blockInterval, half_BlockWidth);
        //3.设置移动方块的位置
        moveBlockPostion(mfixedBlocks, mMoveBlock, initPosition);

    }


    private void fixedBlockPosition(FixedBlock[] mfixedBlocks, int cx, int cy, float dividerWidth, float half_blockWidth) {
        float squareWidth = half_blockWidth * 2;
        int lineCount = (int) Math.sqrt(mfixedBlocks.length);

        float firstRectLeft = 0;
        float firstRectTop = 0;

        // 情况1：当行数 = 偶数时
        if (lineCount % 2 == 0) {
            int squareCountInAline = lineCount / 2;
            int diviCountInAline = squareCountInAline - 1;
            float firstRectLeftTopFromCenter = squareCountInAline * squareWidth
                    + diviCountInAline * dividerWidth
                    + dividerWidth / 2;
            firstRectLeft = cx - firstRectLeftTopFromCenter;
            firstRectTop = cy - firstRectLeftTopFromCenter;

            // 情况2：当行数 = 奇数时
        } else {
            int squareCountInAline = lineCount / 2;
            int diviCountInAline = squareCountInAline;
            float firstRectLeftTopFromCenter = squareCountInAline * squareWidth
                    + diviCountInAline * dividerWidth
                    + half_blockWidth;
            firstRectLeft = cx - firstRectLeftTopFromCenter;
            firstRectTop = cy - firstRectLeftTopFromCenter;
            firstRectLeft = cx - firstRectLeftTopFromCenter;
            firstRectTop = cy - firstRectLeftTopFromCenter;
        }

        for (int i = 0; i < lineCount; i++) {
            for (int j = 0; j < lineCount; j++) {
                if (i == 0) {
                    if (j == 0) {
                        mfixedBlocks[0].rectF.set(firstRectLeft, firstRectTop, firstRectLeft + squareWidth, firstRectTop + squareWidth);
                    } else {
                        int currIndex = i * lineCount + j;
                        mfixedBlocks[currIndex].rectF.set(mfixedBlocks[currIndex - 1].rectF);
                        mfixedBlocks[currIndex].rectF.offset(squareWidth + dividerWidth, 0);
                    }

                } else {
                    int currIndex = i * lineCount + j;
                    mfixedBlocks[currIndex].rectF.set(mfixedBlocks[currIndex - lineCount].rectF);
                    mfixedBlocks[currIndex].rectF.offset(0, squareWidth + dividerWidth);
                }
            }
        }

    }

    private void moveBlockPostion(FixedBlock[] mfixedBlocks, MoveBlock mMoveBlock, int initPosition) {
        FixedBlock mfixedBlock = mfixedBlocks[initPosition];
        mMoveBlock.rectF.set(mfixedBlock.next.rectF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制固定方块
        for (int i = 0; i < mfixedBlocks.length; i++) {
            if (mfixedBlocks[i].isShow) {
                canvas.drawRoundRect(mfixedBlocks[i].rectF, fixBlock_Angle, fixBlock_Angle, mPaint);
            }
        }
        if (mMoveBlock.isShow) {
            canvas.rotate(isClock_Wise ? mRotateDegree : -mRotateDegree, mMoveBlock.cx, mMoveBlock.cy);
            canvas.drawRoundRect(mMoveBlock.rectF, moveBlock_Angle, moveBlock_Angle, mPaint);
        }
    }

    private void initAnimator() {

        if (isMoving || getVisibility() != VISIBLE) {
            return;
        }

        // 设置标记位：以便是否停止动画
        isMoving = true;
        mAllowRoll = true;

        //获取固定方块空的位置
        FixedBlock currEmptyFixedBlock = mfixedBlocks[mCurrEmptyPosition];

        final FixedBlock moveBlock = currEmptyFixedBlock.next;

        mAnimatorSet = new AnimatorSet();

        ValueAnimator translateController = createTranslateValueAnimator(currEmptyFixedBlock, moveBlock, isClock_Wise);

        // 4.2 设置旋转动画：createMoveValueAnimator(（）->>关注3
        ValueAnimator moveConrtroller = createRotateValueAnimator();

        mAnimatorSet.setInterpolator(move_Interpolator);
        mAnimatorSet.playTogether(translateController, moveConrtroller);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                updateMoveBolck();

                mfixedBlocks[mCurrEmptyPosition].next.isShow = false;

                moveBlock.isShow = true;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isMoving = false;
                mfixedBlocks[mCurrEmptyPosition].isShow = true;
                mCurrEmptyPosition = mfixedBlocks[mCurrEmptyPosition].next.index;

                // 将移动的方块隐藏
                mMoveBlock.isShow = false;
            }
        });

    }

    public void startLoading() {
        mAnimatorSet.start();
    }

    private ValueAnimator createTranslateValueAnimator(FixedBlock currEmptyFixedBlock, final FixedBlock moveBlock, final boolean isClock_Wise) {

        float startAnimValue = 0;
        float endAnimValue = 0;
        PropertyValuesHolder left = null;
        PropertyValuesHolder top = null;

        ValueAnimator valueAnimator = new ValueAnimator().setDuration(moveSpeed);

        if (isNextRollLeftOrRight(currEmptyFixedBlock, moveBlock)) {

            if (isClock_Wise && currEmptyFixedBlock.index > moveBlock.index || !isClock_Wise && currEmptyFixedBlock.index > moveBlock.index) {
                startAnimValue = moveBlock.rectF.left;
                endAnimValue = moveBlock.rectF.left + blockInterval;
            } else if (isClock_Wise && currEmptyFixedBlock.index < moveBlock.index || !isClock_Wise && currEmptyFixedBlock.index < moveBlock.index) {
                startAnimValue = moveBlock.rectF.left;
                endAnimValue = moveBlock.rectF.left - blockInterval;
            }

            left = PropertyValuesHolder.ofFloat("left", startAnimValue, endAnimValue);

            valueAnimator.setValues(left);

        } else {
            if (isClock_Wise && currEmptyFixedBlock.index < moveBlock.index
                    || !isClock_Wise && currEmptyFixedBlock.index < moveBlock.index) {

                startAnimValue = moveBlock.rectF.top;
                endAnimValue = moveBlock.rectF.top - blockInterval;

                // 情况4：顺时针且在最右列 / 逆时针且在最左列，移动方块向下移动
            } else if (isClock_Wise && currEmptyFixedBlock.index > moveBlock.index
                    || !isClock_Wise && currEmptyFixedBlock.index > moveBlock.index) {
                startAnimValue = moveBlock.rectF.top;
                endAnimValue = moveBlock.rectF.top + blockInterval;
            }

            // 设置属性值
            top = PropertyValuesHolder.ofFloat("top", startAnimValue, endAnimValue);
            valueAnimator.setValues(top);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object left = animation.getAnimatedValue("left");
                Object top = animation.getAnimatedValue("top");

                if (left != null) {
                    moveBlock.rectF.offset((Float) left, moveBlock.rectF.top);
                }
                if (top != null) {
                    moveBlock.rectF.offset(moveBlock.rectF.left, (Float) top);
                }

                setMoveBlockRouteCenter(mMoveBlock, isClock_Wise);

                invalidate();
            }
        });

        return valueAnimator;
    }


    private boolean isNextRollLeftOrRight(FixedBlock currEmptyFixedBlock, FixedBlock moveBlock) {
        if (currEmptyFixedBlock.rectF.left - moveBlock.rectF.left == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void setMoveBlockRouteCenter(MoveBlock mMoveBlock, boolean isClock_wise) {
        if (mMoveBlock.index == 0) {
            mMoveBlock.cx = mMoveBlock.rectF.right;
            mMoveBlock.cy = mMoveBlock.rectF.bottom;
        } else if (mMoveBlock.index == lineNumber - 1) {
            mMoveBlock.cx = mMoveBlock.rectF.left;
            mMoveBlock.cy = mMoveBlock.rectF.bottom;
        } else if (mMoveBlock.index == lineNumber * lineNumber - 1) {
            mMoveBlock.cx = mMoveBlock.rectF.left;
            mMoveBlock.cy = mMoveBlock.rectF.top;
        } else if (mMoveBlock.index == (lineNumber - 1) * lineNumber) {
            mMoveBlock.cx = mMoveBlock.rectF.right;
            mMoveBlock.cy = mMoveBlock.rectF.top;
        } else if (mMoveBlock.index % lineNumber == 0) {
            mMoveBlock.cx = mMoveBlock.rectF.right;
            mMoveBlock.cy = isClock_wise ? mMoveBlock.rectF.top : mMoveBlock.rectF.bottom;
        } else if (mMoveBlock.index < lineNumber) {
            mMoveBlock.cx = isClock_wise ? mMoveBlock.rectF.right : mMoveBlock.rectF.left;
            mMoveBlock.cy = mMoveBlock.rectF.bottom;
        } else if ((mMoveBlock.index + 1) % lineNumber == 0) {
            mMoveBlock.cx = mMoveBlock.rectF.left;
            mMoveBlock.cy = isClock_wise ? mMoveBlock.rectF.bottom : mMoveBlock.rectF.top;
        } else if (mMoveBlock.index > (lineNumber - 1) * lineNumber) {
            mMoveBlock.cx = isClock_wise ? mMoveBlock.rectF.left : mMoveBlock.rectF.right;
            mMoveBlock.cy = mMoveBlock.rectF.top;
        }
    }

    private ValueAnimator createRotateValueAnimator() {
        ValueAnimator rotateAnim = ValueAnimator.ofFloat(0, 90).setDuration(moveSpeed);

        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object animatedValue = animation.getAnimatedValue();
                mRotateDegree = (float) animatedValue;
                invalidate();
            }
        });
        return rotateAnim;
    }

    private void updateMoveBolck() {

        mMoveBlock.rectF = mfixedBlocks[mCurrEmptyPosition].next.rectF;
        mMoveBlock.index = mfixedBlocks[mCurrEmptyPosition].next.index;
        setMoveBlockRouteCenter(mMoveBlock, isClock_Wise);
    }

    public void stopMoving() {

        // 通过标记位来设置
        mAllowRoll = false;
    }

    /**
     * 固定方块
     */
    private class FixedBlock {
        //存储方块的坐标位置参数
        RectF rectF;

        //方块对应的序号
        int index;

        //标志位：判断是否需要绘制
        boolean isShow;

        //指向下一个需要移动的方块
        FixedBlock next;

    }

    private class MoveBlock {
        //存储方块的坐标位置参数
        RectF rectF;

        //方块对应的序号
        int index;

        //标志位：判断是否需要绘制
        boolean isShow;

        //旋转中心的坐标
        //移动时旋转的x，y
        float cx;
        float cy;
    }
}