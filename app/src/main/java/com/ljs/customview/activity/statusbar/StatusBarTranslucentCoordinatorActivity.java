package com.ljs.customview.activity.statusbar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ljs.customview.R;
import com.ljs.customview.utils.StatusBarUtils;


public class StatusBarTranslucentCoordinatorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_translucent_coordinator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StatusBarColorToolbar");

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_layout);
        collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        StatusBarUtils.setStatusBarColorForCollapsingToolbar(this, mAppBarLayout, collapsingToolbarLayout, toolbar, ContextCompat.getColor(this, R.color.colorPrimary));
    }
}
