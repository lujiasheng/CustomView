package com.ljs.customview.adapter.rv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/7/7.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;

    private View mConvertView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        this.mConvertView = itemView;

    }

    public static BaseViewHolder create(Context context, int layoutId, ViewGroup parent) {
        return new BaseViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    public static BaseViewHolder create(View itemView) {
        return new BaseViewHolder(itemView);
    }

    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if (view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
    public void setText(int viewId, String text){
        TextView view = getView(viewId);
        view.setText(text);
    }
}
