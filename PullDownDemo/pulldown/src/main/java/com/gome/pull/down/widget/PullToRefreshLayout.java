package com.gome.pull.down.widget;

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
import com.gome.pull.down.widget.listener.OnRefreshListener;
import com.gome.pull.down.widget.listener.OnRefreshStateChangeListener;
import com.gome.pull.down.widget.listener.Pullable;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.id;

public class PullToRefreshLayout extends RelativeLayout {
    private float xDistance;
    private float yDistance;
    private float xLast;
    private float yLast;
    public static final String TAG = PullToRefreshLayout.class.getSimpleName();
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
    private PullToRefreshLayout.MyTimer timer;
    public float MOVE_SPEED;
    private boolean isTouch;
    private float radio;
    private GomeHeaderLoadingLayout mHeaderLoading;
    private FooterLoadingLayout mFooterLoading;
    private View pullableView;
    private int mEvents;
    private boolean canPullDown;
    private boolean canPullUp;
    private boolean isLayout;
    private boolean isMeasure;
    private int mode;
    private Context mContext;
    PullToRefreshLayout.MyHandler updateHandler;

    public PullToRefreshLayout(Context context) {
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
        this.updateHandler = new PullToRefreshLayout.MyHandler(this.mRefreshStateChangeLister) {
            public void handleMessage(Message msg) {
                OnRefreshStateChangeListener listener = (OnRefreshStateChangeListener)this.mRefrshListener.get();
                Log.d(PullToRefreshLayout.TAG, "接受消息:" + PullToRefreshLayout.this.pullDownY);
                PullToRefreshLayout.this.MOVE_SPEED = (float)(8.0D + 5.0D * Math.tan(1.5707963267948966D / (double)PullToRefreshLayout.this.getMeasuredHeight() * (double)(PullToRefreshLayout.this.pullDownY + Math.abs(PullToRefreshLayout.this.pullUpY))));
                if(!PullToRefreshLayout.this.isTouch) {
                    if(PullToRefreshLayout.this.state == ILoadingLayout.State.REFRESHING && PullToRefreshLayout.this.pullDownY <= PullToRefreshLayout.refreshDist) {
                        PullToRefreshLayout.this.pullDownY = PullToRefreshLayout.refreshDist;
                        PullToRefreshLayout.this.timer.cancel();
                    } else if(PullToRefreshLayout.this.state == ILoadingLayout.State.LOADING && -PullToRefreshLayout.this.pullUpY <= PullToRefreshLayout.loadmoreDist) {
                        PullToRefreshLayout.this.pullUpY = -PullToRefreshLayout.loadmoreDist;
                        PullToRefreshLayout.this.timer.cancel();
                    }
                }

                if(PullToRefreshLayout.this.pullDownY > 0.0F) {
                    PullToRefreshLayout.this.pullDownY -= PullToRefreshLayout.this.MOVE_SPEED;
                } else if(PullToRefreshLayout.this.pullUpY < 0.0F) {
                    PullToRefreshLayout.this.pullUpY = PullToRefreshLayout.this.pullUpY + PullToRefreshLayout.this.MOVE_SPEED;
                }

                if(PullToRefreshLayout.this.pullDownY < 0.0F) {
                    if(listener != null) {
                        listener.onRefreshStateChange(PullToRefreshLayout.this.pullDownY);
                    }

                    PullToRefreshLayout.this.pullDownY = 0.0F;
                    PullToRefreshLayout.this.mHeaderLoading.clearPullViewAnimation();
                    if(PullToRefreshLayout.this.state != ILoadingLayout.State.REFRESHING && PullToRefreshLayout.this.state != ILoadingLayout.State.LOADING) {
                        PullToRefreshLayout.this.changeState(ILoadingLayout.State.INIT);
                    }

                    PullToRefreshLayout.this.timer.cancel();
                    PullToRefreshLayout.this.requestLayout();
                }

                if(PullToRefreshLayout.this.pullUpY > 0.0F) {
                    PullToRefreshLayout.this.pullUpY = 0.0F;
                    PullToRefreshLayout.this.mFooterLoading.clearPullUpViewAnimation();
                    if(PullToRefreshLayout.this.state != ILoadingLayout.State.REFRESHING && PullToRefreshLayout.this.state != ILoadingLayout.State.LOADING) {
                        PullToRefreshLayout.this.changeState(ILoadingLayout.State.INIT);
                    }

                    PullToRefreshLayout.this.timer.cancel();
                }

                PullToRefreshLayout.this.requestLayout();
                if(PullToRefreshLayout.this.pullDownY + Math.abs(PullToRefreshLayout.this.pullUpY) == 0.0F) {
                    PullToRefreshLayout.this.timer.cancel();
                }

            }
        };
        this.initAttrs(context, (AttributeSet)null);
        this.initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
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
        this.updateHandler = new PullToRefreshLayout.MyHandler(this.mRefreshStateChangeLister) {
            public void handleMessage(Message msg) {
                OnRefreshStateChangeListener listener = (OnRefreshStateChangeListener)this.mRefrshListener.get();
                Log.d(PullToRefreshLayout.TAG, "接受消息:" + PullToRefreshLayout.this.pullDownY);
                PullToRefreshLayout.this.MOVE_SPEED = (float)(8.0D + 5.0D * Math.tan(1.5707963267948966D / (double)PullToRefreshLayout.this.getMeasuredHeight() * (double)(PullToRefreshLayout.this.pullDownY + Math.abs(PullToRefreshLayout.this.pullUpY))));
                if(!PullToRefreshLayout.this.isTouch) {
                    if(PullToRefreshLayout.this.state == ILoadingLayout.State.REFRESHING && PullToRefreshLayout.this.pullDownY <= PullToRefreshLayout.refreshDist) {

                        PullToRefreshLayout.this.pullDownY = PullToRefreshLayout.refreshDist;
                        PullToRefreshLayout.this.timer.cancel();
                    } else if(PullToRefreshLayout.this.state == ILoadingLayout.State.LOADING && -PullToRefreshLayout.this.pullUpY <= PullToRefreshLayout.loadmoreDist) {
                        PullToRefreshLayout.this.pullUpY = -PullToRefreshLayout.loadmoreDist;
                        PullToRefreshLayout.this.timer.cancel();
                    }
                }

                if(PullToRefreshLayout.this.pullDownY > 0.0F) {
                    PullToRefreshLayout.this.pullDownY -= PullToRefreshLayout.this.MOVE_SPEED;
                } else if(PullToRefreshLayout.this.pullUpY < 0.0F) {
                    PullToRefreshLayout.this.pullUpY = PullToRefreshLayout.this.pullUpY + PullToRefreshLayout.this.MOVE_SPEED;
                }

                if(PullToRefreshLayout.this.pullDownY < 0.0F) {
                    if(listener != null) {
                        listener.onRefreshStateChange(PullToRefreshLayout.this.pullDownY);
                    }

                    PullToRefreshLayout.this.pullDownY = 0.0F;
                    PullToRefreshLayout.this.mHeaderLoading.clearPullViewAnimation();
                    if(PullToRefreshLayout.this.state != ILoadingLayout.State.REFRESHING && PullToRefreshLayout.this.state != ILoadingLayout.State.LOADING) {
                        PullToRefreshLayout.this.changeState(ILoadingLayout.State.INIT);
                    }

                    PullToRefreshLayout.this.timer.cancel();
                    PullToRefreshLayout.this.requestLayout();
                }

                if(PullToRefreshLayout.this.pullUpY > 0.0F) {
                    PullToRefreshLayout.this.pullUpY = 0.0F;
                    PullToRefreshLayout.this.mFooterLoading.clearPullUpViewAnimation();
                    if(PullToRefreshLayout.this.state != ILoadingLayout.State.REFRESHING && PullToRefreshLayout.this.state != ILoadingLayout.State.LOADING) {
                        PullToRefreshLayout.this.changeState(ILoadingLayout.State.INIT);
                    }

                    PullToRefreshLayout.this.timer.cancel();
                }

                PullToRefreshLayout.this.requestLayout();
                if(PullToRefreshLayout.this.pullDownY + Math.abs(PullToRefreshLayout.this.pullUpY) == 0.0F) {
                    PullToRefreshLayout.this.timer.cancel();
                }

            }
        };
        this.initAttrs(context, attrs);
        this.initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
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
        this.updateHandler = new PullToRefreshLayout.MyHandler(this.mRefreshStateChangeLister) {
            public void handleMessage(Message msg) {
                OnRefreshStateChangeListener listener = this.mRefrshListener.get();
                Log.d(PullToRefreshLayout.TAG, "接受消息:" + PullToRefreshLayout.this.pullDownY);
                PullToRefreshLayout.this.MOVE_SPEED = (float)(8.0D + 5.0D * Math.tan(1.5707963267948966D / (double)PullToRefreshLayout.this.getMeasuredHeight() * (double)(PullToRefreshLayout.this.pullDownY + Math.abs(PullToRefreshLayout.this.pullUpY))));
                if(!PullToRefreshLayout.this.isTouch) {
                    if(PullToRefreshLayout.this.state == ILoadingLayout.State.REFRESHING && PullToRefreshLayout.this.pullDownY <= PullToRefreshLayout.refreshDist) {
                        PullToRefreshLayout.this.pullDownY = PullToRefreshLayout.refreshDist;
                        PullToRefreshLayout.this.timer.cancel();
                    } else if(PullToRefreshLayout.this.state == ILoadingLayout.State.LOADING && -PullToRefreshLayout.this.pullUpY <= PullToRefreshLayout.loadmoreDist) {
                        PullToRefreshLayout.this.pullUpY = -PullToRefreshLayout.loadmoreDist;
                        PullToRefreshLayout.this.timer.cancel();
                    }
                }
                if(PullToRefreshLayout.this.pullDownY > 0.0F) {
                    Log.d(PullToRefreshLayout.TAG,"pullDownY > 0.0F");
                    PullToRefreshLayout.this.pullDownY -= PullToRefreshLayout.this.MOVE_SPEED;
                } else if(PullToRefreshLayout.this.pullUpY < 0.0F) {
                    PullToRefreshLayout.this.pullUpY = PullToRefreshLayout.this.pullUpY + PullToRefreshLayout.this.MOVE_SPEED;
                    Log.d(PullToRefreshLayout.TAG,"pullUpY < 0.0F");
                }

                if(PullToRefreshLayout.this.pullDownY < 0.0F) {
                    if(listener != null) {
                        listener.onRefreshStateChange(PullToRefreshLayout.this.pullDownY);
                    }
                    Log.d(TAG,"pullDownY < 0.0F");
                    PullToRefreshLayout.this.pullDownY = 0.0F;
                    PullToRefreshLayout.this.mHeaderLoading.clearPullViewAnimation();
                    if(PullToRefreshLayout.this.state != ILoadingLayout.State.REFRESHING && PullToRefreshLayout.this.state != ILoadingLayout.State.LOADING) {
                        PullToRefreshLayout.this.changeState(ILoadingLayout.State.INIT);
                    }

                    PullToRefreshLayout.this.timer.cancel();
                    PullToRefreshLayout.this.requestLayout();
                }

                if(PullToRefreshLayout.this.pullUpY > 0.0F) {
                    Log.d(TAG,"pullUpY < 0.0F");
                    PullToRefreshLayout.this.pullUpY = 0.0F;
                    PullToRefreshLayout.this.mFooterLoading.clearPullUpViewAnimation();
                    if(PullToRefreshLayout.this.state != ILoadingLayout.State.REFRESHING && PullToRefreshLayout.this.state != ILoadingLayout.State.LOADING) {
                        PullToRefreshLayout.this.changeState(ILoadingLayout.State.INIT);
                    }

                    PullToRefreshLayout.this.timer.cancel();
                }

                PullToRefreshLayout.this.requestLayout();
                if(PullToRefreshLayout.this.pullDownY + Math.abs(PullToRefreshLayout.this.pullUpY) == 0.0F) {
                    Log.d(TAG,"pullUpY + pullDownY == 0.0F");
                    PullToRefreshLayout.this.timer.cancel();
                }

            }
        };
        this.initAttrs(context, attrs);
        this.initView(context);
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        if(attributeSet != null) {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.PullToRefresh);

            assert a != null;

            this.mode = a.getInteger(R.styleable.PullToRefresh_mode, 1);
            a.recycle();
        }

    }

    private void initView(Context context) {
        this.mHeaderLoading = new GomeHeaderLoadingLayout(context);
        this.mHeaderLoading.setId(R.id.gomeheaderloadinglayout_id);
        this.mFooterLoading = new FooterLoadingLayout(context);
        this.mFooterLoading.setId(R.id.footerloadinglayout_id);
        this.mContext = context;
        this.timer = new PullToRefreshLayout.MyTimer(this.updateHandler);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!this.isMeasure) {
            this.isMeasure = true;
            this.pullableView = this.getChildAt(0);
            LayoutParams paramsHeader = new LayoutParams(-1, -1);
            LayoutParams paramsFooter = new LayoutParams(-1, -1);
            this.addView(this.mHeaderLoading, paramsHeader);
            this.addView(this.mFooterLoading, paramsFooter);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(!this.isLayout) {
            this.isLayout = true;
            refreshDist = (float)this.mHeaderLoading.getContentHeight();
            loadmoreDist = (float)this.mFooterLoading.getContentHeight();
        }

        this.mHeaderLoading.layout(0, (int)(this.pullDownY + this.pullUpY) - this.mHeaderLoading.getMeasuredHeight(), this.mHeaderLoading.getMeasuredWidth(), (int)(this.pullDownY + this.pullUpY));
        this.pullableView.layout(0, (int)(this.pullDownY + this.pullUpY), this.pullableView.getMeasuredWidth(), (int)(this.pullDownY + this.pullUpY) + this.pullableView.getMeasuredHeight());
        this.mFooterLoading.layout(0, (int)(this.pullDownY + this.pullUpY) + this.pullableView.getMeasuredHeight(), this.mFooterLoading.getMeasuredWidth(), (int)(this.pullDownY + this.pullUpY) + this.pullableView.getMeasuredHeight() + this.mFooterLoading.getMeasuredHeight());
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(this.pullableView instanceof Pullable) {
            switch(ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    this.xDistance = this.yDistance = 0.0F;
                    this.xLast = ev.getX();
                    this.yLast = ev.getY();
                    this.downY = ev.getY();
                    this.lastY = this.downY;
                    this.timer.cancel();
                    this.mEvents = 0;
                    this.releasePull();
                    break;
                case MotionEvent.ACTION_UP:
                    if(this.pullDownY > refreshDist || -this.pullUpY > loadmoreDist) {
                        this.isTouch = false;
                    }

                    if(this.state == ILoadingLayout.State.RELEASE_TO_REFRESH) {
                        this.changeState(ILoadingLayout.State.REFRESHING);
                        if(this.mListener != null) {
                            this.mListener.onRefresh();
                        }
                    } else if(this.state == ILoadingLayout.State.RELEASE_TO_LOAD) {
                        this.changeState(ILoadingLayout.State.LOADING);
                        if(this.mListener != null) {
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
                    if(this.mEvents == 0 && (double)this.yDistance > 1.5D * (double)this.xDistance) {
                        if(this.pullDownY <= 0.0F && (!((Pullable)this.pullableView).canPullDown() || !this.canPullDown || this.state == ILoadingLayout.State.LOADING)) {
                            if(this.pullUpY >= 0.0F && (!((Pullable)this.pullableView).canPullUp() || !this.canPullUp || this.state == ILoadingLayout.State.REFRESHING)) {
                                this.releasePull();
                            } else {
                                this.pullUpY += (ev.getY() - this.lastY) / this.radio;
                                this.mFooterLoading.onPullUpY(this.pullUpY);
                                if(this.pullUpY > 0.0F) {
                                    this.pullUpY = 0.0F;
                                    if(this.mode == 3) {
                                        this.canPullDown = true;
                                        this.canPullUp = false;
                                    }
                                }

                                if(this.pullUpY < (float)(-this.getMeasuredHeight())) {
                                    this.pullUpY = (float)(-this.getMeasuredHeight());
                                }

                                if(this.state == ILoadingLayout.State.LOADING) {
                                    this.isTouch = true;
                                }
                            }
                        } else {
                            this.pullDownY += (ev.getY() - this.lastY) / this.radio;
                            this.mHeaderLoading.onPullDownY(ev.getY() - this.downY);
                            if(this.pullDownY < 0.0F) {
                                this.pullDownY = 0.0F;
                                if(this.mode == 3) {
                                    this.canPullDown = false;
                                    this.canPullUp = true;
                                }
                            }

                            if(mHeaderLoading != null){

                                mHeaderLoading.updata(pullDownY);
                            }
                            if(this.pullDownY > (float)this.getMeasuredHeight()) {
                                this.pullDownY = (float)this.getMeasuredHeight();
                            }

                            if(this.state == ILoadingLayout.State.REFRESHING) {
                                this.isTouch = true;
                            }
                        }
                    } else {
                        this.mEvents = 0;
                    }

                    this.lastY = ev.getY();
                    this.radio = (float)(2.0D + 2.0D * Math.tan(1.5707963267948966D / (double)this.getMeasuredHeight() * (double)(this.pullDownY + Math.abs(this.pullUpY))));
                    if(this.pullDownY > 0.0F || this.pullUpY < 0.0F) {
                        this.requestLayout();
                    }

                    if(this.pullDownY > 0.0F) {
                        if(this.pullDownY <= refreshDist && (this.state == ILoadingLayout.State.RELEASE_TO_REFRESH || this.state == ILoadingLayout.State.DONE)) {
                            this.changeState(ILoadingLayout.State.INIT);
                        }

                        if(this.pullDownY >= refreshDist && this.state == ILoadingLayout.State.INIT) {
                            this.changeState(ILoadingLayout.State.RELEASE_TO_REFRESH);
                        }
                    } else if(this.pullUpY < 0.0F) {
                        if(-this.pullUpY <= loadmoreDist && (this.state == ILoadingLayout.State.RELEASE_TO_LOAD || this.state == ILoadingLayout.State.DONE)) {
                            this.changeState(ILoadingLayout.State.INIT);
                        }

                        if(-this.pullUpY >= loadmoreDist && this.state == ILoadingLayout.State.INIT) {
                            this.changeState(ILoadingLayout.State.RELEASE_TO_LOAD);
                        }
                    }

                    if(this.pullDownY + Math.abs(this.pullUpY) > 8.0F) {
                        ev.setAction(3);
                    }

                    if(this.mRefreshStateChangeLister != null) {
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
        if((to == ILoadingLayout.State.INIT || to == ILoadingLayout.State.DONE) && this.mRefreshStateChangeLister != null) {
            this.mRefreshStateChangeLister.onRefreshStateChange(-1.0F);
        }

        this.state = to;
        this.mHeaderLoading.setState(to);
        this.mFooterLoading.setState(to);
    }

    public void refreshFinish(final ILoadingLayout.State refreshResult) {
        (new Handler() {
            public void handleMessage(Message msg) {
                PullToRefreshLayout.this.changeState(refreshResult);
                PullToRefreshLayout.this.changeState(ILoadingLayout.State.DONE);
                PullToRefreshLayout.this.hide();
            }
        }).sendEmptyMessageDelayed(0, 1000L);
    }

    public void loadmoreFinish(ILoadingLayout.State refreshResult, boolean isHasMore, String loadMessage) {
        this.changeState(refreshResult);
        this.changeState(ILoadingLayout.State.DONE);
        this.hide();
        if(loadMessage != null) {
            this.mFooterLoading.onLoadWithMessage(loadMessage);
        }

    }

    private void hide() {
        this.timer.schedule(5L);
    }

    private void releasePull() {
        switch(this.mode) {
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
        PullToRefreshLayout.AutoRefreshAndLoadTask task = new PullToRefreshLayout.AutoRefreshAndLoadTask();
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
        if(this.mFooterLoading != null) {
            this.mFooterLoading.setmInitText(mInitText);
        }

    }

    public void setmReleaseToLoadText(int mReleaseToLoadText) {
        if(this.mFooterLoading != null) {
            this.mFooterLoading.setmReleaseToLoadText(mReleaseToLoadText);
        }

    }

    private static class MyTimer {
        private PullToRefreshLayout.MyHandler handler;
        private Timer timer;
        private PullToRefreshLayout.MyTimer.MyTask mTask;

        public boolean isCancel = true;

        public MyTimer(PullToRefreshLayout.MyHandler handler) {
            this.handler = handler;
            this.timer = new Timer();
        }

        public void schedule(long period) {
            Log.d(PullToRefreshLayout.TAG, "执行schedule:");
            if(this.mTask != null) {
                this.mTask.cancel();
                this.mTask = null;
                isCancel = true;
            }

            this.mTask = new PullToRefreshLayout.MyTimer.MyTask(this.handler);
            this.timer.schedule(this.mTask, 0L, period);
            isCancel = false;
        }

        public void cancel() {
            if(this.mTask != null) {
                this.mTask.cancel();
                this.mTask = null;
                isCancel = true;
            }
        }

        public boolean isCancel(){

            return isCancel;
        }

        private static class MyTask extends TimerTask {
            protected final WeakReference<PullToRefreshLayout.MyHandler> handlerWeakReference;

            public MyTask(PullToRefreshLayout.MyHandler handler) {
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
            while(PullToRefreshLayout.this.pullDownY < 1.0F * PullToRefreshLayout.refreshDist) {
                PullToRefreshLayout.this.pullDownY += PullToRefreshLayout.this.MOVE_SPEED;
                this.publishProgress(new Float[]{Float.valueOf(PullToRefreshLayout.this.pullDownY)});

                try {
                    Thread.sleep((long)params[0].intValue());
                } catch (InterruptedException var3) {
                    var3.printStackTrace();
                }
            }

            return null;
        }

        protected void onPostExecute(String result) {
            PullToRefreshLayout.this.changeState(ILoadingLayout.State.REFRESHING);
            if(PullToRefreshLayout.this.mListener != null) {
                PullToRefreshLayout.this.mListener.onRefresh();
                PullToRefreshLayout.this.refreshFinish(ILoadingLayout.State.REFRESHSUCCESS);
            }

            PullToRefreshLayout.this.hide();
        }

        protected void onProgressUpdate(Float... values) {
            if(PullToRefreshLayout.this.pullDownY > PullToRefreshLayout.refreshDist) {
                PullToRefreshLayout.this.changeState(ILoadingLayout.State.RELEASE_TO_REFRESH);
            }

            PullToRefreshLayout.this.requestLayout();
        }
    }

    private static class MyHandler extends Handler {
        public final WeakReference<OnRefreshStateChangeListener> mRefrshListener;

        public MyHandler(OnRefreshStateChangeListener listener) {
            this.mRefrshListener = new WeakReference(listener);
        }
    }
}
