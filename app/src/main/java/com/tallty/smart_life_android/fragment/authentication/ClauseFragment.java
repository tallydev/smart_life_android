package com.tallty.smart_life_android.fragment.authentication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

/**
 * A simple {@link Fragment} subclass.
 * 注册-服务条款
 */
public class ClauseFragment extends BaseBackFragment {
    private Toolbar toolbar;
    private TextView toolbar_title;

    public static ClauseFragment newInstance() {
        Bundle args = new Bundle();

        ClauseFragment fragment = new ClauseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_clause;
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
        toolbar_title.setText("服务条款");
    }

    @Override
    public void onClick(View v) {

    }
}
