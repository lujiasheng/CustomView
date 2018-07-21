package com.ljs.customview.adapter.lv;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/7/7.
 */

public class BaseViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private int mPosition;

    private BaseViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mViews = new SparseArray<>();
        this.mPosition = position;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static BaseViewHolder get(Context context, View mConvertView, ViewGroup parent, int layoutId, int position) {
        if (mConvertView == null) {
            return new BaseViewHolder(context, parent, layoutId, position);
        }
        return (BaseViewHolder) mConvertView.getTag();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            return mConvertView.findViewById(viewId);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }
    public BaseViewHolder setText(int viewId,String text){
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
    public BaseViewHolder setImageResource(int viewId,int resource){
        ImageView view = getView(viewId);
        view.setImageResource(resource);
        return this;
    }
    public BaseViewHolder setImageBitmap(int viewId,Bitmap bitmap){
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public int getPosition() {
        return mPosition;
    }
}
