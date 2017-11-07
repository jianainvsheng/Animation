package com.gome.pull.down.pulldown;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gome.pull.down.R;
import com.gome.pull.down.pulldown.adapter.PullDownBaseAdapter;
import com.gome.pull.down.pulldown.listener.OnHeadRefeshListener;
import com.gome.pull.down.pulldown.loadlayout.FooterBaseLoadLayout;
import com.gome.pull.down.pulldown.loadlayout.HeadBaseLoadLayout;
import com.gome.pull.down.widget.ILoadingLayout;
import com.gome.pull.down.widget.listener.OnRefreshListener;
import com.gome.pull.down.widget.listener.OnRefreshStateChangeListener;
import com.gome.pull.down.widget.listener.Pullable;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangjian on 2017/10/27.
 */

public class PullDownLayout extends RelativeLayout implements OnHeadRefeshListener {

    private float xDistance;
    private float yDistance;
    private float xLast;
    private float yLast;
    public static final String TAG = PullDownLayout.class.getSimpleName();
    public static final int PULLDOWNFROMTOP = 1;
    public static final int PULLUPFROMBOTTOM = 2;
    public static final int BOTHALL = 3;
    public static final int BOTHNOT = 4;
    private ILoadingLayout.State state;
    public OnRefreshListener mListener;
    public OnRefreshStateChangeListener mRefreshStateChangeLister;
    private float downY;
    private float lastY;
    public float pullDownY;
    private float pullUpY;
    public static float refreshDist = 200.0F;
    public static float loadmoreDist = 200.0F;
    private PullDownLayout.MyTimer timer;
    public float MOVE_SPEED;
    private boolean isTouch;
    private float radio;
    private HeadBaseLoadLayout mHeaderLoading;
    private FooterBaseLoadLayout mFooterLoading;
    private View pullableView;
    private int mEvents;
    private boolean canPullDown;
    private boolean canPullUp;
    private boolean isLayout;
    private boolean isMeasure;
    private int mode;
    PullDownLayout.MyHandler updateHandler;
    private PullDownBaseAdapter mAdapter;

    public PullDownLayout(Context context) {
        super(context);
        this.state = ILoadingLayout.State.INIT;
        this.pullDownY = 0.0F;
        this.pullUpY = 0.0F;
        this.MOVE_SPEED = 8.0F;
        this.isTouch = false;
        this.radio = 1.0F;
        this.canPullDown = true;
        this.canPullUp = true;
        this.isLayout = false;
        this.isMeasure = false;
        this.mode = 1;
        initHandler();
        this.initAttrs(context, null);
    }

    public PullDownLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.state = ILoadingLayout.State.INIT;
        this.pullDownY = 0.0F;
        this.pullUpY = 0.0F;
        this.MOVE_SPEED = 8.0F;
        this.isTouch = false;
        this.radio = 1.0F;
        this.canPullDown = true;
        this.canPullUp = true;
        this.isLayout = false;
        this.isMeasure = false;
        this.mode = 1;
        initHandler();
        this.initAttrs(context, attrs);
    }

    public PullDownLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.state = ILoadingLayout.State.INIT;
        this.pullDownY = 0.0F;
        this.pullUpY = 0.0F;
        this.MOVE_SPEED = 8.0F;
        this.isTouch = false;
        this.radio = 1.0F;
        this.canPullDown = true;
        this.canPullUp = true;
        this.isLayout = false;
        this.isMeasure = false;
        this.mode = 1;
        initHandler();
        this.initAttrs(context, attrs);
    }

    private void initHandler() {
        this.updateHandler = new PullDownLayout.MyHandler(this.mRefreshStateChangeLister) {
            public void handleMessage(Message msg) {
                OnRefreshStateChangeListener listener = (OnRefreshStateChangeListener) this.mRefrshListener.get();
                PullDownLayout.this.MOVE_SPEED = (float) (8.0D + 5.0D * Math.tan(1.5707963267948966D / (double) PullDownLayout.this.getMeasuredHeight() * (double) (PullDownLayout.this.pullDownY + Math.abs(PullDownLayout.this.pullUpY))));
                if (!PullDownLayout.this.isTouch) {
                    if (PullDownLayout.this.state == ILoadingLayout.State.REFRESHING && PullDownLayout.this.pullDownY <= refreshDist) {
                        PullDownLayout.this.pullDownY = refreshDist;
                        PullDownLayout.this.timer.cancel();
                    } else if (PullDownLayout.this.state == ILoadingLayout.State.LOADING && -PullDownLayout.this.pullUpY <= loadmoreDist) {
                        PullDownLayout.this.pullUpY = -loadmoreDist;
                        PullDownLayout.this.timer.cancel();
                    }
                }

                if (PullDownLayout.this.pullDownY > 0.0F) {
                    PullDownLayout.this.pullDownY -= PullDownLayout.this.MOVE_SPEED;
                } else if (PullDownLayout.this.pullUpY < 0.0F) {
                    PullDownLayout.this.pullUpY = PullDownLayout.this.pullUpY + PullDownLayout.this.MOVE_SPEED;
                }

                if (PullDownLayout.this.pullDownY < 0.0F) {
                    if (listener != null) {
                        listener.onRefreshStateChange(PullDownLayout.this.pullDownY);
                    }

                    PullDownLayout.this.pullDownY = 0.0F;
                    PullDownLayout.this.mHeaderLoading.clearPullViewAnimation();
                    if (PullDownLayout.this.state != ILoadingLayout.State.REFRESHING && PullDownLayout.this.state != ILoadingLayout.State.LOADING) {
                        PullDownLayout.this.changeState(ILoadingLayout.State.INIT);
                    }

                    PullDownLayout.this.timer.cancel();
                    PullDownLayout.this.requestLayout();
                }

                if (PullDownLayout.this.pullUpY > 0.0F) {
                    PullDownLayout.this.pullUpY = 0.0F;
                    PullDownLayout.this.mFooterLoading.clearPullUpViewAnimation();
                    if (PullDownLayout.this.state != ILoadingLayout.State.REFRESHING && PullDownLayout.this.state != ILoadingLayout.State.LOADING) {
                        PullDownLayout.this.changeState(ILoadingLayout.State.INIT);
                    }

                    PullDownLayout.this.timer.cancel();
                }

                PullDownLayout.this.requestLayout();
                if (PullDownLayout.this.pullDownY + Math.abs(PullDownLayout.this.pullUpY) == 0.0F) {
                    PullDownLayout.this.timer.cancel();
                }

            }
        };

        this.timer = new PullDownLayout.MyTimer(this.updateHandler);
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.PullToRefresh);

            assert a != null;

            this.mode = a.getInteger(R.styleable.PullToRefresh_mode, 1);
            a.recycle();
        }

    }

    private void initView() {

        if (mAdapter != null) {
            clearAllLoadLayout();
            this.mHeaderLoading = mAdapter.getHeadLoadLayout();
            if (mHeaderLoading != null) {
                this.mHeaderLoading.setId(R.id.gomeheaderloadinglayout_id);
            }
            this.mFooterLoading = mAdapter.getFooterLoadLayout();
            if (mFooterLoading != null) {

                this.mFooterLoading.setId(R.id.footerloadinglayout_id);
            }
        }
    }

    public void setAdapter(PullDownBaseAdapter adapter) {

        this.mAdapter = adapter;
        initView();
        requestLayout();
    }

    private void clearAllLoadLayout() {

        if (mHeaderLoading != null && indexOfChild(mHeaderLoading) >= 0) {

            removeView(mHeaderLoading);
        }

        if (mFooterLoading != null && indexOfChild(mFooterLoading) >= 0) {

            removeView(mFooterLoading);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!this.isMeasure) {
            this.isMeasure = true;
            this.pullableView = this.getChildAt(0);

            if (mHeaderLoading != null && indexOfChild(mHeaderLoading) < 0) {
                LayoutParams paramsHeader = new LayoutParams(-1, -1);
                this.addView(this.mHeaderLoading, paramsHeader);
            }

            if (mFooterLoading != null && indexOfChild(mFooterLoading) < 0) {
                LayoutParams paramsFooter = new LayoutParams(-1, -1);
                this.addView(this.mFooterLoading, paramsFooter);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!this.isLayout) {
            this.isLayout = true;
            refreshDist = mHeaderLoading == null ? 0 : (float) this.mHeaderLoading.getContentHeight();
            loadmoreDist = mFooterLoading == null ? 0 : (float) this.mFooterLoading.getContentHeight();
        }
        if (mHeaderLoading != null && indexOfChild(mHeaderLoading) >= 0) {

            this.mHeaderLoading.layout(0, (int) (this.pullDownY + this.pullUpY) - this.mHeaderLoading.getMeasuredHeight(), this.mHeaderLoading.getMeasuredWidth(), (int) (this.pullDownY + this.pullUpY));
        }
        this.pullableView.layout(0, (int) (this.pullDownY + this.pullUpY), this.pullableView.getMeasuredWidth(), (int) (this.pullDownY + this.pullUpY) + this.pullableView.getMeasuredHeight());

        if (mFooterLoading != null && indexOfChild(mFooterLoading) >= 0) {

            this.mFooterLoading.layout(0, (int) (this.pullDownY + this.pullUpY) + this.pullableView.getMeasuredHeight(), this.mFooterLoading.getMeasuredWidth(), (int) (this.pullDownY + this.pullUpY) + this.pullableView.getMeasuredHeight() + this.mFooterLoading.getMeasuredHeight());
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.pullableView instanceof Pullable) {

            if(!mHeaderLoading.isAnimFinish()){
                return true;
            }
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    this.xDistance = this.yDistance = 0.0F;
                    this.xLast = ev.getX();
                    this.yLast = ev.getY();
                    this.downY = ev.getY();
                    this.lastY = this.downY;
                    if (timer != null)
                        this.timer.cancel();
                    this.mEvents = 0;
                    this.releasePull();
                    break;
                case MotionEvent.ACTION_UP:
                    if (this.pullDownY > refreshDist || -this.pullUpY > loadmoreDist) {
                        this.isTouch = false;
                    }

                    if (this.state == ILoadingLayout.State.RELEASE_TO_REFRESH) {
                        this.changeState(ILoadingLayout.State.REFRESHING);
                        if (this.mListener != null) {
                            this.mListener.onRefresh();
                        }
                    } else if (this.state == ILoadingLayout.State.RELEASE_TO_LOAD) {
                        this.changeState(ILoadingLayout.State.LOADING);
                        if (this.mListener != null) {
                            this.mListener.onLoadMore();
                        }
                    }
                    this.hide();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float curX = ev.getX();
                    float curY = ev.getY();
                    this.xDistance += Math.abs(curX - this.xLast);
                    this.yDistance += Math.abs(curY - this.yLast);
                    this.xLast = curX;
                    this.yLast = curY;
                    if (this.mEvents == 0 && (double) this.yDistance > 1.5D * (double) this.xDistance) {
                        if (this.pullDownY <= 0.0F && (!((Pullable) this.pullableView).canPullDown() || !this.canPullDown || this.state == ILoadingLayout.State.LOADING)) {
                            if (this.pullUpY >= 0.0F && (!((Pullable) this.pullableView).canPullUp() || !this.canPullUp || this.state == ILoadingLayout.State.REFRESHING)) {
                                this.releasePull();
                            } else {

                                if(mFooterLoading == null || indexOfChild(mFooterLoading) < 0){
                                    releasePull();
                                }else{
                                    this.pullUpY += (ev.getY() - this.lastY) / this.radio;
                                    this.mFooterLoading.onPullUpY(this.pullUpY);
                                    if (this.pullUpY > 0.0F) {
                                        this.pullUpY = 0.0F;
                                        if (this.mode == 3) {
                                            this.canPullDown = true;
                                            this.canPullUp = false;
                                        }
                                    }

                                    if (this.pullUpY < (float) (-this.getMeasuredHeight())) {
                                        this.pullUpY = (float) (-this.getMeasuredHeight());
                                    }

                                    if (this.state == ILoadingLayout.State.LOADING) {
                                        this.isTouch = true;
                                    }
                                }
                            }
                        } else {

                            if(mHeaderLoading == null || indexOfChild(mHeaderLoading) < 0)
                                this.releasePull();
                            else{

                                this.pullDownY += (ev.getY() - this.lastY) / this.radio;
                                this.mHeaderLoading.onPullDownY(ev.getY() - this.downY);
                                if (this.pullDownY < 0.0F) {
                                    this.pullDownY = 0.0F;
                                    if (this.mode == 3) {
                                        this.canPullDown = false;
                                        this.canPullUp = true;
                                    }
                                }

                                if (mAdapter != null) {

                                    mAdapter.onDownUpdata(this,mHeaderLoading,pullDownY, (float) this.getMeasuredHeight());
                                }
                                if (this.pullDownY > (float) this.getMeasuredHeight()) {
                                    this.pullDownY = (float) this.getMeasuredHeight();
                                }

                                if (this.state == ILoadingLayout.State.REFRESHING) {
                                    this.isTouch = true;
                                }
                            }
                        }
                    } else {
                        this.mEvents = 0;
                    }

                    this.lastY = ev.getY();
                    this.radio = (float) (2.0D + 2.0D * Math.tan(1.5707963267948966D / (double) this.getMeasuredHeight() * (double) (this.pullDownY + Math.abs(this.pullUpY))));
                    if (this.pullDownY > 0.0F || this.pullUpY < 0.0F) {
                        this.requestLayout();
                    }

                    if (this.pullDownY > 0.0F) {
                        if (this.pullDownY <= refreshDist && (this.state == ILoadingLayout.State.RELEASE_TO_REFRESH || this.state == ILoadingLayout.State.DONE)) {
                            this.changeState(ILoadingLayout.State.INIT);
                        }

                        if (this.pullDownY >= refreshDist && this.state == ILoadingLayout.State.INIT) {
                            this.changeState(ILoadingLayout.State.RELEASE_TO_REFRESH);
                        }
                    } else if (this.pullUpY < 0.0F) {
                        if (-this.pullUpY <= loadmoreDist && (this.state == ILoadingLayout.State.RELEASE_TO_LOAD || this.state == ILoadingLayout.State.DONE)) {
                            this.changeState(ILoadingLayout.State.INIT);
                        }

                        if (-this.pullUpY >= loadmoreDist && this.state == ILoadingLayout.State.INIT) {
                            this.changeState(ILoadingLayout.State.RELEASE_TO_LOAD);
                        }
                    }

                    if (this.pullDownY + Math.abs(this.pullUpY) > 8.0F) {
                        ev.setAction(3);
                    }

                    if (this.mRefreshStateChangeLister != null) {
                        this.mRefreshStateChangeLister.onRefreshStateChange(this.pullDownY);
                    }
                case 3:
                case 4:
                default:
                    break;
                case 5:
                case 6:
                    this.mEvents = -1;
            }
        }

        super.dispatchTouchEvent(ev);
        return true;
    }

    public void changeState(ILoadingLayout.State to) {
        if ((to == ILoadingLayout.State.INIT || to == ILoadingLayout.State.DONE) && this.mRefreshStateChangeLister != null) {
            this.mRefreshStateChangeLister.onRefreshStateChange(-1.0F);
        }

        this.state = to;
        this.mHeaderLoading.setState(to);
        this.mFooterLoading.setState(to);
    }

    public void refreshFinish(final ILoadingLayout.State refreshResult) {

        if(mHeaderLoading != null){

            mHeaderLoading.setOnHeadRefeshListener(this);
        }
        (new Handler() {
            public void handleMessage(Message msg) {
                PullDownLayout.this.changeState(refreshResult);
                PullDownLayout.this.changeState(ILoadingLayout.State.DONE);

                //PullDownLayout.this.hide();
            }
        }).sendEmptyMessageDelayed(0, 1000L);
    }

    @Override
    public void onHeadRefreshListener() {

        this.hide();
        if(mHeaderLoading != null){

            mHeaderLoading.setOnHeadRefeshListener(null);
        }
    }

    public void loadmoreFinish(ILoadingLayout.State refreshResult, boolean isHasMore, String loadMessage) {
        this.changeState(refreshResult);
        this.changeState(ILoadingLayout.State.DONE);
        this.hide();
        if (loadMessage != null) {
            this.mFooterLoading.onLoadWithMessage(loadMessage);
        }

    }

    private void hide() {

        if(this.timer != null){

            this.timer.schedule(5L);
        }
    }

    private void releasePull() {
        switch (this.mode) {
            case 1:
                this.canPullDown = true;
                this.canPullUp = false;
                break;
            case 2:
                this.canPullDown = false;
                this.canPullUp = true;
                break;
            case 3:
                this.canPullDown = true;
                this.canPullUp = true;
                break;
            case 4:
                this.canPullDown = false;
                this.canPullUp = false;
        }

    }

    public void autoRefresh() {
        PullDownLayout.AutoRefreshAndLoadTask task = new PullDownLayout.AutoRefreshAndLoadTask();
        task.execute(new Integer[]{Integer.valueOf(20)});
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public void setRefreshMode(int mode) {
        this.mode = mode;
    }

    public void setOnRefreshStateChangeListener(OnRefreshStateChangeListener listener) {
        this.mRefreshStateChangeLister = listener;
    }

    public void setRefrshModePulldownfromtop() {
        this.setRefreshMode(1);
    }

    public void setRefrshModeBothnot() {
        this.setRefreshMode(4);
    }

    public void setmInitText(int mInitText) {
        if (this.mFooterLoading != null) {
            this.mFooterLoading.setmInitText(mInitText);
        }

    }

    public void setmReleaseToLoadText(int mReleaseToLoadText) {
        if (this.mFooterLoading != null) {
            this.mFooterLoading.setmReleaseToLoadText(mReleaseToLoadText);
        }

    }

    private static class MyTimer {
        private PullDownLayout.MyHandler handler;
        private Timer timer;
        private PullDownLayout.MyTimer.MyTask mTask;

        public boolean isCancel = true;

        public MyTimer(PullDownLayout.MyHandler handler) {
            this.handler = handler;
            this.timer = new Timer();
        }

        public void schedule(long period) {
            Log.d(TAG, "执行schedule:");
            if (this.mTask != null) {
                this.mTask.cancel();
                this.mTask = null;
                isCancel = true;
            }

            this.mTask = new PullDownLayout.MyTimer.MyTask(this.handler);
            this.timer.schedule(this.mTask, 0L, period);
            isCancel = false;
        }

        public void cancel() {
            if (this.mTask != null) {
                this.mTask.cancel();
                this.mTask = null;
                isCancel = true;
            }
        }

        public boolean isCancel() {

            return isCancel;
        }

        private static class MyTask extends TimerTask {
            protected final WeakReference<PullDownLayout.MyHandler> handlerWeakReference;

            public MyTask(PullDownLayout.MyHandler handler) {
                this.handlerWeakReference = new WeakReference(handler);
            }

            public void run() {
                this.handlerWeakReference.get().obtainMessage().sendToTarget();
            }
        }
    }

    private class AutoRefreshAndLoadTask extends AsyncTask<Integer, Float, String> {
        private AutoRefreshAndLoadTask() {
        }

        protected String doInBackground(Integer... params) {
            while (PullDownLayout.this.pullDownY < 1.0F * refreshDist) {
                PullDownLayout.this.pullDownY += PullDownLayout.this.MOVE_SPEED;
                this.publishProgress(new Float[]{Float.valueOf(PullDownLayout.this.pullDownY)});

                try {
                    Thread.sleep((long) params[0].intValue());
                } catch (InterruptedException var3) {
                    var3.printStackTrace();
                }
            }

            return null;
        }

        protected void onPostExecute(String result) {
            PullDownLayout.this.changeState(ILoadingLayout.State.REFRESHING);
            if (PullDownLayout.this.mListener != null) {
                PullDownLayout.this.mListener.onRefresh();
                PullDownLayout.this.refreshFinish(ILoadingLayout.State.REFRESHSUCCESS);
            }

            PullDownLayout.this.hide();
        }

        protected void onProgressUpdate(Float... values) {
            if (PullDownLayout.this.pullDownY > refreshDist) {
                PullDownLayout.this.changeState(ILoadingLayout.State.RELEASE_TO_REFRESH);
            }

            PullDownLayout.this.requestLayout();
        }
    }

    private static class MyHandler extends Handler {
        public final WeakReference<OnRefreshStateChangeListener> mRefrshListener;

        public MyHandler(OnRefreshStateChangeListener listener) {
            this.mRefrshListener = new WeakReference(listener);
        }
    }

}
