package com.ljs.customview.activity.statusbar;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ljs.customview.R;
import com.ljs.customview.utils.StatusBarUtils;


public class StatusBarWhiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_white);
        getSupportActionBar().setTitle("StatusBarWhite");

        StatusBarUtils.setStatusBarLightMode(this, Color.WHITE);
    }
}
