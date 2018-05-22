package com.ljs.customview.activity;

import com.ljs.customview.R;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.utils.FastBlur;
import com.ljs.customview.view.PhotoZoomView;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class PhotoZoomActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_zoom);
        PhotoZoomView iv = (PhotoZoomView) findViewById(R.id.iv);
        iv.setImageBitmap(FastBlur.fastblur(this, BitmapFactory.decodeResource(getResources(),R.drawable.t1),14));
    }
}
