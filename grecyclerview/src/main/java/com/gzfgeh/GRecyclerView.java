package com.gzfgeh;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.decoration.DividerItemDecoration;
import com.gzfgeh.defaultInterface.DefaultFloatActionButtonListener;
import com.gzfgeh.defaultInterface.FloatActionButtonListener;
import com.gzfgeh.grecyclerview.R;
import com.gzfgeh.swipeheader.SwipeRefreshLayout;


public class GRecyclerView extends FrameLayout {
    public static final String TAG = "EasyRecyclerView";
    public static boolean DEBUG = false;
    protected RecyclerView mRecycler;
    protected ViewGroup mProgressView;
    protected ViewGroup mEmptyView;
    protected ViewGroup mErrorView;
    private FloatingActionButton mFloatButton;
    private int mProgressId;
    private int mEmptyId;
    private int mErrorId;
    private int mErrorNoItemId;
    private int mErrorBtnId;

    protected boolean mClipToPadding;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mScrollbarStyle;
    protected int mScrollbar;

    protected int mDividerPaddingTop;
    protected int mDividerPaddingBottom;
    protected int mDividerPaddingLeft;
    protected int mDividerPaddingRight;
    private Rect rect;

    protected RecyclerView.OnScrollListener mInternalOnScrollListener;
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;
    private FloatActionButtonListener floatActionButtonListener = new DefaultFloatActionButtonListener();
    private boolean isCreateFloatShow =false;

    protected SwipeRefreshLayout mPtrLayout;
    protected android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener mRefreshListener;

    public SwipeRefreshLayout getmPtrLayout(){
        return mPtrLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecycler;
    }

    public GRecyclerView(Context context) {
        super(context);
        initView();
    }

    public GRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public GRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        initView();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.superrecyclerview);
        try {
            mClipToPadding = a.getBoolean(R.styleable.superrecyclerview_recyclerClipToPadding, false);
            mPadding = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPadding, -1.0f);
            mPaddingTop = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingTop, 0.0f);
            mPaddingBottom = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingBottom, 0.0f);
            mPaddingLeft = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingLeft, 0.0f);
            mPaddingRight = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingRight, 0.0f);
            mScrollbarStyle = a.getInteger(R.styleable.superrecyclerview_scrollbarStyle, -1);
            mScrollbar = a.getInteger(R.styleable.superrecyclerview_scrollbars, -1);

            mEmptyId = a.getResourceId(R.styleable.superrecyclerview_layout_empty, 0);
            mProgressId = a.getResourceId(R.styleable.superrecyclerview_layout_progress, 0);
            mErrorId = a.getResourceId(R.styleable.superrecyclerview_layout_error, 0);
            mErrorNoItemId = a.getResourceId(R.styleable.superrecyclerview_layout_error_no_item, 0);

            mDividerPaddingTop = (int) a.getDimension(R.styleable.superrecyclerview_dividerPaddingTop, 0.0f);
            mDividerPaddingBottom = (int) a.getDimension(R.styleable.superrecyclerview_dividerPaddingBottom, 0.0f);
            mDividerPaddingLeft = (int) a.getDimension(R.styleable.superrecyclerview_dividerPaddingLeft, 0.0f);
            mDividerPaddingRight = (int) a.getDimension(R.styleable.superrecyclerview_dividerPaddingRight, 0.0f);
            rect = new Rect(mDividerPaddingLeft, mDividerPaddingTop, mDividerPaddingRight, mDividerPaddingBottom);
        } finally {
            a.recycle();
        }
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }
        //生成主View
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_progress_recyclerview, this);

        mPtrLayout = (SwipeRefreshLayout) v.findViewById(R.id.ptr_layout);
        mPtrLayout.setEnabled(false);

        mFloatButton = (FloatingActionButton) v.findViewById(R.id.fat);
//        mFloatButton.setBackgroundTintList(getResources().getColorStateList(R.color.ff));

        mProgressView = (ViewGroup) v.findViewById(R.id.progress);
        if (mProgressId==0)
            mProgressId = R.layout.view_progress;
        LayoutInflater.from(getContext()).inflate(mProgressId,mProgressView);

        mEmptyView = (ViewGroup) v.findViewById(R.id.empty);
        if (mEmptyId==0)
            mEmptyId = R.layout.view_empty;
        LayoutInflater.from(getContext()).inflate(mEmptyId,mEmptyView);

        mErrorView = (ViewGroup) v.findViewById(R.id.error);
        if (mErrorId==0)
            mErrorId = R.layout.view_error;
        LayoutInflater.from(getContext()).inflate(mErrorId,mErrorView);

        if (mErrorNoItemId == 0)
            mErrorNoItemId = R.layout.view_no_item_error;

        initRecyclerView(v);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mPtrLayout.dispatchTouchEvent(ev);
    }

    /**
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setRecyclerPadding(int left,int top,int right,int bottom){
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
        mRecycler.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    public void setClipToPadding(boolean isClip){
        mRecycler.setClipToPadding(isClip);
    }


    public void setEmptyView(View emptyView){
        mEmptyView.removeAllViews();
        mEmptyView.addView(emptyView);
    }
    public void setProgressView(View progressView){
        mProgressView.removeAllViews();
        mProgressView.addView(progressView);
    }
    public void setErrorView(View errorView){
        mErrorView.removeAllViews();
        mErrorView.addView(errorView);
    }
    public void setEmptyView(int emptyView){
        mEmptyView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(emptyView, mEmptyView);
    }
    public void setProgressView(int progressView){
        mProgressView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(progressView, mProgressView);
    }
    public void setErrorView(int errorView){
        mErrorView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(errorView, mErrorView);
    }

    public void scrollToPosition(int position){
        getRecyclerView().scrollToPosition(position);
    }

    /**
     * Implement this method to customize the AbsListView
     */
    protected void initRecyclerView(View view) {
        mRecycler = (RecyclerView) view.findViewById(android.R.id.list);
        setItemAnimator(null);
        if (mRecycler != null) {
            mRecycler.setHasFixedSize(true);
            mRecycler.setClipToPadding(mClipToPadding);
            mInternalOnScrollListener = new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
                        int pos = ((LinearLayoutManager)(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition();
                        if (pos == 0)
                            ((RecyclerArrayAdapter)(mRecycler.getAdapter())).setLoadItemAnimator(false);
                        else
                            ((RecyclerArrayAdapter)(mRecycler.getAdapter())).setLoadItemAnimator(true);
                    }
                    floatActionButtonListener.recyclerViewOnScroll(mFloatButton,mRecycler);
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);

                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (isCreateFloatShow){
                        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
                            int pos = ((LinearLayoutManager)(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                            if (pos!=0&&newState==0){
                                floatActionButtonListener.recyclerViewOnStop(mFloatButton,mRecycler);
                            }else if (pos==0&&newState==0){
                                floatActionButtonListener.recyclerViewOnTop(mFloatButton,mRecycler);
                            }
                        }
                    }
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener.onScrollStateChanged(recyclerView, newState);

                }
            };
            mRecycler.addOnScrollListener(mInternalOnScrollListener);

            if (mPadding != -1.0f) {
                mRecycler.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mRecycler.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
            if (mScrollbarStyle != -1) {
                mRecycler.setScrollBarStyle(mScrollbarStyle);
            }
            switch (mScrollbar){
                case 0:setVerticalScrollBarEnabled(false);break;
                case 1:setHorizontalScrollBarEnabled(false);break;
                case 2:
                    setVerticalScrollBarEnabled(false);
                    setHorizontalScrollBarEnabled(false);
                    break;
            }
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        mRecycler.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        mRecycler.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
    }

    /**
     * Set the layout manager to the recycler
     *
     * @param manager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecycler.setLayoutManager(manager);
    }

    /**
     * 不满一屏 停止load
     */
    public void isLessMoreScreen(){
        //fix bug  如果不满一屏 不能再次加载更多
        if (getRecyclerView().getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) getRecyclerView().getLayoutManager();

            //获取第一个可见view的位置
            int firstItemPosition = linearManager.findFirstCompletelyVisibleItemPosition();
            int lastItemPosition = linearManager.findLastCompletelyVisibleItemPosition();
            RecyclerArrayAdapter adapter = (RecyclerArrayAdapter) getAdapter();
            if ((0 == firstItemPosition) && (
                    (lastItemPosition == adapter.getItemCount()-1) ||
                            (lastItemPosition == adapter.getCount()-1)
                    )){
                adapter.stopMore();
            }else{
                adapter.resumeMore();
            }
        }
    }


    public static class EasyDataObserver extends AdapterDataObserver {
        private GRecyclerView recyclerView;

        public EasyDataObserver(GRecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            update();
        }

        @Override
        public void onChanged() {
            super.onChanged();
            update();
        }

        //自动更改Container的样式
        private void update() {
            log("update");
            int count;
            if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
                count = ((RecyclerArrayAdapter) recyclerView.getAdapter()).getCount();
            } else {
                count = recyclerView.getAdapter().getItemCount();
            }
            if (count == 0) {
                log("no data:"+"show empty");
                recyclerView.showEmpty();
            } else{
                log("has data");
                recyclerView.showRecycler();
            }

            //fix bug  如果不满一屏 不能再次加载更多
            if (recyclerView.getRecyclerView().getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) recyclerView.getRecyclerView().getLayoutManager();

                //获取第一个可见view的位置
                int firstItemPosition = linearManager.findFirstCompletelyVisibleItemPosition();
                int lastItemPosition = linearManager.findLastCompletelyVisibleItemPosition();
                RecyclerArrayAdapter adapter = (RecyclerArrayAdapter) recyclerView.getAdapter();
                if ((0 == firstItemPosition) && adapter.getCount() != 0 && (
                        (lastItemPosition == adapter.getItemCount()-1) ||
                                (lastItemPosition == adapter.getCount()-1)
                )){
                    adapter.stopMore();
                }
            }
        }
    }

    /**
     * 设置适配器，关闭所有副view。展示recyclerView
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EasyDataObserver(this));
        showRecycler();
    }

    /**
     * 设置适配器，关闭所有副view。展示进度条View
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     *
     * @param adapter
     */
    public void setAdapterWithProgress(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EasyDataObserver(this));
        //只有Adapter为空时才显示ProgressView
        if (adapter instanceof RecyclerArrayAdapter){
            if (((RecyclerArrayAdapter) adapter).getCount() == 0){
                showProgress();
            }else {
                showRecycler();
            }
        }else {
            if (adapter.getItemCount() == 0){
                showProgress();
            }else {
                showRecycler();
            }
        }
    }

    /**
     * Remove the adapter from the recycler
     */
    public void clear() {
        mRecycler.setAdapter(null);
    }


    private void hideAll(){
        mEmptyView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(GONE);
        mPtrLayout.setRefreshing(false);
        mRecycler.setVisibility(View.INVISIBLE);
    }


    public void showError() {
        log("showError");
        if (mErrorView.getChildCount()>0){
            if (getAdapter() instanceof RecyclerArrayAdapter){
                if (((RecyclerArrayAdapter)(getAdapter())).getCount() != 0){
                    ((RecyclerArrayAdapter)(getAdapter())).pauseMore();
                    return;
                }
            }
            hideAll();
            mErrorView.removeAllViews();
            LayoutInflater.from(getContext()).inflate(mErrorNoItemId,mErrorView);
            if (mErrorNoItemId == R.layout.view_no_item_error) {
                mErrorView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        resumeFromError();
                    }
                });
            }else{
                mErrorView.findViewById(mErrorBtnId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resumeFromError();
                    }
                });
            }
            mErrorView.setVisibility(View.VISIBLE);
        }else {
            showRecycler();
        }

    }

    public GRecyclerView setErrorBtnID(@IdRes int resID){
        this.mErrorBtnId = resID;
        mEmptyView.findViewById(resID).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshListener.onRefresh();
            }
        });
        return this;
    }

    public void resumeFromError(){
        showProgress();
        ((RecyclerArrayAdapter)(getAdapter())).resumeMore();
    }

    public void showEmpty() {
        log("showEmpty");
        if (mEmptyView.getChildCount()>0){
            //hideAll();
            showRecycler();
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            showRecycler();
        }
    }


    public void showProgress() {
        log("showProgress");
        if (mProgressView.getChildCount()>0){
            hideAll();
            mProgressView.setVisibility(View.VISIBLE);
        }else {
            showRecycler();
        }
    }


    public void showRecycler() {
        log("showRecycler");
        hideAll();
        mRecycler.setVisibility(View.VISIBLE);
    }


    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     *
     * @param listener
     */
    public void setRefreshListener(android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener listener) {
        mPtrLayout.setEnabled(true);
        mPtrLayout.setOnRefreshListener(listener);
        this.mRefreshListener = listener;
    }

    public void setRefreshing(final boolean isRefreshing){
        mPtrLayout.post(new Runnable() {
            @Override
            public void run() {
                mPtrLayout.setRefreshing(isRefreshing);
                showError();
            }
        });
    }

    public void setRefreshing(final boolean isRefreshing, final boolean isCallbackListener){
        mPtrLayout.post(new Runnable() {
            @Override
            public void run() {
                mPtrLayout.setRefreshing(isRefreshing);
                if (isRefreshing&&isCallbackListener&&mRefreshListener!=null){
                    mRefreshListener.onRefresh();
                    showError();
                }
            }
        });
    }


    /**
     * Set the scroll listener for the recycler
     *
     * @param listener
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }

    /**
     * Add the onItemTouchListener for the recycler
     *
     * @param listener
     */
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.addOnItemTouchListener(listener);
    }

    /**
     * Remove the onItemTouchListener for the recycler
     *
     * @param listener
     */
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.removeOnItemTouchListener(listener);
    }

    /**
     * @return the recycler adapter
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecycler.getAdapter();
    }


    public void setOnTouchListener(OnTouchListener listener) {
        mRecycler.setOnTouchListener(listener);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecycler.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecycler.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.removeItemDecoration(itemDecoration);
    }


    /**
     * @return inflated error view or null
     */
    public View getErrorView() {
        if (mErrorView.getChildCount()>0)return mErrorView.getChildAt(0);
        return null;
    }

    /**
     * @return inflated progress view or null
     */
    public View getProgressView() {
        if (mProgressView.getChildCount()>0)return mProgressView.getChildAt(0);
        return null;
    }


    /**
     * @return inflated empty view or null
     */
    public View getEmptyView() {
        if (mEmptyView.getChildCount()>0)return mEmptyView.getChildAt(0);
        return null;
    }

    /**
     * 默认配置
     * @param adapter
     * @param listener
     * @param refreshListener
     */
    public GRecyclerView setAdapterDefaultConfig(RecyclerArrayAdapter adapter, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener refreshListener, RecyclerArrayAdapter.OnLoadMoreListener listener){
        if (adapter != null) {
            if(listener != null)
                adapter.setMore(listener);
            adapter.setNoMore(R.layout.view_nomore);
            adapter.setError(R.layout.view_error);
            if (refreshListener != null)
                setRefreshListener(refreshListener);
            addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, rect));
            setAdapterWithProgress(adapter);
        }
        return this;
    }

    /**
     * 默认配置
     * @param adapter
     * @param refreshListener
     */
    public GRecyclerView setAdapterDefaultConfig(RecyclerArrayAdapter adapter, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener refreshListener){
        if (adapter != null) {
            adapter.setNoMore(R.layout.view_nomore);
            adapter.setError(R.layout.view_error);
            if (refreshListener != null)
                setRefreshListener(refreshListener);
            addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, rect));
            setAdapterWithProgress(adapter);
        }
        return this;
    }

    private static void log(String content){
        if (DEBUG){
            Log.i(TAG,content);
        }
    }

    public void setFloatActionButtonListener(FloatActionButtonListener floatActionButtonListener) {
        this.floatActionButtonListener = floatActionButtonListener;
    }

    public void setCreateFloatShow(boolean createFloatShow) {
        isCreateFloatShow = createFloatShow;
    }
    public void setFloatSrc(Drawable drawable){
        mFloatButton.setImageDrawable(drawable);
    }
    public void setFloatBackground(ColorStateList tint){
        mFloatButton.setBackgroundTintList(tint);
    }
    /**
     * 设置按钮的阴影宽度
     * */
    public void setFloatelevation(float elevation){
        mFloatButton.setCompatElevation(elevation);
    }
    /**
     * 设置按钮按下去的颜色
     * */
    public void setFloatRipperColor(int color){
        mFloatButton.setRippleColor(color);
    }
}
