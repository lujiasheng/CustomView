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
    private ImageView fullImage;
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
        fullImage = (ImageView) findViewById(R.id.full_view);
        view.setScaleCompleteListener(new BorderScaleImageView.ScaleCompleteListener() {
            @Override
            public void scaleComplete() {
                int location[] = new int[2];
                view.getLocationOnScreen(location);
                fullImageInfo = new FullImageInfo();
                fullImageInfo.setLocationX(location[0]);
                fullImageInfo.setLocationY(location[1]);
                fullImageInfo.setWidth(view.getWidth());
                fullImageInfo.setHeight(view.getHeight());
                final int left = fullImageInfo.getLocationX();
                final int top = fullImageInfo.getLocationY();
                final int width = fullImageInfo.getWidth();
                final int height = fullImageInfo.getHeight();
                mBackground = new ColorDrawable(Color.BLACK);
                fullImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        fullImage.getViewTreeObserver().removeOnPreDrawListener(this);
                        int location[] = new int[2];
                        fullImage.getLocationOnScreen(location);
                        mLeft = left - location[0];
                        mTop = top - location[1];
                        mScaleX = width * 1.0f / fullImage.getWidth();
                        mScaleY = height * 1.0f / fullImage.getHeight();
                        activityEnterAnim();
                        return true;
                    }
                });
                fullImage.setImageResource(R.drawable.ts);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void activityEnterAnim() {
        fullImage.setPivotX(0);
        fullImage.setPivotY(0);
        fullImage.setScaleX(mScaleX);
        fullImage.setScaleY(mScaleY);
        fullImage.setTranslationX(mLeft);
        fullImage.setTranslationY(mTop);
        fullImage.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).
                setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }
}
