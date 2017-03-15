package com.gzfgeh.defaultInterface;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

/**
 * Created by guo_Devin on 2017/3/15.
 */

public interface FloatActionButtonListener {
    public void recyclerViewOnTop(FloatingActionButton floatingActionButton, RecyclerView recyclerView);
    public void recyclerViewOnScroll(FloatingActionButton floatingActionButton,RecyclerView recyclerView);
    public void recyclerViewOnStop(FloatingActionButton floatingActionButton,RecyclerView recyclerView);
}
