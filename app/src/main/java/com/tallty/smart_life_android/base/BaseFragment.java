package com.tallty.smart_life_android.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kang on 16/6/21.
 * base fragment
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout_id = getFragmentLayout();
        View view = inflater.inflate(layout_id, container, false);
        // 引用组件
        initView(view);
        // 设置监听器
        setListener();
        // 添加业务逻辑
        processLogic();

        return view;
    }

    public abstract int getFragmentLayout();

    protected abstract void initView(View view);

    protected abstract void setListener();

    protected abstract void processLogic();
}
