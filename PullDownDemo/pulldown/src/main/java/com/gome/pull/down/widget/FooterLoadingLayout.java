package com.gome.pull.down.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gome.pull.down.R;

@SuppressLint({"InflateParams"})
public class FooterLoadingLayout extends LoadingLayout {
    private ProgressWheel loadingView;
    private TextView loadStateTextView;
    private String mInitText;
    private String mReleaseToLoadText;

    public FooterLoadingLayout(Context context) {
        super(context);
        this.mInitText = "上拉加载";
        this.mReleaseToLoadText = "释放加载";
        this.init(context);
    }

    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this.mInitText = string.pr_df_pullup_to_load;
//        this.mReleaseToLoadText = string.pr_df_release_to_load;
        this.init(context);
    }

    private void init(Context context) {
        this.loadStateTextView = (TextView)this.findViewById(R.id.load_message);
        this.loadingView = (ProgressWheel)this.findViewById(R.id.loading_progress_bar);
    }

    protected View createLoadingView(RelativeLayout relativeLayout, Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.load_more,null);
        if(null == container) {
            throw new NullPointerException("Loading view can not be null.");
        } else {
            LayoutParams params = new LayoutParams(-1, -2);
            params.addRule(10);
            relativeLayout.addView(container, params);
            relativeLayout.setBackgroundColor(Color.parseColor("#d2d2d2"));
            return container;
        }
    }

    public int getContentHeight() {
        return this.getChildAt(0).getMeasuredHeight();
    }

    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    public void onPullUpY(float pullUpY) {
    }

    protected void onInit() {
        this.loadStateTextView.setText(this.mInitText);
    }

    protected void onReleaseToLoad() {
        this.loadStateTextView.setText(this.mReleaseToLoadText);
    }

    protected void onLoading() {
        this.loadingView.setVisibility(GONE);
        this.loadStateTextView.setText("加载");
    }

    protected void onLoadSuccess() {
        this.loadingView.setVisibility(GONE);
        this.loadStateTextView.setText("加载成功");
    }

    protected void onLoadFail() {
        this.loadingView.setVisibility(GONE);
        this.loadStateTextView.setText("加载失败");
    }

    public void onLoadWithMessage(String Message) {
        this.loadingView.setVisibility(GONE);
        this.loadStateTextView.setText(Message);
    }

    public void clearPullUpViewAnimation() {
        this.loadingView.setVisibility(GONE);
    }

    public void setmInitText(int mInitText) {
        //this.mInitText = mInitText;
    }

    public void setmReleaseToLoadText(int mReleaseToLoadText) {
       // this.mReleaseToLoadText = mReleaseToLoadText;
    }
}
