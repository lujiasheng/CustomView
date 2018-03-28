package com.ljs.customview.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.ljs.customview.R;
import com.ljs.customview.imp.Listener;
import com.ljs.customview.view.DragCardView;

public class DragCardActivity extends AppCompatActivity {

    private DragCardView cardView;
    private ImageView img;

    /**
     * 方法说明：返回屏幕宽度，单位：像素
     */
    public static int getScreenWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 方法说明：返回屏幕高度，单位：像素
     */
    public static int getScreenHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_card);
        cardView = (DragCardView) findViewById(R.id.dragCardView);
        img = (ImageView) findViewById(R.id.img);

//        animation= AnimationUtils.loadAnimation(this,R.anim.scale_anim);
        cardView.setOnDragViewListener(new Listener<Integer, Integer>() {
            @Override
            public void onCallBack(Integer integer, Integer reply) {
                switch (integer) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        cardView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 3000);



//                       Intent intent= new Intent(DragCardActivity.this, SecondActivity.class);
//                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DragCardActivity.this,
//                                img, "img");
//                        startActivity(intent, options.toBundle());

//

                        break;
                }
            }
        });
    }
}
