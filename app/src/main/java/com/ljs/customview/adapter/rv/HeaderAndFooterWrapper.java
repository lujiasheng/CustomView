package com.ljs.customview.adapter.rv;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.ljs.customview.utils.WrapperUtils;

/**
 * Created by Administrator on 2018/7/7.
 */

public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 10000;
    private static final int BASE_ITEM_TYPE_FOOTER = 20000;

    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFooterViews = new SparseArray<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        this.mInnerAdapter = adapter;
    }

    private boolean isHeardViewPos(int position) {
        return position < getHeaderCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeaderCount() + getRealItemCount();
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    private int getFooterCount() {
        return mFooterViews.size();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(BASE_ITEM_TYPE_HEADER + mHeaderViews.size(), view);
    }

    public void addFooterView(View view) {
        mFooterViews.put(BASE_ITEM_TYPE_FOOTER + mFooterViews.size(), view);
    }

    public void removeHeaderView(int position) {
        mHeaderViews.remove(getItemViewType(position));
    }

    public void removeFooterView(int position) {
        mFooterViews.remove(getItemViewType(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return BaseViewHolder.create(mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {
            return BaseViewHolder.create(mFooterViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (isHeardViewPos(position)) {
            return;
        } else if (isFooterViewPos(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position-getHeaderCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeardViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFooterViews.keyAt(position - getHeaderCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeaderCount());
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getRealItemCount() + getFooterCount();
    }

    public int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType)!=null){
                    return layoutManager.getSpanCount();
                }else if (mFooterViews.get(viewType)!=null){
                   return layoutManager.getSpanCount();
                }
                if (oldLookup!=null){
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeardViewPos(position)||isFooterViewPos(position)){
            WrapperUtils.setFullSpan(holder);
        }
    }
}
