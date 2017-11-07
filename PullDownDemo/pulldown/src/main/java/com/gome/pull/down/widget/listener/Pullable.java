package com.gome.pull.down.widget.listener;

public interface Pullable {
    boolean canPullDown();

    boolean canPullUp();

    void selfPullUp(float var1);
}
