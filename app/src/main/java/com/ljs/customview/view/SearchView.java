package com.ljs.customview.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ljs.customview.R;

/**
 * 作者：Administrator create on 2018/3/28
 * <p>
 * 内容：搜索控件
 */
public class SearchView extends View {
    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        context.obtainStyledAttributes(attrs, R.styleable.SearchView);
    }
    //画笔
    private Paint mPaint;

    //宽
    private int mWidth;
    //高
    private int mHeight;


}