package com.gzfgeh.demo;

import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.decoration.DividerItemDecoration;
import com.gzfgeh.decoration.StickyHeaderDecoration;
import com.gzfgeh.defaultInterface.DefaultRecyclerViewItem;
import com.gzfgeh.demo.data.DataProvider;
import com.gzfgeh.demo.data.Person;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

public class StickyHeaderActivity extends AutoLayoutActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private GRecyclerView recyclerView;
    private RecyclerArrayAdapter adapter;
    private Handler handler = new Handler();

    private int page = 0;
    private boolean hasNetWork = true;
    public static int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        recyclerView = (GRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setCreateFloatShow(true);
        adapter = new RecyclerArrayAdapter<Person>(this, R.layout.item_person){

            @Override
            protected void convert(BaseViewHolder viewHolder, Person item) {
                viewHolder.setText(R.id.person_name, item.getName());
                viewHolder.setText(R.id.person_sign, item.getSign());
                viewHolder.setImageUrl(R.id.person_face, item.getFace(), R.mipmap.ic_launcher);
            }
        };

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(this));
        decoration.setIncludeHeader(false);
        recyclerView.addItemDecoration(decoration);
        onRefresh();
    }
    //第四页会返回空,意为数据加载结束
    @Override
    public void onLoadMore() {
        Log.i("EasyRecyclerView","onLoadMore");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新
                if (!hasNetWork) {
//                    adapter.pauseMore();
                    recyclerView.showError();
                    return;
                }
                adapter.addAll(DataProvider.getPersonList(page));
                page++;
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        page = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                //刷新
                if (!hasNetWork) {
//                    adapter.pauseMore();
                    recyclerView.showError();
                    return;
                }
                adapter.addAll(DataProvider.getPersonList(page));
                page=1;
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.checkbox);
        CheckBox box = (CheckBox) item.getActionView();
        box.setChecked(true);
        box.setText("网络");
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hasNetWork = isChecked;
            }
        });
        return true;
    }
}
