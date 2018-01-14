package com.gzfgeh.demo;

import android.content.Context;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.demo.data.Ad;
import com.gzfgeh.demo.data.Person;

/**
 * Description:
 * Created by guzhenfu on 2018/1/14.
 */

public class MultiAdapter extends RecyclerArrayAdapter<Object> {
    public static final int TYPE_INVALID = 0;
    public static final int TYPE_AD = 1;
    public static final int TYPE_PERSON = 2;

    public MultiAdapter(Context context, int resId) {
        super(context, resId);
    }

    @Override
    public int getViewType(int position) {
        if(getItem(position) instanceof Ad){
            return TYPE_AD;
        }else if (getItem(position) instanceof Person){
            return TYPE_PERSON;
        }
        return TYPE_INVALID;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Object item) {

    }
}
