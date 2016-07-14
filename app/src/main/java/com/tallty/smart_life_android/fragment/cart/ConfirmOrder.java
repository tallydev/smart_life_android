package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.StartBrotherEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 购物车-确认订单
 */
public class ConfirmOrder extends BaseBackFragment {
    private String mName;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView submit;
    private RelativeLayout link_to_address;

    public static ConfirmOrder newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        ConfirmOrder fragment = new ConfirmOrder();
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
        return R.layout.fragment_confirm_order;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        submit = getViewById(R.id.submit_order);
        link_to_address = getViewById(R.id.link_to_address);
    }

    @Override
    protected void setListener() {
        submit.setOnClickListener(this);
        link_to_address.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.link_to_address:
                EventBus.getDefault().post(new StartBrotherEvent(MyAddress.newInstance("收货地址")));
                break;
            case R.id.submit_order:
                EventBus.getDefault().post(new StartBrotherEvent(PayOrder.newInstance("支付订单")));
                break;
        }
    }
}
