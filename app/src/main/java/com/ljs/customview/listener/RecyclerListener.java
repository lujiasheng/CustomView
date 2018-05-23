package com.ljs.customview.listener;

/**
 * Created by Administrator on 2018/5/22.
 */
public interface RecyclerListener {
    void addItem(int position);

    void deleteItem(int position);

    void loadMore();

    void onRefresh();

    void onItemClick(int position);

    void onLongItemClick(int position);

    void dragItem(int fromPosition, int toPosition);
}
