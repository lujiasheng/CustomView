package com.ljs.customview.adapter.rv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/7/7.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> datas;
    private Context context;

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }


    public CommonAdapter(List<T> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.create(context, getItemLayoutId(), parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Log.d("CommonAdapter", "position:" + position);
        convert(holder, datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    protected abstract int getItemLayoutId();

    protected abstract void convert(BaseViewHolder holder, T data, int position);
}
