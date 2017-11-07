package com.gome.pull.down;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.pull.down.adapter.GomeRefreshAdapter;
import com.gome.pull.down.model.GomeRefreshModel;
import com.gome.pull.down.pulldown.PullDownLayout;
import com.gome.pull.down.pulldown.widget.GomePullableListView;
import com.gome.pull.down.widget.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRefreshListener {

    private GomePullableListView mListView;

    private PullDownLayout mPullLayout;

    private List<String> mData;

    private MyAdapter mAdapter;

    private Handler mHandler;

    private GomeRefreshAdapter mRefreshAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (GomePullableListView) this.findViewById(R.id.home_page_refresh_list_view);
        mPullLayout = (PullDownLayout) this.findViewById(R.id.home_page_refresh_layout);
        mRefreshAdapter = new GomeRefreshAdapter(this);
        initModel();
        mPullLayout.setRefreshMode(3);
        mPullLayout.setAdapter(mRefreshAdapter);
        mPullLayout.setOnRefreshListener(this);
        mData = new ArrayList<>();
        for(int i = 0 ; i < 30 ; i ++){

            mData.add("asdsad " + i + "*******");
        }
        mHandler = new Handler();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    private void initModel(){

        List<GomeRefreshModel> list = new ArrayList<>();
        GomeRefreshModel model1 = new GomeRefreshModel(1,500);
        GomeRefreshModel model2 = new GomeRefreshModel(2,900);
        list.add(model1);
        list.add(model2);
        mRefreshAdapter.setData(list);
    }
    @Override
    public void onRefresh() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mListView.onRefreshComplete();
                mListView.setHasMore(true);
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mListView.onPullToRefreshLayouLoadFinish(true);
                mListView.setHasMore(true);
            }
        }, 1000);
    }

    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){

                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item,null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.list_item);
            textView.setText(mData.get(position));
            return convertView;
        }
    }
}
