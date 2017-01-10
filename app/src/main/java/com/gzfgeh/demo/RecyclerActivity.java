package com.gzfgeh.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {
    private GRecyclerView recyclerView;
    private RecyclerArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recyclerView = (GRecyclerView) findViewById(R.id.recycler);
        adapter = new RecyclerArrayAdapter<String>(this, android.R.layout.activity_list_item) {
            @Override
            protected void convert(BaseViewHolder viewHolder, String item) {
                viewHolder.setText(android.R.id.text1, item);
            }
        };

        getData();
        adapter.addAll(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                String string = " ";
//                for(int i=0; i<position; i++)
//                    string += "";
//                list.add(position+1, string);
//                adapter.clear();
//                adapter.addAll(list);
                list.set(position, "900000");
                adapter.notifyItemChanged(position, "1223");
            }
        });
    }

    private List<String> getData(){
        String string = " ";
        for(int i=0; i<20; i++){
            for(int j=0; j<i; j++)
                string += " ";
            String s = string + i;
            list.add(s);
        }
        return list;
    }
}
