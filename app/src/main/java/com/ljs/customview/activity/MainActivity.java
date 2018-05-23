package com.ljs.customview.activity;

import com.ljs.customview.R;
import com.ljs.customview.R.layout;
import com.ljs.customview.base.BaseActivity;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends BaseActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);

        Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(this);

        Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(this);

        Button button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                startActivity(new Intent(this, DragItemActivity.class));
                break;
            default:
                break;
        }

    }

    @Override
    protected boolean enableSliding() {

        return false;
    }
}
