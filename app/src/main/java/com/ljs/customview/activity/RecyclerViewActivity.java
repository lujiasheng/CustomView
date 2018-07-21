package com.ljs.customview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ljs.customview.R;
import com.ljs.customview.adapter.rv.BaseViewHolder;
import com.ljs.customview.adapter.rv.CommonAdapter;
import com.ljs.customview.adapter.rv.HeaderAndFooterWrapper;
import com.ljs.customview.adapter.rv.LoadMoreWrapper;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.listener.DefaultRecyclerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/7.
 */

public class RecyclerViewActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.bind(this);
        recyclerView.setBackgroundResource(R.color.white);
        list = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            list.add("第" + i + "项");
        }
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        final CommonAdapter<String> commonAdapter = new CommonAdapter<String>(list, this) {
            @Override
            protected int getItemLayoutId() {
                return R.layout.text_item;
            }

            @Override
            protected void convert(BaseViewHolder holder, String data, int position) {
                holder.setText(R.id.tv_name, data);
            }
        };

        final HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
        TextView headerView = (TextView) LayoutInflater.from(this).inflate(R.layout.text_item, null);
        TextView footerView = (TextView) LayoutInflater.from(this).inflate(R.layout.text_item, null);
        headerView.setText("这是头部");
        footerView.setText("这是尾部");
        headerAndFooterWrapper.addHeaderView(headerView);
        headerAndFooterWrapper.addFooterView(footerView);
        final LoadMoreWrapper loadMoreWrapper = new LoadMoreWrapper(headerAndFooterWrapper);
        loadMoreWrapper.setLoadMoreView(new ProgressBar(this));
        loadMoreWrapper.setListener(new DefaultRecyclerListener() {
            @Override
            public void loadMore() {
                super.loadMore();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int size = list.size();
                        for (int i = 40; i < 60; i++) {
                            list.add("第" + i + "项");
                        }
                        loadMoreWrapper.notifyItemInserted(size+headerAndFooterWrapper.getHeaderCount());
                    }
                }, 2000);

            }
        });
        recyclerView.setAdapter(loadMoreWrapper);
    }
}
