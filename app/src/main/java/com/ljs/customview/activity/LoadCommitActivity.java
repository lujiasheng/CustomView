package com.ljs.customview.activity;

import android.os.Bundle;
import android.view.View;

import com.ljs.customview.R;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.view.LoadCommitView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadCommitActivity extends BaseActivity {

    @BindView(R.id.load_view)
    LoadCommitView loadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_commit);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_start, R.id.btn_success, R.id.btn_fail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                loadView.start();
                break;
            case R.id.btn_success:
                loadView.success();
                break;
            case R.id.btn_fail:
                loadView.fail();
                break;

        }
    }
}
