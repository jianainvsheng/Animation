package com.gome.bottom.anim.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gome.bottom.anim.R;

/**
 * Created by yangjian on 2017/10/17.
 */

public class ZoomAnimView extends RelativeLayout implements Animation.AnimationListener, View.OnClickListener {

    /**
     * 外部的填充布局view
     */
    private ImageView mOutAnimView;

    /**
     * 里面包裹自适应布局view 居中
     */
    private ImageView mInsideView;

    private long durationMillis = 200l;

    private Animation mBoostAnim, mLessenAnim,mInsideAnim;

    //private ValueAnimator mInsideAnim;

    private Drawable mOutsideFocusDrawable,
            mOutsideUnFocusDrawable,
            mInsideDrawable,
            mDefaultDrawable;

    private boolean isAnimFinish = true;

    private OnClickListener mClickListener;

    public ZoomAnimView(Context context) {
        this(context, null);
    }

    public ZoomAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {

        if(l != this){

            mClickListener = l;
        }
        super.setOnClickListener(this);
    }

    private void init(AttributeSet attrs) {

        mOutAnimView = new ImageView(getContext());
        RelativeLayout.LayoutParams outParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        outParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        outParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mOutAnimView.setLayoutParams(outParams);

        mInsideView = new ImageView(getContext());
        RelativeLayout.LayoutParams inParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        inParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        inParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mInsideView.setLayoutParams(inParams);

        addView(mOutAnimView);
        addView(mInsideView);
        if (attrs != null) {

            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ZoomAnimView);
            mOutsideFocusDrawable = typedArray.getDrawable(R.styleable.ZoomAnimView_image_out_focus);
            mOutsideUnFocusDrawable = typedArray.getDrawable(R.styleable.ZoomAnimView_image_out_unfocus);
            mInsideDrawable = typedArray.getDrawable(R.styleable.ZoomAnimView_image_inside);
            mDefaultDrawable = typedArray.getDrawable(R.styleable.ZoomAnimView_image_out_defalut);

            if (mOutsideFocusDrawable != null) {
                mBoostAnim = AnimationUtils.loadAnimation(getContext(), R.anim.gome_plus_anim_boost);
                mBoostAnim.setDuration(durationMillis/3*2);
                mBoostAnim.setAnimationListener(this);
            }

            if (mInsideDrawable != null) {

                mInsideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.gome_plus_anim_boost);
                mInsideAnim.setDuration(durationMillis/3);
                mInsideAnim.setAnimationListener(this);
            }

            if (mOutsideUnFocusDrawable != null) {

                mLessenAnim = AnimationUtils.loadAnimation(getContext(), R.anim.gome_plus_anim_lessen);
                mLessenAnim.setDuration(durationMillis);
                mLessenAnim.setAnimationListener(this);
            }
        }
        setOnClickListener(this);
        showUnFocusView();
    }

    @Override
    public void onAnimationStart(Animation animation) {

        isAnimFinish = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if(animation == mLessenAnim){
            switchInsideVisibility(true);
            if(mOutsideFocusDrawable != null){

                mOutAnimView.setImageDrawable(mOutsideFocusDrawable);
            }

            if(mBoostAnim != null){

                //mInsideView.startAnimation(mBoostAnim);
                mOutAnimView.startAnimation(mBoostAnim);
            }else{
                isAnimFinish = true;
            }
        }else if(animation == mBoostAnim){

            if(mBoostAnim != null){

                mInsideView.startAnimation(mInsideAnim);
                //mOutAnimView.startAnimation(mBoostAnim);
            }else{
                isAnimFinish = true;
            }
        }else {
            isAnimFinish = true;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onClick(View v) {

        showFocusView(true);
        if(mClickListener != null){

            mClickListener.onClick(this);
        }
    }

    public void showUnFocusView() {

        if(!isAnimFinish)
            return;
        switchInsideVisibility(false);
        if (mOutsideUnFocusDrawable != null) {

            mOutAnimView.setImageDrawable(mOutsideUnFocusDrawable);
        } else if (mDefaultDrawable != null) {

            mOutAnimView.setImageDrawable(mDefaultDrawable);
        }
    }

    private void switchInsideVisibility(boolean isVisibility){

        if(isVisibility && mInsideDrawable != null){
            mInsideView.setImageDrawable(mInsideDrawable);
            mInsideView.setVisibility(View.VISIBLE);
        }else{
            mInsideView.setVisibility(View.INVISIBLE);
        }
    }
    public void showFocusView(boolean isAnimStart) {

        if(!isAnimFinish)
            return;
        if (isAnimStart) {

            if(isAnimFinish && mLessenAnim != null){

                mOutAnimView.startAnimation(mLessenAnim);
            }
        } else {

            switchInsideVisibility(true);
            if (mOutsideFocusDrawable != null) {

                mOutAnimView.setImageDrawable(mOutsideFocusDrawable);
            } else if (mDefaultDrawable != null) {

                mOutAnimView.setImageDrawable(mDefaultDrawable);
            }
        }
    }

    public boolean isAnimFinish() {
        return isAnimFinish;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }
}
