package com.gome.pull.down.pulldown.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gome.pull.down.R;
import com.gome.pull.down.pulldown.listener.OnHeadRefeshListener;

/**
 * Created by yangjian on 2017/10/30.
 */

public class GomeBeanLayout extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {

    /**
     * 美豆的图片
     */
    private ImageView mBeanImage;

    /**
     * 美豆的个数
     */
    private TextView mBeanText;

    /**
     * 美豆的描述
     */
    private TextView mBeanDes;

    private BeanLayout mBeanLayout;

    private ValueAnimator mAnimator;

    private int mTranslationXSize;

    private int mTranslationYSize;

    private OnHeadRefeshListener mListener;

    private final static int ANIMATOR_SIZE = 1000;

    private boolean isStartAnim = false;

    private int mBeanTextLeft = (int) getResources().getDimension(R.dimen.dp_6);

    private int mBeanTextRight = (int) getResources().getDimension(R.dimen.dp_6);

    private int mBeanDesRight = (int) getResources().getDimension(R.dimen.dp_6);

    private int mBeanImageLeft = (int) getResources().getDimension(R.dimen.dp_6);

    private int mBeanLayoutMargeTop = (int) getResources().getDimension(R.dimen.dp_6);

    private int mBeanLayoutMargeBottom = (int) getResources().getDimension(R.dimen.dp_6);

    public GomeBeanLayout(Context context) {
        this(context, null);
    }

    public GomeBeanLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GomeBeanLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mBeanLayout = new BeanLayout(getContext());
        mBeanLayout.setBackgroundColor(Color.YELLOW);
        mBeanText = new TextView(getContext());
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mBeanText.setLayoutParams(params);
        mBeanText.setTextSize(35);
        mBeanText.setTextColor(Color.BLACK);
        mBeanText.setText("+5");
        addView(mBeanLayout);
        addView(mBeanText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {

            getChildAt(i).measure(0, 0);
        }

        int mChildMaxSize = mBeanLayout.getMeasuredHeight() > mBeanText.getMeasuredHeight() ?
                mBeanLayout.getMeasuredHeight() : mBeanText.getMeasuredHeight();

        int height = getMeasuredHeight() > mChildMaxSize ? getMeasuredHeight() : mChildMaxSize;
        setMeasuredDimension(widthMeasureSpec, height);
    }

    public boolean isAnimFinish(){

        if(mAnimator == null ){
            return true;
        }

        return !mAnimator.isRunning();
    }

    public void setOnHeadRefeshListener (OnHeadRefeshListener listener){

        this.mListener = listener;
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mBeanLayout != null) {

            mBeanLayout.layout((getMeasuredWidth() - mBeanLayout.getMeasuredWidth()) / 2,
                    (getMeasuredHeight() - mBeanLayout.getMeasuredHeight()) / 2,
                    (getMeasuredWidth() + mBeanLayout.getMeasuredWidth()) / 2,
                    (getMeasuredHeight() + mBeanLayout.getMeasuredHeight()) / 2);
        }

        if (mBeanText != null) {

            mBeanText.layout(mBeanTextLeft + getBeanChildRight(mBeanImage),
                   -mBeanText.getMeasuredHeight(),
                    mBeanTextLeft + getBeanChildRight(mBeanImage) + mBeanText.getMeasuredWidth(),
                    0);
        }

        if (mBeanLayout != null && mBeanText != null) {

            mTranslationYSize = mBeanLayout.getBottom() - (mBeanLayout.getMeasuredHeight() - mBeanText.getMeasuredHeight()) / 2;
        }

        if(isStartAnim){
            isStartAnim = false;
            mBeanImage.layout(mBeanLayout.mImageLeft + mTranslationXSize,
                    mBeanLayout.mBeanImageTop,
                    mBeanLayout.mBeanImageRight + mTranslationXSize,
                    mBeanLayout.mBeanImageBottom);
            startAnim();
        }
    }

    public void setBeanText(String text) {

        if (!TextUtils.isEmpty(text)) {

            mBeanText.setText(text);
            isStartAnim = true;
            mBeanLayout.requestLayout();
        }
    }

    private void startAnim() {
        if(mAnimator != null && mAnimator.isRunning()){
            return;
        }
        clearAnim();
        mAnimator = ValueAnimator.ofInt(ANIMATOR_SIZE);
        mAnimator.setDuration(1200);
        mAnimator.addUpdateListener(this);
        if (mTranslationYSize != 0 && mTranslationXSize != 0){
            mAnimator.start();
        }
    }

    public void clearAnim(){

        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator.removeAllListeners();
            mAnimator = null;
        }
    }

    private int getBeanChildLeft(View view) {

        if (view != null && mBeanLayout.indexOfChild(view) >= 0) {

            return (mBeanLayout.getLeft() + view.getLeft());
        }
        return 0;
    }

    private int getBeanChildRight(View view) {

        if (view != null && mBeanLayout.indexOfChild(view) >= 0) {

            return (mBeanLayout.getLeft() + view.getRight());
        }
        return 0;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        int curentSize = (int) animation.getAnimatedValue();
        int beanImageAnimSize = ANIMATOR_SIZE / 3;
        int beanTextAnimSize = ANIMATOR_SIZE  - beanImageAnimSize;

        if (curentSize <= beanImageAnimSize) {

            animBeanImageUpdata(curentSize, beanImageAnimSize);
        }else{
            mBeanImage.layout(mBeanLayout.mImageLeft,
                    mBeanLayout.mBeanImageTop,
                    mBeanLayout.mBeanImageRight,
                    mBeanLayout.mBeanImageBottom);
            animBeanTextUpdata(curentSize - beanImageAnimSize,beanTextAnimSize);
            if(curentSize == ANIMATOR_SIZE){
                mBeanText.layout(mBeanText.getLeft(),-mBeanText.getMeasuredHeight() + mTranslationYSize,
                        mBeanText.getRight(),mTranslationYSize);
                if(mListener != null){

                    mListener.onHeadRefreshListener();
                }
            }
        }
    }

    private void animBeanTextUpdata(int curentSize, int countSize) {

        int countDownSize = countSize / 5;
        int otherDownYSize = countSize - countDownSize;
        int updownTranslationYSize = mTranslationYSize / 3 * 2;
        int numberAnim = getBeanTextAnimNumber(updownTranslationYSize / 3,0);
        if(curentSize <= countDownSize){
            int translationY = (int) (((float) curentSize / countDownSize) * mTranslationYSize);
            mBeanText.layout(mBeanText.getLeft(),-mBeanText.getMeasuredHeight() + translationY,
                    mBeanText.getRight(),translationY);
        }else{

            curentSize = curentSize - countDownSize;
            if(numberAnim > 0){
                int size = (int)((float)otherDownYSize / numberAnim);
                int curentPosition = getCurentPositionNumber(curentSize,size,numberAnim);
                if(curentPosition <= 0){
                    return;
                }
                int tYSize = (int)((float)updownTranslationYSize / Math.pow(2,curentPosition));
                curentSize = curentSize - (curentPosition - 1) * (otherDownYSize / numberAnim);
               // Log.d("anim","numberAnim : " + numberAnim + " ; curentPosition : "+ curentPosition+";tYSize : " + tYSize + " ; size : " + size + " ; curentSize : " + curentSize);
                bounceUpAnim(curentSize,size,tYSize);
            }
        }
    }

    private int getCurentPositionNumber(int curentSize,int everySize,int numberAnim){

        //int everySize = countSize / numberAnim;

        for(int i = 1 ; i <= numberAnim ; i ++){

            if(curentSize > 0 && curentSize < everySize * i){

                return i;
            }else if(curentSize > (numberAnim -1) * everySize && curentSize <= numberAnim * everySize){

                return numberAnim;
            }
        }
        return 0;
    }

    private int getBeanTextAnimNumber(int translationYSize,int number){

        if(translationYSize < 5){

            return number;
        }else{
            translationYSize = (int)((float)translationYSize/2);
            number++;
            return getBeanTextAnimNumber(translationYSize,number);
        }
    }

    private void bounceUpAnim(int curentSize, int countSize,int translationYSize){

        int everyCountSize = countSize/2;
        if(curentSize <= everyCountSize){

            int translationY = (int) (((float) curentSize / everyCountSize) * translationYSize);
            mBeanText.layout(mBeanText.getLeft(),-mBeanText.getMeasuredHeight() +mTranslationYSize -translationY,
                    mBeanText.getRight(),mTranslationYSize - translationY);
        }else{

            int translationY = (int) (((float) (curentSize - everyCountSize) / everyCountSize) * translationYSize);
            mBeanText.layout(mBeanText.getLeft(),-mBeanText.getMeasuredHeight() +mTranslationYSize - translationYSize +translationY,
                    mBeanText.getRight(),mTranslationYSize - translationYSize + translationY);
        }
    }
    private void animBeanImageUpdata(int curentSize, int countSize) {

        int translationX = mTranslationXSize -(int) (((float) curentSize / countSize) * mTranslationXSize);
        mBeanImage.layout(mBeanLayout.mImageLeft + translationX,
                mBeanLayout.mBeanImageTop,
                mBeanLayout.mBeanImageRight + translationX,
                mBeanLayout.mBeanImageBottom);
    }

    public class BeanLayout extends ViewGroup {

        private int mImageLeft,mBeanImageRight,
                mBeanImageTop,mBeanImageBottom;
        public BeanLayout(Context context) {
            super(context);

            ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            this.setLayoutParams(params);
            mBeanImage = new ImageView(getContext());
            ViewGroup.LayoutParams imageParams = new LayoutParams(50, 50);
            mBeanImage.setLayoutParams(imageParams);
            mBeanImage.setImageResource(R.drawable.pd_call_service_offline);
            addView(mBeanImage);

            mBeanDes = new TextView(getContext());
            ViewGroup.LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mBeanDes.setLayoutParams(textParams);
            mBeanDes.setTextSize(20);
            mBeanDes.setTextColor(Color.BLACK);
            mBeanDes.setText("今日已经完成签到");
            addView(mBeanDes);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            int width = 0;
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {

                getChildAt(i).measure(0, 0);
                width += getChildAt(i).getMeasuredWidth();
            }

            width += mBeanDesRight + mBeanImageLeft + mBeanTextLeft + mBeanTextRight;
            height = mBeanDes.getMeasuredHeight() > mBeanImage.getMeasuredHeight() ?
                    mBeanDes.getMeasuredHeight() : mBeanImage.getMeasuredHeight();
            height += mBeanLayoutMargeBottom + mBeanLayoutMargeTop;
            if (mBeanText != null) {

                width += mBeanText.getMeasuredWidth();
            }
            this.setMeasuredDimension(width, height);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int width = mBeanImageLeft;
            if (mBeanImage != null) {

                mBeanImage.layout(width, (this.getMeasuredHeight() - mBeanImage.getMeasuredHeight()) / 2, width + mBeanImage.getMeasuredWidth(), (this.getMeasuredHeight() + mBeanImage.getMeasuredHeight()) / 2);
                mImageLeft = mBeanImage.getLeft();
                mBeanImageRight = mBeanImage.getRight();
                mBeanImageTop = mBeanImage.getTop();
                mBeanImageBottom = mBeanImage.getBottom();
                width += mBeanImage.getMeasuredWidth();
            }

            if (mBeanText != null) {

                width += mBeanTextLeft + mBeanTextRight;
                width += mBeanText.getMeasuredWidth();
            }

            if (mBeanDes != null) {
                mBeanDes.layout(width, (this.getMeasuredHeight() - mBeanDes.getMeasuredHeight()) / 2, width + mBeanDes.getMeasuredWidth(), (this.getMeasuredHeight() + mBeanDes.getMeasuredHeight()) / 2);
            }

            if(mBeanImage != null && mBeanDes != null){

                mTranslationXSize = mBeanDes.getLeft() - mBeanImage.getRight();
            }
        }
    }
}
