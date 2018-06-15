package com.ljs.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ljs.customview.R;
import com.ljs.customview.activity.statusbar.StatusBarActivity;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.button5)
    Button button5;
    @BindView(R.id.button6)
    Button button6;
    @BindView(R.id.button7)
    Button button7;
    @BindView(R.id.button8)
    Button button8;
    @BindView(R.id.button9)
    Button button9;
    @BindView(R.id.button10)
    Button button10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }


    @Override
    protected boolean enableSliding() {

        return false;
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,R.id.button10,R.id.button11})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                startActivity(new Intent(this, RouteActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(this, PhotoZoomActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(this, SlideScaleActivity.class));
                break;
            case R.id.button4:
                startActivity(new Intent(this, DragCardActivity.class));
                break;
            case R.id.button5:
                startActivity(new Intent(this, SearchActivity.class));
                break;

            case R.id.button6:
                startActivity(new Intent(this, CurveActivity.class));
                break;
            case R.id.button7:
                startActivity(new Intent(this, WaveActivity.class));
                break;
            case R.id.button8:
                Class clazz = null;
                try {
                    clazz = Class.forName("com.ljs.customview.activity.DragItemActivity");
                } catch (ClassNotFoundException e) {
                }

                startActivity(new Intent(this, clazz));
                break;
            case R.id.button9:
                startActivity(new Intent(this, BlockLoadActivity.class));
                break;
            case R.id.button10:
                startActivity(new Intent(this, StatusBarActivity.class));
                break;
            case R.id.button11:
                startActivity(new Intent(this, LoadCommitActivity.class));
                break;

        }
    }
}
