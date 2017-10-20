package com.gome.bottom.anim.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gome.bottom.anim.R;

/**
 * Created by yangjian on 2017/10/17.
 */

public abstract class ZoomAnimationView extends RelativeLayout implements ValueAnimator.AnimatorUpdateListener, View.OnClickListener {

    /**
     * 外部的填充布局view
     */
    private ImageView mOutAnimView;

    /**
     * 里面包裹自适应布局view 居中
     */
    private ImageView mInsideView;

    private long durationMillis = 100l;

    private ValueAnimator mBoostAnim, mLessenAnim, mInsideAnim;

    private Drawable mOutsideFocusDrawable,
            mOutsideUnFocusDrawable,
            mInsideDrawable,
            mDefaultDrawable;

    private OnClickListener mClickListener;

    private int mValueSize = 100;

    private int mTopMargin;

    private int mBottomMargin;

    public ZoomAnimationView(Context context) {
        this(context, null);
    }

    public ZoomAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {

        if (l != this) {

            mClickListener = l;
        }
        super.setOnClickListener(this);
    }

    private void init(AttributeSet attrs) {

        mOutAnimView = new ImageView(getContext());
        LayoutParams outParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        outParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        outParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mOutAnimView.setLayoutParams(outParams);

        mInsideView = new ImageView(getContext());
        LayoutParams inParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        inParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        inParams.addRule(RelativeLayout.CENTER_VERTICAL);
        inParams.topMargin = mTopMargin;
        inParams.bottomMargin = mBottomMargin;
        mInsideView.setLayoutParams(inParams);

        addView(mOutAnimView);
        addView(mInsideView);
        if (attrs != null) {

            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ZoomAnimView);
            mOutsideFocusDrawable = typedArray.getDrawable(R.styleable.ZoomAnimView_image_out_focus);
            mOutsideUnFocusDrawable = typedArray.getDrawable(R.styleable.ZoomAnimView_image_out_unfocus);
            mInsideDrawable = typedArray.getDrawable(R.styleable.ZoomAnimView_image_inside);
            mDefaultDrawable = typedArray.getDrawable(R.styleable.ZoomAnimView_image_out_defalut);
            mBottomMargin = (int) typedArray.getDimension(R.styleable.ZoomAnimView_image_inside_bottom_margin, mBottomMargin);
            mTopMargin = (int) typedArray.getDimension(R.styleable.ZoomAnimView_image_inside_top_margin, mTopMargin);
            if (mOutsideFocusDrawable != null && mBoostAnim == null) {

                mBoostAnim = getIntValeAnimator(mValueSize);
            }

            if (mInsideDrawable != null && mInsideAnim == null) {

//                mInsideAnim = ValueAnimator.ofInt(0,mValueSize / 2);
//                mInsideAnim.setDuration(durationMillis);
//                mInsideAnim.addUpdateListener(this);
                mInsideAnim = getInsideViewAnim();
            }


            if (mOutsideUnFocusDrawable != null && mLessenAnim == null) {
                mLessenAnim = getIntValeAnimator(mValueSize);
            }
        }
        setOnClickListener(this);
        showUnFocusView();
    }

    private ValueAnimator getIntValeAnimator(int valueSize) {

        ValueAnimator animator = ValueAnimator.ofInt(0, valueSize);
        animator.setDuration(durationMillis);
        animator.addUpdateListener(this);
        return animator;
    }

    public void setOutsideFocusDrawable(Drawable mOutsideFocusDrawable) {

        this.mOutsideFocusDrawable = mOutsideFocusDrawable;
        if (mBoostAnim == null) {
            mBoostAnim = ValueAnimator.ofInt(0, mValueSize);
            mBoostAnim.setDuration(durationMillis);
            mBoostAnim.addUpdateListener(this);
        }
    }

    public void setOutsideUnFocusDrawable(Drawable mOutsideUnFocusDrawable) {
        this.mOutsideUnFocusDrawable = mOutsideUnFocusDrawable;
        if (mLessenAnim == null) {

            mLessenAnim = ValueAnimator.ofInt(0, mValueSize);
            mLessenAnim.setDuration(durationMillis);
            mLessenAnim.addUpdateListener(this);
        }
    }

    public int getValueSize() {
        return mValueSize;
    }

    public void setInsideDrawable(Drawable mInsideDrawable) {
        this.mInsideDrawable = mInsideDrawable;
        if (mInsideAnim == null) {

            mInsideAnim = getInsideViewAnim();
        }
    }

    public ValueAnimator getInsideViewAnim() {

        ValueAnimator insideAnim = ValueAnimator.ofInt(0, mValueSize);
        insideAnim.setDuration(durationMillis);
        insideAnim.addUpdateListener(this);
        return insideAnim;
    }

    public abstract void onInsideViewAnimPrepare(ImageView imageView,ValueAnimator insideAnim);

    public abstract void onInsideViewAnimUpdata(ImageView imageView, int curentInt,int totalInt);

    public void setmDefaultDrawable(Drawable mDefaultDrawable) {
        this.mDefaultDrawable = mDefaultDrawable;
    }

    @Override
    public void onClick(View v) {

        showFocusView(true);
        if (mClickListener != null) {

            mClickListener.onClick(this);
        }
    }

    public void showUnFocusView() {

        if (!isAnimFinish())
            return;
        switchInsideVisibility(false);
        if (mOutsideUnFocusDrawable != null) {

            mOutAnimView.setImageDrawable(mOutsideUnFocusDrawable);
        } else if (mDefaultDrawable != null) {

            mOutAnimView.setImageDrawable(mDefaultDrawable);
        }
    }

    public void switchInsideVisibility(boolean isVisibility) {

        switchInsideVisibility(isVisibility, 1.0f);
    }

    public void switchInsideVisibility(boolean isVisibility, float scale) {

        if (isVisibility && mInsideDrawable != null) {
            mInsideView.setImageDrawable(mInsideDrawable);
            mInsideView.setVisibility(View.VISIBLE);
            setScaleView(mInsideView, scale);
        } else {
            mInsideView.setVisibility(View.INVISIBLE);
        }
    }

    public void showFocusView(boolean isAnimStart) {
        if (!isAnimFinish())
            return;
        if (isAnimStart && mLessenAnim != null) {

            mLessenAnim.start();
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

        return !((mBoostAnim != null && mBoostAnim.isRunning()) ||
                (mInsideAnim != null && mInsideAnim.isRunning()) ||
                (mLessenAnim != null && mLessenAnim.isRunning()));
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Integer curentInt = (Integer) animation.getAnimatedValue();

        if (curentInt > 0) {

            updata(animation, curentInt);
        }
        if (curentInt == mValueSize) {

            animFinish(animation);
        }
    }

    private void updata(ValueAnimator animation, int curentInt) {

        float scaleSize = 0f;
        if (animation == mBoostAnim) {

            scaleSize = ((float) curentInt / mValueSize * 0.9f) + 0.1f;
            if (scaleSize > 0.5f && !mInsideAnim.isRunning()) {

                //switchInsideVisibility(true, 0);
                if(getInsideView() != null){

                    if(mInsideAnim == null){

                        mInsideAnim = getInsideViewAnim();
                    }
                    onInsideViewAnimPrepare(getInsideView(),mInsideAnim);
                }else{
                    switchInsideVisibility(false);
                }

                mInsideAnim.start();
            }
            setScaleView(mOutAnimView, scaleSize);

        } else if (animation == mInsideAnim) {

//            scaleSize = (float) curentInt / (mValueSize/2);
//            setScaleView(mInsideView,scaleSize);
            if(mInsideView != null){

                onInsideViewAnimUpdata(mInsideView, curentInt,mValueSize);
            }
        } else if (animation == mLessenAnim) {

            scaleSize = 1.0f - (float) curentInt / (mValueSize) * 0.9f;
            setScaleView(mOutAnimView, scaleSize);
        }
    }

    public ImageView getOutAnimView() {
        return mOutAnimView;
    }

    public ImageView getInsideView() {
        return mInsideView;
    }

    public void setScaleView(View view, float scale) {

        if (view == null)
            return;
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    private void animFinish(ValueAnimator animation) {
        if (animation == mLessenAnim) {
            if (mOutsideFocusDrawable != null) {
                mOutAnimView.setImageDrawable(mOutsideFocusDrawable);
            } else {
                if (mDefaultDrawable != null) {

                    mOutAnimView.setImageDrawable(mDefaultDrawable);
                }
            }
            if (mBoostAnim != null) {
                mBoostAnim.start();
            } else {
                setScaleView(mOutAnimView, 1.0f);
            }
        }
    }
}
