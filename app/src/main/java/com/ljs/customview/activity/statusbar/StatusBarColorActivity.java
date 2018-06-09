package com.ljs.customview.activity.statusbar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.ljs.customview.R;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.utils.StatusBarUtils;


public class StatusBarColorActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_color);
        getSupportActionBar().setTitle("StatusBarColorByCodes");
        StatusBarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorAccent));
    }
}
