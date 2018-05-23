package com.ljs.customview.utils;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * 作者：Administrator create on 2018/5/22
 * <p/>
 * 内容：
 */
public class DefaultItemTouchHelper extends ItemTouchHelper {
    DefaultItemTouchHelperCallback callback;
    public DefaultItemTouchHelper(DefaultItemTouchHelperCallback callback) {
        super(callback);
        this.callback = callback;
    }
    /**
     * 设置是否可以被拖拽
     *
     * @param canDrag 是true，否false
     */
    public void setDragEnable(boolean canDrag) {
        callback.setDragEnable(canDrag);
}

    /**
     * 设置是否可以被滑动
     *
     * @param canSwipe 是true，否false
     */
    public void setSwipeEnable(boolean canSwipe) {
        callback.setSwipeEnable(canSwipe);
    }

    /**
     * 设置滑动删除方向
     *
     * @param deleteDir 左ItemTouchHelper.LEFT，左ItemTouchHelper.RIGHT
     */
    public void setDeleteDir(int deleteDir) {
        callback.setDeleteDir(deleteDir);
    }
}