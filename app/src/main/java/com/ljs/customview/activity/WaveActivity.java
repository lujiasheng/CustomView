package com.ljs.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljs.customview.R;
import com.ljs.customview.view.WaveView;

public class WaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        WaveView waveView = (WaveView) findViewById(R.id.wave_view);
//        waveView.startWave();
    }
}
