package com.gome.pull.down.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
public abstract class LoadingLayout extends RelativeLayout implements ILoadingLayout {
    protected State mCurState;
    private State mPreState;

    public LoadingLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurState = State.INIT;
        this.mPreState = State.INIT;
        this.init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        this.createLoadingView(this, context, attrs);
    }

    public void setState(State state) {
        if(this.mCurState != state) {
            this.mPreState = this.mCurState;
            this.mCurState = state;
            this.onStateChanged(state, this.mPreState);
        }

    }

    public State getState() {
        return this.mCurState;
    }

    protected State getPreState() {
        return this.mPreState;
    }

    public void onPullDownY(float downY, float moveY) {
    }

    public void onPullDownY(float pullDownY) {
    }

    public void onPullUpY(float pullUpY) {
    }

    protected void onStateChanged(State curState, State oldState) {
        switch(curState.ordinal()) {
            case 1:
                this.onInit();
                break;
            case 2:
                this.onReleaseToRefresh();
                break;
            case 3:
                this.onRefreshing();
                break;
            case 4:
                this.onRefreshSuccess();
                break;
            case 5:
                this.onRefreshFail();
                break;
            case 6:
                this.onReleaseToLoad();
                break;
            case 7:
                this.onLoading();
                break;
            case 8:
                this.onLoadSuccess();
                break;
            case 9:
                this.onLoadFail();
                break;
            case 10:
                this.onDone();
        }

    }

    protected void onInit() {
    }

    protected void onReleaseToRefresh() {
    }

    protected void onRefreshing() {
    }

    protected void onRefreshSuccess() {
    }

    protected void onRefreshFail() {
    }

    protected void onReleaseToLoad() {
    }

    protected void onLoading() {
    }

    protected void onLoadSuccess() {
    }

    protected void onLoadFail() {
    }

    protected void onDone() {
    }

    public abstract int getContentHeight();

    protected abstract View createLoadingView(RelativeLayout var1, Context var2, AttributeSet var3);
}

