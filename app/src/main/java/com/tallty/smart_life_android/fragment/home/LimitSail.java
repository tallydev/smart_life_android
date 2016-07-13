package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

/**
 * 首页-限量销售
 */
public class LimitSail extends BaseBackFragment {
    private String mName;

    private Toolbar toolbar;
    private TextView toolbar_title;

    public static LimitSail newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        LimitSail fragment = new LimitSail();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(TOOLBAR_TITLE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_limit_sail;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
    }

    @Override
    public void onClick(View v) {

    }
}