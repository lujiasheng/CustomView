package com.ljs.customview.listener;

import android.view.View;

/**
 * Created by Administrator on 2018/5/22.
 */
public interface RecyclerListener {
    void addItem(int position);

    void addItem(View view, int position);

    void deleteItem(int position);

    void deleteItem(View view, int position);

    void loadMore();

    void loadMore(View view);

    void onRefresh();

    void onRefresh(View view);

    void onItemClick(int position);

    void onItemClick(View view, int position);

    void onLongItemClick(int position);

    void onLongItemClick(View view, int position);

    void dragItem(int fromPosition, int toPosition);
}
