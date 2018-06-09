package com.ljs.customview.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.ljs.customview.view.SlideLayout;

/**
 * 作者：Administrator create on 2018/5/22
 * <p/>
 * 内容：
 */
public  class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (enableSliding()) {
            SlideLayout rootView = new SlideLayout(this);
            rootView.bindActivity(this);
        }
    }



    protected boolean enableSliding() {
        return true;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}