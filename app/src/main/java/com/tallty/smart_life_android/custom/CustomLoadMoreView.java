package com.tallty.smart_life_android.custom;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.tallty.smart_life_android.R;

/**
 * Created by kang on 2016/12/12.
 * RecycleView 加载更多自定义加载视图
 */

public class CustomLoadMoreView extends LoadMoreView {

    /**
     * 如果返回true，数据全部加载完毕后会隐藏加载更多
     * 如果返回false，数据全部加载完毕后会显示getLoadEndViewId()布局
     */
    @Override public boolean isLoadEndGone() {
        return false;
    }

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
        return R.id.load_more_load_end_view;
    }
}
