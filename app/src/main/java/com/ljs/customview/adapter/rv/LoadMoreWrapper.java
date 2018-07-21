package com.ljs.customview.adapter.rv;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ljs.customview.listener.RecyclerListener;
import com.ljs.customview.utils.WrapperUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/7/7.
 */

public class LoadMoreWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LOAD_MORE_TYPE_ITEM = Integer.MAX_VALUE >> 2;

    private View mLoadMoreView;

    private int mLoadMoreLayoutId;

    private RecyclerView.Adapter mInnerAdapter;

    public LoadMoreWrapper(RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_TYPE_ITEM) {
            if (mLoadMoreView != null) {
                return BaseViewHolder.create(mLoadMoreView);
            } else {
                return BaseViewHolder.create(parent.getContext(), mLoadMoreLayoutId, parent);
            }
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (listener != null) {
                listener.loadMore();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return LOAD_MORE_TYPE_ITEM;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }

    private boolean hasLoadMore() {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0;
    }

    public LoadMoreWrapper setLoadMoreView(View loadMoreView) {
        this.mLoadMoreView = loadMoreView;
        return this;
    }

    public LoadMoreWrapper setLoadMoreLayoutId(int loadMoreLayoutId) {
        this.mLoadMoreLayoutId = loadMoreLayoutId;
        return this;
    }

    private RecyclerListener listener;

    public void setListener(RecyclerListener listener) {
        this.listener = listener;
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (isShowLoadMore(holder.getLayoutPosition())) {
            WrapperUtils.setFullSpan(holder);
        }
    }
}
