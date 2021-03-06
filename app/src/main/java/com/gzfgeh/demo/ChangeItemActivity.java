package com.gzfgeh.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.demo.data.DataProvider;
import com.gzfgeh.demo.data.Person;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.List;
import java.util.Random;

public class ChangeItemActivity extends AutoLayoutActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
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
        adapter = new RecyclerArrayAdapter<Person>(this, R.layout.item_person){

            @Override
            protected void convert(BaseViewHolder viewHolder, Person item) {
                viewHolder.setText(R.id.person_name, item.getName());
                viewHolder.setText(R.id.person_sign, item.getSign());
                viewHolder.setImageUrl(R.id.person_face, item.getFace(), R.mipmap.ic_launcher);
            }
        };

        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                adapter.update(adapter.getAllData().get(0), position);
                return false;
            }
        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
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
        getMenuInflater().inflate(R.menu.menu_insert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Random random = new Random();
        int len = adapter.getCount();
        if (len > 0) {
            int pos = random.nextInt(len);
//        int pos = 0;
            List<Person> persons = DataProvider.getPersonList(0);
            Person data = persons.get(random.nextInt(persons.size()));
            switch (item.getItemId()) {
                case R.id.ic_add:
                    adapter.insert(data, pos);
                    break;
                case R.id.ic_remove:
                    adapter.remove(pos);
                    break;
            }
        }
        return true;
    }


}
