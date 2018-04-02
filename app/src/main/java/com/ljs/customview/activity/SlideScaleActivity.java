package com.ljs.customview.activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.ljs.customview.R;
import com.ljs.customview.model.FullImageInfo;
import com.ljs.customview.view.BorderScaleImageView;

import java.io.Serializable;

public class SlideScaleActivity extends AppCompatActivity {

    private BorderScaleImageView view;
    private FullImageInfo fullImageInfo;
//    private ImageView fullImage;
    private float mScaleY;
    private float mScaleX;
    private int mTop;
    private int mLeft;
    private ColorDrawable mBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_scale);
        view = (BorderScaleImageView) findViewById(R.id.scale_view);
//        fullImage = (ImageView) findViewById(R.id.full_view);
        view.setScaleCompleteListener(new BorderScaleImageView.ScaleCompleteListener() {
            @Override
            public void scaleComplete() {
                int location[] = new int[2];
                view.getLocationOnScreen(location);
                FullImageInfo fullImageInfo = new FullImageInfo();
                fullImageInfo.setLocationX(location[0]);
                fullImageInfo.setLocationY(location[1]);
                fullImageInfo.setWidth(view.getWidth());
                fullImageInfo.setHeight(view.getHeight());
                Intent intent = new Intent(SlideScaleActivity.this, FullImageActivity.class);
                intent.putExtra("data",fullImageInfo);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.out);
            }
        });
    }


}
