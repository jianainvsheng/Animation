package com.gome.bottom.anim.widget;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by yangjian on 2017/10/18.
 */

public class RotateAnimationView extends ZoomAnimationView {

    public RotateAnimationView(Context context) {
        super(context);
    }

    public RotateAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onInsideViewAnimPrepare(ImageView imageView,ValueAnimator insideAnim) {
        switchInsideVisibility(true,0f);
        //insideAnim.setDuration(3000);
    }

    @Override
    public void onInsideViewAnimUpdata(ImageView imageView, int curentInt, int totalInt) {

        float rotateSize = (float) curentInt / totalInt ;
        setScaleView(imageView,rotateSize);
        imageView.setRotation(rotateSize* 180);
    }
}
