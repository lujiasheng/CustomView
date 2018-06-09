package com.ljs.customview.activity.statusbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ljs.customview.R;
import com.ljs.customview.utils.StatusBarUtils;


public class StatusBarTranslucent extends AppCompatActivity {

    boolean isHide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //use requestWindowFeature() and hide actionbar or use AppTheme.NoActionBar style
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_translucent);
//        getSupportActionBar().hide();
        StatusBarUtils.translucentStatusBar(this, false);

        findViewById(R.id.toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHide = !isHide;
                StatusBarUtils.translucentStatusBar(StatusBarTranslucent.this, isHide);
            }
        });
    }
}
