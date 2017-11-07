package com.gome.pull.down.pulldown.loadlayout;

import android.content.Context;
import android.util.AttributeSet;

import com.gome.pull.down.widget.LoadingLayout;

/**
 * Created by yangjian on 2017/10/30.
 */

public abstract class FooterBaseLoadLayout extends LoadingLayout{

    public FooterBaseLoadLayout(Context context) {
        super(context);
    }

    public FooterBaseLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterBaseLoadLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public abstract void clearPullUpViewAnimation();

    public abstract void setmInitText(int resId);

    public abstract void setmReleaseToLoadText(int resId);

    public abstract void onLoadWithMessage(String loadMessage);

}
