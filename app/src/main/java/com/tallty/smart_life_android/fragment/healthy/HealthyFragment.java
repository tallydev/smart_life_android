package com.tallty.smart_life_android.fragment.healthy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseFragment;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;

/**
 * Created by kang on 16/6/20.
 * 健康
 */
public class HealthyFragment extends BaseLazyMainFragment {


    public static HealthyFragment newInstance() {
        Bundle args = new Bundle();

        HealthyFragment fragment = new HealthyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_healthy;
    }

    @Override
    protected void initToolBar(Toolbar toolbar, TextView title) {
        title.setText("智慧健康");
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {

    }
}
