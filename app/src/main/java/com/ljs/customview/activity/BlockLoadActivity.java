package com.ljs.customview.activity;

import android.os.Bundle;

import com.ljs.customview.R;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.view.BlockLoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlockLoadActivity extends BaseActivity {

    @BindView(R.id.block_view)
    BlockLoadingView blockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_load);
        ButterKnife.bind(this);
//        blockView.startLoading();
    }
}
