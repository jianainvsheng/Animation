package com.gome.pull.down.widget;

public interface ILoadingLayout {
    void setState(ILoadingLayout.State var1);

    ILoadingLayout.State getState();

    int getContentHeight();

    void onPullDownY(float var1);

    void onPullUpY(float var1);

    public static enum State {
        INIT,
        RELEASE_TO_REFRESH,
        REFRESHING,
        REFRESHSUCCESS,
        REFRESHFAIL,
        RELEASE_TO_LOAD,
        LOADING,
        LOADSUCCESS,
        LOADFAIL,
        DONE;

        private State() {
        }
    }
}