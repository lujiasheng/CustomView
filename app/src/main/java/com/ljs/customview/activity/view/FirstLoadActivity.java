package com.ljs.customview.activity.view;

import android.os.Bundle;
import android.view.View;

import com.ljs.customview.R;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.view.FirstLoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lujiasheng QQ:1060545627
 * @name CustomView
 * @class name：com.ljs.customview.activity.view
 * @class describe
 * @time 2018/7/21 14:46
 * @change
 * @chang time
 * @class describe
 */

public class FirstLoadActivity extends BaseActivity {


    @BindView(R.id.flv)
    FirstLoadingView flv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_load);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_start, R.id.btn_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
flv.startLoading();
                break;
            case R.id.btn_end:
                flv.stopLoading();
                break;
        }
    }
}
