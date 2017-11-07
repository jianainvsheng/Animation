package com.gome.pull.down.pulldown.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.gome.pull.down.pulldown.PullDownLayout;
import com.gome.pull.down.widget.ILoadingLayout;
import com.gome.pull.down.widget.listener.OnRefreshListener;
import com.gome.pull.down.widget.listener.Pullable;
import com.gome.pull.down.widget.utils.NetUtility;

public class GomePullableListView extends ListView implements Pullable, OnScrollListener {
    private boolean isLazy = true;
    private boolean hasMore = false;
    private boolean isNeedHasMore = true;
    private boolean isLoadding = false;
    private boolean isNeedNet = true;
    private Button goBackTop;
    private Context mContext;
    private GomePullableListView.OnMyListViewScrollListener mOnMyListViewScrollListener;
    private OnRefreshListener onLoadMoreListener;
    private int scrollY = 0;
    private boolean mIsFooterReady = false;
    private final int SCROLL_LIST_ITEM_ID = 4;
    private boolean isHasFooter = true;
    private boolean scrollFlag = false;
    private GomePullableListView.PullableScrollCallBack pullableScrollCallBack;

    public GomePullableListView(Context context) {
        super(context);
        this.initView(context);
    }

    public GomePullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public GomePullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initView(context);
    }

    public void initView(Context context) {
        this.mContext = context;
        this.setOnScrollListener(this);
    }

    public void setAdapter(ListAdapter adapter) {
        if(!this.mIsFooterReady && this.isHasFooter) {
            this.mIsFooterReady = true;
        }

        super.setAdapter(adapter);
    }

    public boolean canPullDown() {
        return this.getCount() == 0?true:(this.getChildAt(0) == null?true:this.getFirstVisiblePosition() == 0 && this.getChildAt(0).getTop() >= 0);
    }

    public boolean canPullUp() {
        boolean result=false;
        if (getLastVisiblePosition() == (getCount() - 1)) {
            final View bottomChildView = getChildAt(getLastVisiblePosition() - getFirstVisiblePosition());
            result= (getHeight()>=bottomChildView.getBottom());
            Log.d("gomeplusbottom","**************************");
            Log.d("gomeplusbottom","botttom : " + bottomChildView.getBottom());
            Log.d("gomeplusbottom","getHeight() : " + getHeight());
            Log.d("gomeplusbottom","**************************");
        };
        return  result;
    }

    public void selfPullUp(float pullUpDistace) {
    }

    public void handleLoad() {
        if(this.hasMore) {
            if(this.onLoadMoreListener != null) {
                this.isLoadding = true;
                if(this.isNeedNet && !NetUtility.isNetworkAvailable(this.mContext)) {
                    this.onLoadMoreNoNet();
                } else {
                    this.onLoadMoreListener.onLoadMore();
                }
            } else {
                this.isLoadding = false;
            }
        } else {
            this.isLoadding = false;
        }

    }

    public boolean isHasMore() {
        return this.hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setIsNeedNet(boolean isNeedNet) {
        this.isNeedNet = isNeedNet;
    }

    public void setIsLazy(boolean isLazy) {
        this.isLazy = isLazy;
    }

    public void setGoBackTop(Button btnGoBackTop) {
        this.goBackTop = btnGoBackTop;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = false;

        try {
            result = super.dispatchTouchEvent(ev);
        } catch (Exception var4) {
            ;
        }

        int y = (int)ev.getY();
        switch(ev.getAction()) {
            case 0:
                this.scrollY = y;
                break;
            case 1:
                this.scrollY = -1;
                break;
            case 2:
                if(this.scrollY == -1) {
                    this.scrollY = y;
                } else if(y > this.scrollY) {
                    if(y - this.scrollY > 40 && super.getFirstVisiblePosition() > 0 && this.pullableScrollCallBack != null) {
                        this.pullableScrollCallBack.scrollDown();
                    }
                } else if(this.scrollY - y > 40 && this.pullableScrollCallBack != null) {
                    this.pullableScrollCallBack.scrollUp();
                }

                this.scrollY = y;
        }

        return result;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if(this.isLazy) {
//            ImageUtils.frescoLazyLoad(scrollState, true, true);
//        }

        if(this.mOnMyListViewScrollListener != null) {
            this.mOnMyListViewScrollListener.onScrollStateChanged(view, scrollState);
        }

        if(!this.isLoadding) {
            switch(scrollState) {
                case 0:
                    this.scrollFlag = false;
                    if(view.getLastVisiblePosition() == view.getCount() - 1 && this.goBackTop != null) {
                        this.goBackTop.setVisibility(VISIBLE);
                    }

                    if(this.getFirstVisiblePosition() == 0 && this.goBackTop != null) {
                        this.goBackTop.setVisibility(GONE);
                    }

                    if(this.getLastVisiblePosition() != -1 && this.getLastVisiblePosition() == this.getCount() - 1) {
                        this.handleLoad();
                    } else {
                        this.isLoadding = false;
                    }
                    break;
                case 1:
                    this.scrollFlag = true;
                    break;
                case 2:
                    this.scrollFlag = true;
            }

        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(this.mOnMyListViewScrollListener != null) {
            this.mOnMyListViewScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if(this.goBackTop != null && this.scrollFlag) {
            boolean isShowArrow = firstVisibleItem >= 4;
            this.goBackTop.setVisibility(isShowArrow?VISIBLE:GONE);
        }

    }

    public void onRefreshComplete() {
        if(this.getParent() instanceof PullDownLayout) {
            PullDownLayout pullToRefreshLayout = (PullDownLayout)this.getParent();
            pullToRefreshLayout.refreshFinish(ILoadingLayout.State.REFRESHSUCCESS);
        }

        this.setFooterView();
    }

    public void onLoadMoreNoNet() {
        this.onPullToRefreshLayouLoadFinish(false);
    }

    public void onPullToRefreshLayouLoadFinish(boolean isLoadSucess) {
        this.isLoadding = false;
        if(this.getParent() instanceof PullDownLayout) {
            PullDownLayout pullToRefreshLayout = (PullDownLayout)this.getParent();
            if(isLoadSucess) {
                pullToRefreshLayout.loadmoreFinish(ILoadingLayout.State.LOADSUCCESS, this.hasMore, "");
            } else {
                pullToRefreshLayout.loadmoreFinish(ILoadingLayout.State.LOADFAIL, this.hasMore, "");
            }
        }

    }

    public void setOnScrollListener(GomePullableListView.OnMyListViewScrollListener listener) {
        this.mOnMyListViewScrollListener = listener;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onLoadMoreListener = onRefreshListener;
        if(this.getParent() instanceof PullDownLayout) {
            PullDownLayout pullToRefreshLayout = (PullDownLayout)this.getParent();
            pullToRefreshLayout.setOnRefreshListener(onRefreshListener);
        }

    }

    private void setFooterView() {
        if(this.hasMore && this.isHasFooter) {
            if(this.isNeedNet && !NetUtility.isNetworkAvailable(this.mContext)) {
                this.onLoadMoreNoNet();
            }
        }
    }

    public void setNeedHasMore(boolean needHasMore) {
        this.isNeedHasMore = needHasMore;
    }

    public void setPullableScrollCallBack(GomePullableListView.PullableScrollCallBack pullableScrollCallBack) {
        this.pullableScrollCallBack = pullableScrollCallBack;
    }

    public interface PullableScrollCallBack {
        void scrollUp();

        void scrollDown();
    }

    public interface OnMyListViewScrollListener {
        void onScroll(AbsListView var1, int var2, int var3, int var4);

        void onScrollStateChanged(AbsListView var1, int var2);
    }
}

