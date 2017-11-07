package com.gome.pull.down.pulldown.adapter;
import android.content.Context;
import android.view.ViewGroup;

import com.gome.pull.down.pulldown.loadlayout.FooterBaseLoadLayout;
import com.gome.pull.down.pulldown.loadlayout.HeadBaseLoadLayout;
import com.gome.pull.down.pulldown.model.PullDownBaseData;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by yangjian on 2017/10/27.
 */

public abstract class PullDownBaseAdapter<T extends HeadBaseLoadLayout,B extends FooterBaseLoadLayout,D extends PullDownBaseData> {

    /**
     * 下拉的view
     */
    private T mTopLayout;

    /**
     * 上拉的view
     */
    private B mBottomLayout;

    private List<D> mData;

    private Context mContext;

    public PullDownBaseAdapter(Context context){

        mData = new ArrayList<>();
        this.mContext = context;
    }

    /**
     * 设置下拉参数
     * @param datas
     */
    public void setData(List<D> datas){

        if(datas != null && datas.size() > 0){
            this.mData.clear();
            this.mData.addAll(datas);
            datas.clear();
        }
    }

    /**
     * 得到下拉刷新界面
     * @return
     */
    public abstract T getHeadLoadLayout();

    /**
     * 得到上拉加载界面
     * @return
     */
    public abstract B getFooterLoadLayout();

    /**
     * 下拉动作刷新
     * @param parentView
     * @param headLoadView
     * @param curentDistance
     * @param maxDistance
     */
    public abstract void onDownUpdata(ViewGroup parentView,T headLoadView,float curentDistance, float maxDistance);


    /**
     * 获得当前的数据
     * @param position
     * @return
     */
    public D getCurentData(int position){

        if(mData == null || mData.size() < position){
            return null;
        }

        return mData.get(position);
    }

    /**
     * 需要效果的个数
     * @return
     */
    public int getDataSize(){

        return mData == null ? 0 : mData.size();
    }

    public Context getContext() {
        return mContext;
    }

    public List<D> getData(){

        return mData;
    }
}
