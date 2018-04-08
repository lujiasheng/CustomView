package com.ljs.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljs.customview.R;
import com.ljs.customview.view.TempCurveView;

public class CurveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve);
        TempCurveView tempCurveView = (TempCurveView) findViewById(R.id.curve_view);
    }
}
