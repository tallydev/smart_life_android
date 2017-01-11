package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceivePushFragment extends BaseBackFragment {
    private RecyclerView recyclerView;
    private Bundle bundle;

    public static ReceivePushFragment newInstance(Bundle bundle) {
        ReceivePushFragment fragment = new ReceivePushFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            bundle = args;
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_receive_push;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("告警");
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.push_list);
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
