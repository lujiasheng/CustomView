package com.ljs.customview.activity.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljs.customview.R;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.listener.DefaultRecyclerListener;
import com.ljs.customview.utils.DefaultItemTouchHelper;
import com.ljs.customview.utils.DefaultItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DragItemActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_item);
        ButterKnife.bind(this);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
//        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        recyclerView.setLayoutManager(manager);
        final MoveItemAdapter adapter = new MoveItemAdapter();
        DefaultItemTouchHelper itemTouchHelper = new DefaultItemTouchHelper(new DefaultItemTouchHelperCallback(new DefaultRecyclerListener() {

            @Override
            public void dragItem(int fromPosition, int toPosition) {

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(list, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(list, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
            }
        }));
        itemTouchHelper.setDragEnable(true);
        itemTouchHelper.setSwipeEnable(false);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < 20; i++) {
            list.add("第" + i + "个");
        }
        adapter.notifyDataSetChanged();
    }

    class MoveItemAdapter extends RecyclerView.Adapter<MoveItemAdapter.Holder> {


        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(DragItemActivity.this).inflate(R.layout.item_dev_permiss, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.tvName.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_icon)
            ImageView ivIcon;
            @BindView(R.id.tv_name)
            TextView tvName;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
