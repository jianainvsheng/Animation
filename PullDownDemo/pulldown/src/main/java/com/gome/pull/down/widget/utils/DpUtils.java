package com.gome.pull.down.widget.utils;

import android.content.Context;

public class DpUtils {

    public DpUtils() {
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
}
