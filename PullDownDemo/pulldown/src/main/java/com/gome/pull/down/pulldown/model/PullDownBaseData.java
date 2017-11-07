package com.gome.pull.down.pulldown.model;

/**
 * Created by yangjian on 2017/10/27.
 */

public class PullDownBaseData {


    private int mType;

    private int mDistance;

    public PullDownBaseData(int type,int distance){

        this.mType = type;
        this.mDistance = distance;
    }
    /**
     * 获得类型
     * @return
     */
    public int getType(){

        return mType;
    }

    /**
     * 获得该类型下可以滑动的距离
     * @return
     */
    public int getDistance(){

        return mDistance;
    }
}
