package com.gzfgeh.defaultInterface;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by guo_Devin on 2017/3/15.
 */

public class DefaultFloatActionButtonListener implements FloatActionButtonListener {
    @Override
    public void recyclerViewOnTop(FloatingActionButton floatingActionButton, RecyclerView recyclerView) {
        floatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void recyclerViewOnScroll(FloatingActionButton floatingActionButton, RecyclerView recyclerView) {
        floatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void recyclerViewOnStop(final FloatingActionButton floatingActionButton, final RecyclerView recyclerView) {
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
                floatingActionButton.setVisibility(View.GONE);
            }
        });
    }
}
