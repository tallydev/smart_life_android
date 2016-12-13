package com.tallty.smart_life_android.custom;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.tallty.smart_life_android.R;

/**
 * Created by kang on 2016/12/12.
 * RecycleView 加载更多自定义加载视图
 */

public class CustomLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.custom_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }
}
