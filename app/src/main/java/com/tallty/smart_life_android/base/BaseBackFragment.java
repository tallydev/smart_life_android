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

import me.yokeyword.fragmentation.SwipeBackLayout;
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
        // 引用组件
        initView();
        // 设置监听器
        setListener();
        // 添加业务逻辑
        processLogic();
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        // 入场动画结束后执行  优化,防动画卡顿
        afterAnimationLogic();
    }

    // 获取布局文件id
    public abstract int getFragmentLayout();
    // find UI
    protected abstract void initView();
    // 设置监听
    protected abstract void setListener();
    // 处理视图逻辑
    protected abstract void processLogic();
    // 转场动画完成后执行(可选)
    protected abstract void afterAnimationLogic();


    /**
     * 设置toolbar的返回按钮
     */
    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                _mActivity.onBackPressed();
                pop();
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
