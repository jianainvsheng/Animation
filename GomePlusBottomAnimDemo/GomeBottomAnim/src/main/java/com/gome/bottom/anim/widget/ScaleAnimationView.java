package com.gome.bottom.anim.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by yangjian on 2017/10/18.
 */

public class ScaleAnimationView extends ZoomAnimationView {

    public ScaleAnimationView(Context context) {
        super(context);
    }

    public ScaleAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onInsideViewAnimPrepare(ImageView imageView,ValueAnimator insideAnim) {
        switchInsideVisibility(true, 0f);
    }

    @Override
    public void onInsideViewAnimUpdata(ImageView imageView, int curentInt, int totalInt) {
        float scaleSize = (float) curentInt / totalInt;
        setScaleView(imageView,scaleSize);
    }
}
