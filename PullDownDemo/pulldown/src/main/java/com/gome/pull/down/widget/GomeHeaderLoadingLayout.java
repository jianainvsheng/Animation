package com.gome.pull.down.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gome.pull.down.R;
import com.gome.pull.down.widget.utils.DpUtils;

import static android.R.attr.id;

@SuppressLint({"InflateParams"})
public class GomeHeaderLoadingLayout extends LoadingLayout {
    private Context context;
    private ImageView imageViewNormal;
    private ImageView imageViewRefeeshing;

    public GomeHeaderLoadingLayout(Context context) {
        super(context);
        this.init(context);
    }

    public GomeHeaderLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.imageViewNormal = (ImageView) this.findViewById(R.id.refresh_child_normal);
        this.imageViewRefeeshing = (ImageView) this.findViewById(R.id.refresh_child_refeeshing);
    }

    public int getContentHeight() {
        return this.getChildAt(0).getMeasuredHeight();
    }

    protected View createLoadingView(RelativeLayout relativeLayout, Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.refresh_head1, (ViewGroup)null);
        if(null == container) {
            throw new NullPointerException("Loading view can not be null.");
        } else {
            RelativeLayout containerP = new RelativeLayout(context);
            LayoutParams containerParams = new LayoutParams(-1, -2);
            containerParams.addRule(13);
            containerP.addView(container, containerParams);
            LayoutParams params = new LayoutParams(-1, -2);
            params.addRule(12);
            relativeLayout.addView(containerP, params);
            relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            int rightPadding = DpUtils.dip2px(context, 0.0F);
            int topPadding = DpUtils.dip2px(context, 0.0F);
            int bottomPadding = DpUtils.dip2px(context, 0.0F);
            int leftPadding = DpUtils.dip2px(context, 0.0F);
            containerP.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
            return container;
        }
    }

    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    public void onPullDownY(float pullDownY) {
        if(this.mCurState == State.INIT) {
            this.imageViewNormal.setVisibility(VISIBLE);
            this.imageViewRefeeshing.setVisibility(GONE);
        } else if(this.mCurState == State.RELEASE_TO_REFRESH) {
            this.imageViewNormal.setVisibility(VISIBLE);
            this.imageViewRefeeshing.setVisibility(GONE);
        } else if(this.mCurState == State.REFRESHING) {
            this.imageViewNormal.setVisibility(GONE);
            this.imageViewRefeeshing.setVisibility(VISIBLE);
        } else if(this.mCurState == State.REFRESHSUCCESS) {
            this.imageViewNormal.setVisibility(VISIBLE);
            this.imageViewRefeeshing.setVisibility(GONE);
        } else if(this.mCurState == State.REFRESHFAIL) {
            this.imageViewNormal.setVisibility(VISIBLE);
            this.imageViewRefeeshing.setVisibility(GONE);
        } else if(this.mCurState == State.DONE) {
            this.imageViewNormal.setVisibility(VISIBLE);
            this.imageViewRefeeshing.setVisibility(GONE);
        }

    }

    protected void onInit() {
        this.imageViewNormal.setVisibility(VISIBLE);
        this.imageViewRefeeshing.setVisibility(GONE);
//        String normalUrl = String.format("res://%s/%d", new Object[]{this.context.getPackageName(), Integer.valueOf(drawable.gtheme_loading_header_first)});
//        String gifUrl = String.format("res://%s/%d", new Object[]{this.context.getPackageName(), Integer.valueOf(drawable.gtheme_loading_header)});
//        ImageUtils.with(this.context).loadListImage(normalUrl, this.imageViewNormal);
//        ImageUtils.with(this.context).loadListImage(gifUrl, this.imageViewRefeeshing);
    }

    protected void onReleaseToRefresh() {
        this.imageViewNormal.setVisibility(VISIBLE);
        this.imageViewRefeeshing.setVisibility(GONE);
    }

    protected void onRefreshing() {
        this.imageViewNormal.setVisibility(GONE);
        this.imageViewRefeeshing.setVisibility(VISIBLE);
    }

    protected void onRefreshSuccess() {
        this.imageViewNormal.setVisibility(VISIBLE);
        this.imageViewRefeeshing.setVisibility(GONE);
    }

    protected void onRefreshFail() {
        this.imageViewNormal.setVisibility(VISIBLE);
        this.imageViewRefeeshing.setVisibility(GONE);
    }

    public void clearPullViewAnimation() {
        this.imageViewNormal.setVisibility(VISIBLE);
        this.imageViewRefeeshing.setVisibility(GONE);
    }

    public void updata(float downY){

        if(downY > 0){
            clearPullViewAnimation();
        }
        if(downY > 0 && downY < 500){

            imageViewNormal.setImageResource(android.R.color.holo_green_dark);
        }else if(downY < 900){

            imageViewNormal.setImageResource(android.R.color.holo_blue_bright);
        }else{
            imageViewNormal.setImageResource(android.R.color.holo_red_dark);
        }
    }
}

