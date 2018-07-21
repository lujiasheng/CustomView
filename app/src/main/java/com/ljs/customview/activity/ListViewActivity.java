package com.ljs.customview.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.ljs.customview.R;
import com.ljs.customview.adapter.lv.BaseViewHolder;
import com.ljs.customview.adapter.lv.CommonAdapter;
import com.ljs.customview.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/7.
 */

public class ListViewActivity extends BaseActivity {
    @BindView(R.id.list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        ButterKnife.bind(this);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("第"+i+"项");
        }
        listView.setAdapter(new CommonAdapter<String>(this,list,R.layout.text_item) {
            @Override
            public void convert(BaseViewHolder holder, String data) {
                holder.setText(R.id.tv_name,data);
            }
        });
    }
}
