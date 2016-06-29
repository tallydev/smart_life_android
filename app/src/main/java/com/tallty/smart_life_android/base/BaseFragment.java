package com.tallty.smart_life_android.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.utils.ToastUtil;

/**
 * Created by kang on 16/6/21.
 * base fragment
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
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
        initToolBar(toolbar, toolbar_title);
        // 引用组件
        initView();
        // 设置监听器
        setListener();
        // 添加业务逻辑
        processLogic();

        return view;
    }

    public abstract int getFragmentLayout();

    protected abstract void initToolBar(Toolbar toolbar, TextView title);

    protected abstract void initView();

    protected abstract void setListener();

    protected abstract void processLogic();

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
