package com.tallty.smart_life_android.fragment.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;
import com.tallty.smart_life_android.event.StartBrotherEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kang on 16/6/20.
 * 购物车
 */
public class CartFragment extends BaseLazyMainFragment {
    private TextView pay;

    public static CartFragment newInstance() {
        Bundle args = new Bundle();

        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void initToolBar(Toolbar toolbar, TextView title) {
        title.setText("购物车");
    }

    @Override
    protected void initView() {
        pay = getViewById(R.id.pay);
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                EventBus.getDefault().post(new StartBrotherEvent(ConfirmOrder.newInstance("确认订单")));
                break;
        }
    }
}
