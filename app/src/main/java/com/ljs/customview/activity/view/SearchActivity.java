package com.ljs.customview.activity.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ljs.customview.R;
import com.ljs.customview.base.BaseActivity;
import com.ljs.customview.view.SearchView;

public class SearchActivity extends BaseActivity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (SearchView) findViewById(R.id.search_view);

    }
    public void start(View view){
        searchView.startSearch();
    }
    public void end(View view){
        searchView.endSearch();
    }
}
