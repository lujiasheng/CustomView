package com.ljs.customview.activity.statusbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ljs.customview.R;
import com.ljs.customview.utils.StatusBarUtils;


public class StatusBarWhiteToolBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_white_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StatusBar white with toolbar");

        StatusBarUtils.setStatusBarLightMode(this, Color.WHITE);
    }
}
