package com.gome.pull.down.pulldown.loadlayout;

import android.content.Context;
import android.util.AttributeSet;

import com.gome.pull.down.pulldown.listener.OnHeadRefeshListener;
import com.gome.pull.down.widget.LoadingLayout;

/**
 * Created by yangjian on 2017/10/30.
 */

public abstract class HeadBaseLoadLayout extends LoadingLayout{

    private OnHeadRefeshListener mListener;

    public HeadBaseLoadLayout(Context context) {
        super(context);
    }

    public HeadBaseLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadBaseLoadLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public abstract void clearPullViewAnimation();

    public boolean isAnimFinish(){

        return true;
    }

    public void setOnHeadRefeshListener(OnHeadRefeshListener listener){

        this.mListener = listener;
    }

    public OnHeadRefeshListener getHeadRefeshListener() {
        return mListener;
    }
}
