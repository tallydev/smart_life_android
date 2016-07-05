package com.tallty.smart_life_android.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.utils.ToastUtil;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by kang on 16/6/21.
 * 自定义滑动返回功能的fragment基类
 * 使用SwipeBackFragment开源库
 */
public abstract class BaseBackFragment extends SwipeBackFragment implements View.OnClickListener {
    private static final String TAG = "Fragmentation";
    private View view;
    protected Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout_id = getFragmentLayout();
        view = inflater.inflate(layout_id, container, false);
        context = getActivity().getApplicationContext();
        // 初始化ToolBar
        Toolbar toolbar = getViewById(R.id.toolbar);
        TextView toolbar_title = getViewById(R.id.toolbar_title);
        initToolbarNav(toolbar);
        initToolBar(toolbar, toolbar_title);
        // 引用组件
        initView();

        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        // 入场动画结束后执行  优化,防动画卡顿
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置监听器
        setListener();
        // 添加业务逻辑
        processLogic();
    }

    public abstract int getFragmentLayout();

    protected abstract void initToolBar(Toolbar toolbar, TextView title);

    protected abstract void initView();

    protected abstract void setListener();

    protected abstract void processLogic();


    /**
     * 设置toolbar的返回按钮
     */
    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });

        initToolbarMenu(toolbar);
    }

    /**
     * 设置toolbar的菜单按钮
     */
    private void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        _mActivity.showFragmentStackHierarchyView();
                        _mActivity.logFragmentStackHierarchy(TAG);
                }
                return true;
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 防止getActivity空指针
        this.context = context;
    }

    /**
     * 全局查找View
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) view.findViewById(id);
    }

    /**
     * 显示Toast
     */
    public void showToast(String text) {
        ToastUtil.show(text);
    }
}
