package com.gome.pull.down.pulldown.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.pull.down.R;
import com.gome.pull.down.pulldown.listener.OnHeadRefeshListener;
import com.gome.pull.down.pulldown.loadlayout.HeadBaseLoadLayout;
import com.gome.pull.down.widget.utils.DpUtils;

public class GomeHeaderLoadLayout extends HeadBaseLoadLayout {
    private Context context;
    public ImageView imageViewNormal;
    private GomeBeanLayout imageViewRefeeshing;
    public TextView mTextView;
    public GomeHeaderLoadLayout(Context context) {
        super(context);
        this.init(context);
    }

    public GomeHeaderLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.imageViewNormal = (ImageView) this.findViewById(R.id.refresh_child_normal);
        this.imageViewRefeeshing = (GomeBeanLayout) this.findViewById(R.id.refresh_child_refeeshing);
        this.mTextView = (TextView) this.findViewById(R.id.text_refresh);
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

    @Override
    public void setOnHeadRefeshListener(OnHeadRefeshListener listener) {

        imageViewRefeeshing.setOnHeadRefeshListener(listener);
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
        this.imageViewRefeeshing.setBeanText("+100");
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
        this.imageViewRefeeshing.clearAnim();
        this.imageViewRefeeshing.setVisibility(GONE);
    }

//    @Override
//    public void onDownUpdata(float curentDistance, float maxDistance) {
//        if(curentDistance > 0){
//            clearPullViewAnimation();
//        }
//        if(curentDistance > 0 && curentDistance < 500){
//
//            imageViewNormal.setImageResource(android.R.color.holo_green_dark);
//        }else if(curentDistance < 900){
//
//            imageViewNormal.setImageResource(android.R.color.holo_blue_bright);
//        }else{
//            imageViewNormal.setImageResource(android.R.color.holo_red_dark);
//        }
//    }

    @Override
    public boolean isAnimFinish() {

        return imageViewRefeeshing.isAnimFinish();
    }
}

