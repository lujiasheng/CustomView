package com.ljs.customview.activity.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljs.customview.R;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.view.TempCurveView;

import java.util.ArrayList;
import java.util.List;

public class CurveActivity extends BaseActivity {
    private List<Integer> textX;
    private List<Integer> textY;
    private List<Float> dataX;
    private List<Float> dataY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve);
        TempCurveView tempCurveView = (TempCurveView) findViewById(R.id.curve_view);
        textX = new ArrayList<>();
        textY = new ArrayList<>();
        dataX = new ArrayList<>();
        dataY = new ArrayList<>();

        textX.add(2);
        textX.add(4);
        textX.add(6);
        textX.add(8);
        textX.add(10);
        textX.add(12);

        textY.add(5);
        textY.add(10);
        textY.add(15);
        textY.add(20);
        textY.add(25);
        textY.add(30);

        dataX.add(0f);
        dataX.add(2f);
        dataX.add(4f);
        dataX.add(6f);
        dataX.add(8f);
        dataX.add(10f);
        dataX.add(12f);

        dataY.add(21f);
        dataY.add(25f);
        dataY.add(24f);
        dataY.add(21f);
        dataY.add(26f);
        dataY.add(27f);
        dataY.add(28f);

        tempCurveView.setCoor(textX, textY);
        tempCurveView.setData(dataX, dataY);
        tempCurveView.setShowCoor(true);
        tempCurveView.start();
    }
}
