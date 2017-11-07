package com.gome.pull.down.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.gome.pull.down.model.GomeRefreshModel;
import com.gome.pull.down.pulldown.adapter.PullDownBaseAdapter;
import com.gome.pull.down.pulldown.widget.GomeFooterLoadLayout;
import com.gome.pull.down.pulldown.widget.GomeHeaderLoadLayout;

/**
 * Created by yangjian on 2017/10/30.
 */

public class GomeRefreshAdapter extends PullDownBaseAdapter<GomeHeaderLoadLayout, GomeFooterLoadLayout, GomeRefreshModel> {

    public GomeRefreshAdapter(Context context) {
        super(context);
    }

    @Override
    public GomeHeaderLoadLayout getHeadLoadLayout() {
        return new GomeHeaderLoadLayout(getContext());
    }

    @Override
    public GomeFooterLoadLayout getFooterLoadLayout() {
        return new GomeFooterLoadLayout(getContext());
    }

    @Override
    public void onDownUpdata(ViewGroup parentView, GomeHeaderLoadLayout headLoadView, float curentDistance, float maxDistance) {

        if (curentDistance > 0) {
            headLoadView.clearPullViewAnimation();
        }
        if (getDataSize() <= 0 || curentDistance < 0) {

            return;
        }

        if(curentDistance > getData().get(getDataSize() - 1).getDistance()){
            onUpData(getData().get(getDataSize() - 1).getType(), headLoadView, curentDistance, getData().get(getDataSize() - 1).getDistance());
            return;
        }
        for (int i = 0; i < getDataSize(); i++) {

            if (curentDistance < getData().get(i).getDistance()) {

                if (i == 0)
                    onUpData(0, headLoadView, curentDistance, getData().get(i).getDistance());
                else
                    onUpData(getData().get(i - 1).getType(), headLoadView, curentDistance, getData().get(i).getDistance());
                return;
            }
        }
    }

    private void onUpData(int type, GomeHeaderLoadLayout headLoadView, float curentDistance, float maxDistance) {

        switch (type) {

            case 0:
                headLoadView.imageViewNormal.setImageResource(android.R.color.holo_green_dark);
                float scale = (float) curentDistance / maxDistance * 0.9f + 0.1f;
                headLoadView.imageViewNormal.setScaleX(scale);
                headLoadView.imageViewNormal.setScaleY(scale);
                headLoadView.mTextView.setText("松开刷新");
                break;
            case 1:
                headLoadView.imageViewNormal.setScaleX(1.0f);
                headLoadView.imageViewNormal.setScaleY(1.0f);
                headLoadView.mTextView.setText("松开的美逗");
                headLoadView.imageViewNormal.setImageResource(android.R.color.holo_blue_bright);
                break;
            case 2:
                headLoadView.mTextView.setText("松开跳转到web楼层");
                headLoadView.imageViewNormal.setImageResource(android.R.color.holo_red_dark);
                break;
        }
    }
}
