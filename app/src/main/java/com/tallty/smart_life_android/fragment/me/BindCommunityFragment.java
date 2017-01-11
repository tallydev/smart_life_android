package com.tallty.smart_life_android.fragment.me;


import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定社区页面
 */
public class BindCommunityFragment extends BaseBackFragment {
    private int position;

    public static BindCommunityFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(Const.INT, position);
        BindCommunityFragment fragment = new BindCommunityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt(Const.INT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_bind_community;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("绑定社区");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {

    }

    @Override
    public void onClick(View v) {

    }
}
