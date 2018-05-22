package com.ljs.customview.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ljs.customview.view.SlideLayout;

/**
 * 作者：Administrator create on 2018/5/22
 * <p/>
 * 内容：
 */
public abstract class BaseActivity extends AppCompatActivity {
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
}