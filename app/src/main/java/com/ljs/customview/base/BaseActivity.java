package com.ljs.customview.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ljs.customview.R;
import com.ljs.customview.view.SlideLayout;

/**
 * 作者：Administrator create on 2018/5/22
 * <p/>
 * 内容：
 */
public abstract class BaseActivity extends AppCompatActivity {

    private LinearLayout rootLayout;

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